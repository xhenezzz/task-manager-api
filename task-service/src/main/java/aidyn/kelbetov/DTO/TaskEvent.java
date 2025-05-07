package aidyn.kelbetov.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class TaskEvent {
    private String title;
    private Long userId;
    private Date dueDate;
}
