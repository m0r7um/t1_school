package mortum.task1.persistence.dto;

import lombok.Data;
import mortum.task1.persistence.models.RoleEnum;

@Data
public class RoleDto {
    private Long id;
    private RoleEnum name;
}
