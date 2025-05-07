package aidyn.kelbetov.controller;

import aidyn.kelbetov.DTO.StatusUpdateDto;
import aidyn.kelbetov.DTO.TaskDto;
import aidyn.kelbetov.model.Priority;
import aidyn.kelbetov.model.Status;
import aidyn.kelbetov.model.Task;
import aidyn.kelbetov.utils.JwtUtils;
import aidyn.kelbetov.service.TaskService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {

   private final TaskService taskService;
   private final JwtUtils jwtUtils;

    public TaskController(TaskService taskService, JwtUtils jwtUtils) {
        this.taskService = taskService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/create")
    public ResponseEntity<Task> createTask(@RequestBody TaskDto taskDto,
                                           @RequestHeader("Authorization") String authHeader){
        String token = authHeader.replace("Bearer", "");
        Long userId = jwtUtils.extractUserId(token);

        Task task = taskService.createTask(taskDto, userId);
        return new ResponseEntity<>(task, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Task>> getAllTask(@RequestHeader("Authorization") String authHeader){
        String token = authHeader.replace("Bearer", "").trim();
        Long userId = jwtUtils.extractUserId(token);

        List<Task> tasks = taskService.getTaskForUser(userId);
        return ResponseEntity.ok(tasks);
    }

    @PutMapping("/edit/{taskId}")
    public ResponseEntity<Task> editTask(@RequestBody TaskDto taskDto,
                                         @PathVariable Long taskId,
                                         @RequestHeader("Authorization") String authHeader){
        String token = authHeader.replace("Bearer", "").trim();
        Long userId = jwtUtils.extractUserId(token);
        Task task = taskService.editTaskForUser(taskId,userId,taskDto);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{taskId}")
    public void deleteTask(@PathVariable Long taskId,
                                       @RequestHeader("Authorization") String authHeader){
        String token = authHeader.replace("Bearer", "").trim();
        Long userId = jwtUtils.extractUserId(token);
        taskService.deleteTask(taskId, userId);
    }

    @PatchMapping("/{taskId}/status")
    public ResponseEntity<Task> updateTaskStatus(@PathVariable Long taskId,
                                                 @RequestBody StatusUpdateDto statusDto,
                                                 @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer", "").trim();
        Long userId = jwtUtils.extractUserId(token);

        Task updatedTask = taskService.editTaskStatus(taskId, statusDto.getStatus(), userId);
        return ResponseEntity.ok(updatedTask);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Task>> filterTask(
            @RequestParam(required = false)Status status,
            @RequestParam(required = false)Priority priority,
            @RequestParam(required = false)@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)Date dueDate,
            @RequestHeader("Authorization") String authHeader){
        String token = authHeader.replace("Bearer", "").trim();
        Long userId = jwtUtils.extractUserId(token);

        List<Task> tasks = taskService.filetTasks(userId, status, priority, dueDate);
        return ResponseEntity.ok(tasks);
    }
}
