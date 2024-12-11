package mortum.task1.persistence.dto;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String password;

    private Set<RoleDto> roles = new HashSet<>();

    public UserDto(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
