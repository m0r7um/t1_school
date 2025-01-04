package mortum.task1.services;

import mortum.task1.exceptions.UsernameAlreadyExistException;
import mortum.task1.persistence.dto.RoleDto;
import mortum.task1.persistence.dto.SignupRequest;
import mortum.task1.persistence.dto.UserDto;
import mortum.task1.persistence.mappers.UserMapper;
import mortum.task1.persistence.models.RoleEnum;
import mortum.task1.persistence.models.User;
import mortum.task1.persistence.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleService roleService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private SignupRequest signupRequest;

    @BeforeEach
    public void setUp() {
        // Создаем объект для теста
        signupRequest = new SignupRequest();
        signupRequest.setUsername("testUser");
        signupRequest.setPassword("password123");
        signupRequest.setRole(new HashSet<>());
    }

    @Test
    public void signupUser_UsernameAlreadyExists_ThrowsUsernameAlreadyExistException() {
        when(userRepository.existsByLogin("testUser")).thenReturn(true);

        assertThrows(UsernameAlreadyExistException.class, () -> userService.signupUser(signupRequest));

        verify(userRepository, never()).save(any());
    }

    @Test
    public void signupUser_NoRoleAssigned_AssignsUserRole() {
        when(userRepository.existsByLogin("testUser")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        RoleDto userRole = new RoleDto();
        userRole.setName(RoleEnum.ROLE_USER);
        when(roleService.findByName(RoleEnum.ROLE_USER)).thenReturn(userRole);
        when(userMapper.fromUserDtoToUser(any(UserDto.class))).thenReturn(new User());

        UserDto userDto = userService.signupUser(signupRequest);


        Set<String> roleNames = userDto.getRoles().stream()
                .map(roleDto -> roleDto.getName().name()).collect(Collectors.toSet());
        assertEquals(1, userDto.getRoles().size());
        assertTrue(roleNames.contains("ROLE_USER"));

        verify(userRepository, times(1)).save(any());
    }

    @Test
    public void signupUser_RoleAdminAssigned_AssignsAdminRole() {
        when(userRepository.existsByLogin("testUser")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");

        Set<String> roles = new HashSet<>();
        roles.add("admin");
        roles.add("user");
        signupRequest.setRole(roles);

        RoleDto adminRole = new RoleDto();
        adminRole.setName(RoleEnum.ROLE_ADMIN);
        RoleDto userRole = new RoleDto();
        userRole.setName(RoleEnum.ROLE_USER);
        when(roleService.findByName(RoleEnum.ROLE_ADMIN)).thenReturn(adminRole);
        when(roleService.findByName(RoleEnum.ROLE_USER)).thenReturn(userRole);
        when(userMapper.fromUserDtoToUser(any(UserDto.class))).thenReturn(new User());

        UserDto userDto = userService.signupUser(signupRequest);

        Set<String> roleNames = userDto.getRoles().stream()
                .map(roleDto -> roleDto.getName().name()).collect(Collectors.toSet());
        assertEquals(2, roleNames.size());
        assertTrue(roleNames.contains("ROLE_USER"));
        assertTrue(roleNames.contains("ROLE_ADMIN"));

        verify(userRepository, times(1)).save(any());
    }

}
