package mortum.task1.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import mortum.task1.KafkaBaseTest;
import mortum.task1.persistence.dto.TaskAddRequest;
import mortum.task1.persistence.dto.TaskAddResponse;
import mortum.task1.persistence.dto.TaskUpdateRequest;
import mortum.task1.persistence.models.Role;
import mortum.task1.persistence.models.RoleEnum;
import mortum.task1.persistence.models.TaskStatus;
import mortum.task1.persistence.models.User;
import mortum.task1.persistence.repositories.UserRepository;
import mortum.task1.services.TaskService;
import mortum.task1.services.UserDetailsImpl;
import mortum.task1.utils.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class TaskControllerIntegrationTest extends KafkaBaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    private String jwtToken;

    @Autowired
    private TaskService taskService;

    @BeforeEach
    public void setup() {
        // Создаем тестового пользователя
        User user = new User();
        user.setLogin("testUser");
        user.setPassword("password123");
        user.setRoles(Set.of(new Role(1L, RoleEnum.ROLE_ADMIN)));
        User userEntity = userRepository.save(user);

        // Генерируем JWT токен для теста
        UserDetails userDetails = UserDetailsImpl.build(userEntity);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        jwtToken = jwtUtils.generateJwtToken(authentication);
    }

    @Test
    public void testGetAllTasks_AsAdmin_ShouldReturnTasks() throws Exception {
        mockMvc.perform(get("/tasks")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isUnauthorized());  // Проверяем, что только админ может получить все задачи
    }

    @Test
    public void testGetTaskById_AsUser_ShouldReturnTask() throws Exception {
        // Добавляем тестовую задачу
        TaskAddRequest taskRequest = new TaskAddRequest("Test Task", "Description", 1, TaskStatus.NOT_STARTED);
        TaskAddResponse taskResponse = taskService.createTask(taskRequest);

        mockMvc.perform(get("/tasks/" + taskResponse.getId())
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(taskResponse.getId()))
                .andExpect(jsonPath("$.title").value("Test Task"))
                .andExpect(jsonPath("$.description").value("Description"));
    }

    @Test
    public void testAddTask_AsUser_ShouldAddTask() throws Exception {
        TaskAddRequest taskRequest = new TaskAddRequest("New Task", "Task description", 1, TaskStatus.NOT_STARTED);

        mockMvc.perform(post("/tasks")
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(objectMapper.writeValueAsString(taskRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New Task"))
                .andExpect(jsonPath("$.description").value("Task description"));
    }

    @Test
    public void testUpdateTask_AsUser_ShouldUpdateTask() throws Exception {
        // Создаем тестовую задачу
        TaskAddRequest taskRequest = new TaskAddRequest("Old Task", "Old Description", 1, TaskStatus.IN_PROGRESS);
        TaskAddResponse taskResponse = taskService.createTask(taskRequest);

        TaskUpdateRequest updateRequest = new TaskUpdateRequest("Updated Task", "Updated Description", 1, TaskStatus.NOT_STARTED);

        mockMvc.perform(put("/tasks/" + taskResponse.getId())
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Task"))
                .andExpect(jsonPath("$.description").value("Updated Description"));
    }

    @Test
    public void testDeleteTask_AsUser_ShouldDeleteTask() throws Exception {
        // Создаем тестовую задачу
        TaskAddRequest taskRequest = new TaskAddRequest("Task to delete", "Description", 1, TaskStatus.NOT_STARTED);
        TaskAddResponse taskResponse = taskService.createTask(taskRequest);

        mockMvc.perform(delete("/tasks/" + taskResponse.getId())
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(taskResponse.getId()));
    }

    @Test
    public void testGetTask_AsUser_ShouldReturnForbiddenIfNotAuthorized() throws Exception {
        // Генерируем JWT для пользователя без ролей или другого пользователя
        User unauthorizedUser = new User();
        unauthorizedUser.setLogin("unauthorizedUser");
        unauthorizedUser.setPassword("password123");
        unauthorizedUser.setRoles(Set.of(new Role(1L, RoleEnum.ROLE_USER)));
        User userEntity = userRepository.save(unauthorizedUser);

        UserDetails userDetails = UserDetailsImpl.build(userEntity);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        String unauthorizedJwtToken = jwtUtils.generateJwtToken(authentication);

        mockMvc.perform(get("/tasks")
                        .header("Authorization", "Bearer " + unauthorizedJwtToken))
                .andExpect(status().isForbidden());
    }
}
