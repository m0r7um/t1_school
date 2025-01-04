package mortum.task1.services;

import lombok.RequiredArgsConstructor;
import mortum.task1.persistence.dto.RoleDto;
import mortum.task1.persistence.mappers.RoleMapper;
import mortum.task1.persistence.models.RoleEnum;
import mortum.task1.persistence.repositories.RoleRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public RoleDto findByName(RoleEnum role) {
        return roleMapper.roleToRoleDto(
                roleRepository.findByName(role)
                        .orElseThrow(() -> new RuntimeException("Role not found"))
        );
    }
}
