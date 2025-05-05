package aidyn.kelbetov.controller;

import aidyn.kelbetov.DTO.AuthResponse;
import aidyn.kelbetov.DTO.LoginDto;
import aidyn.kelbetov.DTO.RegisterDto;
import aidyn.kelbetov.model.User;
import aidyn.kelbetov.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    UserService service;

    public AuthController(UserService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody RegisterDto registerDto){
        User registeredUser = service.register(registerDto);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginDto loginDto){
        AuthResponse response = service.login(loginDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
