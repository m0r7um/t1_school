package mortum.task1.persistence.mappers;

import mortum.task1.persistence.dto.RoleDto;
import mortum.task1.persistence.dto.UserDto;
import mortum.task1.persistence.models.Role;
import mortum.task1.persistence.models.User;
import org.mapstruct.Mapper;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {
    default User fromUserDtoToUser(UserDto userDto) {
        Set<RoleDto> rolesDto = userDto.getRoles();
        Set<Role> roles = rolesDto.stream().map(roleDto -> new Role(
                roleDto.getId(),
                roleDto.getName()
        )).collect(Collectors.toSet());
        User user = new User(
                userDto.getUsername(),
                userDto.getPassword()
        );
        user.setRoles(roles);
        return user;
    }
}
