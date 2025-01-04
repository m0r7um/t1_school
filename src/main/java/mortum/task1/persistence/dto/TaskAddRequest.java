package mortum.task1.persistence.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mortum.task1.persistence.models.TaskStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskAddRequest {
    private String title;
    private String description;
    private Integer userId;
    private TaskStatus status;
}
