package mortum.task1.persistence.mappers;

import mortum.task1.persistence.dto.*;
import mortum.task1.persistence.models.Task;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    Task fromTaskUpdateRequestToTask(TaskUpdateRequest taskUpdateRequest);

    TaskGetResponse fromTaskToTaskGetResponse(Task task);

    Task fromTaskAddRequestToTask(TaskAddRequest task);

    TaskAddResponse fromTaskToTaskAddResponse(Task task);

    TaskGetAllDto fromTaskToTaskGetAllDto(Task task);
}
