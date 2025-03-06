package Review.auth.RabbitMQ;

import Review.auth.Dtos.ProfileCreatedDTO;
import Review.auth.models.UserEntity;
import Review.auth.respositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.beans.BeanProperty;
import java.util.function.Consumer;

@Component
@Slf4j
public class RabbitMQConsumer {
    @Autowired
    private UserRepository userRepository;

    @Bean
    public Consumer<ProfileCreatedDTO> profileCreated() {
        return profileCreatedDTO -> {
            UserEntity user = this.userRepository.findById(profileCreatedDTO.getUserId()).orElseThrow(()->new RuntimeException("no se encontro el usuario"));
            user.setProfileId(profileCreatedDTO.getProfileId());
            this.userRepository.save(user);
            log.info("Usuario {} actualizado con profileId {}", user.getUsername(), profileCreatedDTO.getProfileId());
        };
    }
}
