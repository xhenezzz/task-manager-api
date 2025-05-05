package aidyn.kelbetov.service;

import aidyn.kelbetov.DTO.AuthResponse;
import aidyn.kelbetov.DTO.LoginDto;
import aidyn.kelbetov.DTO.RegisterDto;
import aidyn.kelbetov.configuration.JwtTokenProvider;
import aidyn.kelbetov.model.User;
import aidyn.kelbetov.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository repository;
    private final JwtTokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, JwtTokenProvider tokenProvider, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.tokenProvider = tokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(RegisterDto registerDto){
        if(repository.existsByUsername(registerDto.getUsername())){
            throw new RuntimeException("User already existed!");
        }
        User user = new User();
        user.setName(registerDto.getName());
        user.setSurname(registerDto.getSurname());
        user.setUsername(registerDto.getUsername());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        return repository.save(user);
    }

    public AuthResponse login (LoginDto loginDto){
        User user = repository.findByUsername(loginDto.getUsername());
        if(!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())){
            throw new RuntimeException("Password not matches!");
        }
        String token = tokenProvider.generateToken(user);
        return new AuthResponse(token);
    }
}
