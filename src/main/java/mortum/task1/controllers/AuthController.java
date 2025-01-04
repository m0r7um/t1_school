package mortum.task1.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mortum.task1.persistence.dto.LoginRequest;
import mortum.task1.persistence.dto.LoginResponse;
import mortum.task1.persistence.dto.SignupRequest;
import mortum.task1.persistence.dto.SignupResponse;
import mortum.task1.services.UserDetailsImpl;
import mortum.task1.services.UserService;
import mortum.task1.utils.JwtUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;

    private final UserService userService;

    private final JwtUtils jwtUtils;

    @PostMapping("/signin")
    public LoginResponse authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return new LoginResponse(
                jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                roles
        );
    }

    @PostMapping("/signup")
    public SignupResponse registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        userService.signupUser(signupRequest);
        SignupResponse signupResponse = new SignupResponse();
        signupResponse.setMessage("User registered successfully");
        return signupResponse;
    }
}
