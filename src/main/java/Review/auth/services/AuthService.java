package Review.auth.services;

import Review.auth.Dtos.AuthResponse;
import Review.auth.Dtos.LoginRequest;
import Review.auth.Dtos.RegisterRequest;
import Review.auth.Dtos.UserDTO;
import Review.auth.models.UserEntity;
import Review.auth.respositories.RoleRepository;
import Review.auth.respositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
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

    //@Autowired
    //private RabbitmqProducer rabbitmqProducer;

    @Autowired
    private StreamBridge streamBridge;

    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest loginRequest) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        UserDetails user = this.userRepository.findUserEntityByUsername(loginRequest.getUsername())
                .orElseThrow(()->new UsernameNotFoundException("No existe ese usuario"));
        UserEntity userWithId = this.userRepository.findUserEntityByUsername(loginRequest.getUsername()).get();

        String token = this.jwtService.getToken(user, userWithId.getId(), userWithId.getProfileId());


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
                .profileId(Long.valueOf(0))
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .build();

        UserEntity userWithId=this.userRepository.save(user);
        boolean sent = streamBridge.send("userCreated-out-0", UserDTO.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .build()
        );
        if (!sent) {
            // Aquí puedes manejar el error de envío según tus necesidades
            throw new RuntimeException("Error al enviar el evento de creación de usuario");
        }
 /*       try {
            this.rabbitmqProducer.sendMessage("user.exchange", "user.created", UserDTO.builder()
                    .userId(user.getId())
                    .username(user.getUsername())
                    .build()

            );

        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("error al enviar mensaje por cola");
        }
*/

        return AuthResponse.builder()
                .token(this.jwtService.getToken(user, userWithId.getId(), userWithId.getProfileId()))
                .build();
    }

}
