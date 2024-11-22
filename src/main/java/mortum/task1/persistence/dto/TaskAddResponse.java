package mortum.task1.persistence.dto;

import lombok.Data;

@Data
public class TaskAddResponse {
    private Integer id;
    private String title;
    private String description;
    private Integer userId;
}
