package mortum.task1.services;

import lombok.RequiredArgsConstructor;
import mortum.task1.persistence.dto.*;
import mortum.task1.persistence.mappers.TaskMapper;
import mortum.task1.persistence.models.Task;
import mortum.task1.persistence.repositories.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TaskService {
    final TaskRepository taskRepository;
    final TaskMapper taskMapper;

    public TaskGetResponse getTask(Integer id) {
        Task task = taskRepository.findById(id).orElseThrow();
        return taskMapper.fromTaskToTaskGetResponse(task);
    }

    public TaskGetAllResponse getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        TaskGetAllResponse taskGetAllResponse = new TaskGetAllResponse();
        List<TaskGetAllDto> taskDtoList = tasks.stream().map(taskMapper::fromTaskToTaskGetAllDto).toList();
        taskGetAllResponse.setTasks(taskDtoList);
        return taskGetAllResponse;
    }

    public TaskAddResponse createTask(TaskAddRequest task) {
        Task taskEntity = taskMapper.fromTaskAddRequestToTask(task);
        Task savedTaskEntity = taskRepository.save(taskEntity);
        return taskMapper.fromTaskToTaskAddResponse(savedTaskEntity);
    }

    @Transactional
    public Integer updateTask(TaskUpdateRequest task, Integer id) {
        Task taskEntity = taskMapper.fromTaskUpdateRequestToTask(task);
        return taskRepository.update(taskEntity, id);
    }

    @Transactional
    public Integer deleteTask(Integer id) {
        return taskRepository.customDeleteById(id);
    }
}
