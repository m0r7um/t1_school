package mortum.task1.services;

import mortum.task1.persistence.dto.RoleDto;
import mortum.task1.persistence.mappers.RoleMapper;
import mortum.task1.persistence.models.Role;
import mortum.task1.persistence.models.RoleEnum;
import mortum.task1.persistence.repositories.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private RoleMapper roleMapper;

    @InjectMocks
    private RoleService roleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findByName_ShouldReturnRoleDto_WhenRoleExists() {
        RoleEnum roleEnum = RoleEnum.ROLE_USER;
        Role role = new Role(1L, roleEnum);
        RoleDto expectedRoleDto = new RoleDto();
        expectedRoleDto.setId(1L);
        expectedRoleDto.setName(roleEnum);

        when(roleRepository.findByName(roleEnum)).thenReturn(Optional.of(role));
        when(roleMapper.roleToRoleDto(role)).thenReturn(expectedRoleDto);

        RoleDto result = roleService.findByName(roleEnum);

        assertNotNull(result);
        assertEquals(expectedRoleDto, result);
        verify(roleRepository, times(1)).findByName(roleEnum);
        verify(roleMapper, times(1)).roleToRoleDto(role);
    }

    @Test
    void findByName_ShouldThrowException_WhenRoleDoesNotExist() {
        RoleEnum roleEnum = RoleEnum.ROLE_ADMIN;

        when(roleRepository.findByName(roleEnum)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> roleService.findByName(roleEnum));
        assertEquals("Role not found", exception.getMessage());
        verify(roleRepository, times(1)).findByName(roleEnum);
        verifyNoInteractions(roleMapper);
    }
}
