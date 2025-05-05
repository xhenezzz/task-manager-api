package aidyn.kelbetov.DTO;

import aidyn.kelbetov.model.Priority;
import aidyn.kelbetov.model.Status;
import lombok.Data;

import java.util.Date;
@Data
public class TaskDto {
    private String title;
    private String description;
    private Date dueDate;
    private Priority priority;
    private Status status;
}
