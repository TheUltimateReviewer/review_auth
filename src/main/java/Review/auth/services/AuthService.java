package Review.auth.services;

import Review.auth.Dtos.AuthResponse;
import Review.auth.Dtos.LoginRequest;
import Review.auth.Dtos.RegisterRequest;
import Review.auth.models.UserEntity;
import Review.auth.respositories.RoleRepository;
import Review.auth.respositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest loginRequest) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        UserDetails user = this.userRepository.findUserEntityByUsername(loginRequest.getUsername())
                .orElseThrow(()->new UsernameNotFoundException("No existe ese usuario"));

        String token = this.jwtService.getToken(user);


        return AuthResponse.builder()
                .token(token)
                .build();
    }

    @Transactional
    public AuthResponse register(RegisterRequest registerRequest) throws Exception {

        UserEntity user = UserEntity.builder().username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .roles(Set.of(this.roleRepository.findById(Long.valueOf(2)).orElseThrow(() -> new Exception("Role not found"))))
                .email(registerRequest.getEmail())
                .phone(registerRequest.getPhone())
                .isEnabled(true)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .build();

        this.userRepository.save(user);

        return AuthResponse.builder()
                .token(this.jwtService.getToken(user))
                .build();
    }

}
