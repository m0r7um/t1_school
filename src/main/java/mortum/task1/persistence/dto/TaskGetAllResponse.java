package mortum.task1.persistence.dto;

import lombok.Data;

import java.util.List;

@Data
public class TaskGetAllResponse {
    protected List<TaskGetAllDto> tasks;
}
