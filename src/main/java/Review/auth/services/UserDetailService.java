package Review.auth.services;

import Review.auth.models.UserEntity;
import Review.auth.respositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        UserEntity userEntity = this.userRepository.findUserEntityByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException("usuario inexistente"));

        //mapear el granted authorities con el UserEntity, que en ese particular caso tiene los mismos valores aunque tengo entendido que se puede hacer personalizado
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        //convertir los roles a un simple granted auth
        userEntity.getRoles().forEach(
                role -> authorities.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRoleEnum().name())))
                );

        userEntity.getRoles().stream()
                .flatMap(role ->role.getPermissionList().stream())
                .forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission.getPermission())));

        return new User(userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.isEnabled(),
                userEntity.isAccountNonExpired(),
                userEntity.isCredentialsNonExpired(),
                userEntity.isAccountNonLocked(),
                authorities
        );


    }



}
