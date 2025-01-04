package mortum.task1.services;

import mortum.task1.kafka.KafkaNotificationProducer;
import mortum.task1.kafka.dto.NotificationDto;
import mortum.task1.persistence.dto.*;
import mortum.task1.persistence.mappers.TaskMapper;
import mortum.task1.persistence.models.Task;
import mortum.task1.persistence.models.TaskStatus;
import mortum.task1.persistence.repositories.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private KafkaNotificationProducer kafkaNotificationProducer;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getTask_ShouldReturnTaskGetResponse_WhenTaskExists() {
        // Arrange
        Integer taskId = 1;
        Task task = new Task();
        task.setId(taskId);
        task.setTitle("Test Task");
        TaskGetResponse expectedResponse = new TaskGetResponse();
        expectedResponse.setId(taskId);
        expectedResponse.setTitle("Test Task");

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskMapper.fromTaskToTaskGetResponse(task)).thenReturn(expectedResponse);

        TaskGetResponse result = taskService.getTask(taskId);

        assertNotNull(result);
        assertEquals(expectedResponse, result);
        verify(taskRepository, times(1)).findById(taskId);
        verify(taskMapper, times(1)).fromTaskToTaskGetResponse(task);
    }

    @Test
    void getTask_ShouldThrowException_WhenTaskDoesNotExist() {
        Integer taskId = 1;
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> taskService.getTask(taskId));
        verify(taskRepository, times(1)).findById(taskId);
    }

    @Test
    void getAllTasks_ShouldReturnTaskGetAllResponse() {
        Task task = new Task();
        task.setId(1);
        task.setTitle("Task 1");
        TaskGetAllDto dto = new TaskGetAllDto();
        dto.setId(1);
        dto.setTitle("Task 1");

        when(taskRepository.findAll()).thenReturn(List.of(task));
        when(taskMapper.fromTaskToTaskGetAllDto(task)).thenReturn(dto);

        TaskGetAllResponse result = taskService.getAllTasks();

        assertNotNull(result);
        assertEquals(1, result.getTasks().size());
        assertEquals(dto, result.getTasks().get(0));
        verify(taskRepository, times(1)).findAll();
        verify(taskMapper, times(1)).fromTaskToTaskGetAllDto(task);
    }

    @Test
    void createTask_ShouldReturnTaskAddResponse() {
        // Arrange
        TaskAddRequest request = new TaskAddRequest();
        request.setTitle("New Task");
        Task task = new Task();
        task.setTitle("New Task");
        Task savedTask = new Task();
        savedTask.setId(1);
        savedTask.setTitle("New Task");
        TaskAddResponse expectedResponse = new TaskAddResponse();
        expectedResponse.setId(1);
        expectedResponse.setTitle("New Task");

        when(taskMapper.fromTaskAddRequestToTask(request)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(savedTask);
        when(taskMapper.fromTaskToTaskAddResponse(savedTask)).thenReturn(expectedResponse);

        TaskAddResponse result = taskService.createTask(request);

        assertNotNull(result);
        assertEquals(expectedResponse, result);
        verify(taskMapper, times(1)).fromTaskAddRequestToTask(request);
        verify(taskRepository, times(1)).save(task);
        verify(taskMapper, times(1)).fromTaskToTaskAddResponse(savedTask);
    }

    @Test
    void updateTask_ShouldUpdateAndSendNotification_WhenStatusChanges() {
        Integer taskId = 1;
        String testTopic = "test-topic";
        taskService.setTopic(testTopic);

        TaskUpdateRequest request = new TaskUpdateRequest();
        request.setStatus(TaskStatus.COMPLETED);
        request.setUserId(100);
        Task existingTask = new Task();
        existingTask.setStatus(TaskStatus.PENDING);
        Task updatedTask = new Task();

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskMapper.fromTaskUpdateRequestToTask(request)).thenReturn(updatedTask);
        when(taskRepository.update(updatedTask, taskId)).thenReturn(1);

        Integer updatedCount = taskService.updateTask(request, taskId);

        assertEquals(1, updatedCount);
        verify(taskRepository, times(1)).findById(taskId);
        verify(taskMapper, times(1)).fromTaskUpdateRequestToTask(request);
        verify(taskRepository, times(1)).update(updatedTask, taskId);
        verify(kafkaNotificationProducer, times(1))
                .sendTo(eq(testTopic), any(NotificationDto.class), eq(100));
    }

    @Test
    void deleteTask_ShouldDeleteTask() {
        Integer taskId = 1;
        when(taskRepository.customDeleteById(taskId)).thenReturn(1);

        Integer deletedCount = taskService.deleteTask(taskId);

        assertEquals(1, deletedCount);
        verify(taskRepository, times(1)).customDeleteById(taskId);
    }
}
