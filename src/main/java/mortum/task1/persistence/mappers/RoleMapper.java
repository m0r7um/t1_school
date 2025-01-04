package mortum.task1.persistence.mappers;

import mortum.task1.persistence.dto.RoleDto;
import mortum.task1.persistence.models.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleDto roleToRoleDto(Role role);
}
