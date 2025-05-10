package aidyn.kelbetov.service;

import aidyn.kelbetov.DTO.TaskDto;
import aidyn.kelbetov.DTO.TaskEventDto;
import aidyn.kelbetov.kafka.KafkaPublisher;
import aidyn.kelbetov.model.Priority;
import aidyn.kelbetov.model.Status;
import aidyn.kelbetov.model.Task;
import aidyn.kelbetov.repository.TaskRepository;
import aidyn.kelbetov.utils.TaskSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final KafkaPublisher kafkaPublisher;

    public TaskService(TaskRepository taskRepository, KafkaPublisher kafkaPublisher) {
        this.taskRepository = taskRepository;
        this.kafkaPublisher = kafkaPublisher;
    }

    public Task createTask(TaskDto taskDto, Long userId){
        Task task = new Task();
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setStatus(Status.TODO);
        task.setPriority(taskDto.getPriority());
        task.setDueDate(taskDto.getDueDate());
        task.setUserId(userId);

        if (task.getDueDate() != null && isDeadlineTomorrow(task.getDueDate())) {
            kafkaPublisher.sendMessage(new TaskEventDto(task.getTitle(), userId, task.getDueDate()));
        }

        return taskRepository.save(task);
    }

    public List<Task> getTaskForUser(Long userId){
        List<Task> tasks = taskRepository.getListOfTasksByUserId(userId);
        if(tasks.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No tasks found for user");
        }
        return tasks;
    }

    public Task editTaskForUser(Long taskId, Long userId, TaskDto taskDto){
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found!"));

        if(!task.getUserId().equals(userId)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not owner of this task!");
        }

        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setDueDate(taskDto.getDueDate());
        task.setPriority(taskDto.getPriority());
        task.setStatus(taskDto.getStatus());
        return taskRepository.save(task);
    }

    public void deleteTask(Long taskId, Long userId){
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found!"));

        if(!task.getUserId().equals(userId)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not owner of this task!");
        }

        taskRepository.delete(task);
    }

    public Task editTaskStatus(Long taskId, Status status, Long userId){
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found!"));

        if(!task.getUserId().equals(userId)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not owner of this task!");
        }

        task.setStatus(status);
        return taskRepository.save(task);
    }

    public List<Task> filetTasks(Long userId, Status status, Priority priority, Date dueDate){
        Specification<Task> spec = TaskSpecification.byFilters(status, priority, dueDate, userId);
        return taskRepository.findAll(spec);
    }

    private boolean isDeadlineTomorrow(Date dueDate) {
        LocalDate due = dueDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        return due.equals(tomorrow);
    }
}
