package mortum.task1.persistence.dto;

import lombok.Data;
import mortum.task1.persistence.models.TaskStatus;

@Data
public class TaskUpdateRequest {
    private String title;
    private String description;
    private Integer userId;
    private TaskStatus status;
}
