package mortum.task1.services;

import lombok.RequiredArgsConstructor;
import mortum.task1.exceptions.UsernameAlreadyExistException;
import mortum.task1.persistence.dto.RoleDto;
import mortum.task1.persistence.dto.SignupRequest;
import mortum.task1.persistence.dto.UserDto;
import mortum.task1.persistence.mappers.UserMapper;
import mortum.task1.persistence.models.RoleEnum;
import mortum.task1.persistence.models.User;
import mortum.task1.persistence.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final UserMapper userMapper;

    public UserDto signupUser(SignupRequest signupRequest) {
        if (userRepository.existsByLogin(signupRequest.getUsername())) {
            throw new UsernameAlreadyExistException("Username already exists");
        }

        UserDto userDto = new UserDto(signupRequest.getUsername(),
                passwordEncoder.encode(signupRequest.getPassword()));
        Set<String> strRoles = signupRequest.getRole();
        Set<RoleDto> roles = new HashSet<>();

        if (strRoles == null || strRoles.isEmpty()) {
            RoleDto userRole = roleService.findByName(RoleEnum.ROLE_USER);
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                if (role.equals("admin")) {
                    RoleDto adminRole = roleService.findByName(RoleEnum.ROLE_ADMIN);
                    roles.add(adminRole);
                } else {
                    RoleDto userRole = roleService.findByName(RoleEnum.ROLE_USER);
                    roles.add(userRole);
                }
            });
        }
        userDto.setRoles(roles);

        User user = userMapper.fromUserDtoToUser(userDto);

        userRepository.save(user);

        return userDto;
    }
}
