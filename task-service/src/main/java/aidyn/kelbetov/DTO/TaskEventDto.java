package aidyn.kelbetov.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskEventDto {
    private String title;
    private Long userId;
    private Date dueDate;
}
