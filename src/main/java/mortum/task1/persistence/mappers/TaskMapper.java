package mortum.task1.persistence.mappers;

import mortum.task1.persistence.dto.TaskAddRequest;
import mortum.task1.persistence.dto.TaskAddResponse;
import mortum.task1.persistence.dto.TaskGetResponse;
import mortum.task1.persistence.dto.TaskUpdateRequest;
import mortum.task1.persistence.models.Task;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface TaskMapper {
    Task fromTaskUpdateRequestToTask(TaskUpdateRequest taskUpdateRequest);
    TaskGetResponse fromTaskToTaskGetResponse(Task task);
    Task fromTaskAddRequestToTask(TaskAddRequest task);
    TaskAddResponse fromTaskToTaskAddResponse(Task task);
}
