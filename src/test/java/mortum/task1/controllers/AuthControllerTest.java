package mortum.task1.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import mortum.task1.persistence.dto.LoginRequest;
import mortum.task1.persistence.dto.SignupRequest;
import mortum.task1.persistence.repositories.UserRepository;
import mortum.task1.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @BeforeEach
    @Transactional
    public void setUp() {
        // Удаляем все записи перед каждым тестом (например, всех пользователей)
        userRepository.deleteAll(); // Удаляет всех пользователей
    }


    @Test
    public void testSignupUser() throws Exception {
        // Создаем объект для регистрации
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername("testUser");
        signupRequest.setPassword("password123");

        // Выполняем запрос на регистрацию
        mockMvc.perform(post("/auth/signup")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registered successfully"));
    }

    @Test
    public void testAuthenticateUser_Success() throws Exception {
        // 1. Создаем нового пользователя (для теста)
        SignupRequest signupRequest = new SignupRequest("testUser", "password123", null);
        userService.signupUser(signupRequest); // Регистрация пользователя

        // 2. Аутентификация с правильными данными
        LoginRequest loginRequest = new LoginRequest("testUser", "password123");

        // 3. Выполнение запроса на авторизацию
        mockMvc.perform(post("/auth/signin")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testUser"))
                .andExpect(jsonPath("$.roles[0]").value("ROLE_USER"))
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    public void testAuthenticateUser_Failure_InvalidCredentials() throws Exception {
        // 1. Аутентификация с неправильными данными
        LoginRequest loginRequest = new LoginRequest("nonExistentUser", "wrongPassword");

        // 2. Выполнение запроса и проверка, что возвращается статус 401
        mockMvc.perform(post("/auth/signin")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Unauthorized"))
                .andExpect(jsonPath("$.message").value("Bad credentials"));
    }
}
