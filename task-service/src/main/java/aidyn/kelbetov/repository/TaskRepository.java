package aidyn.kelbetov.repository;

import aidyn.kelbetov.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> getListOfTasksByUserId(Long userId);
}
