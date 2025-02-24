package Review.auth.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "app_user")
public class UserEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy =  GenerationType.AUTO)
    private Long id;

    @Getter @Setter
    @Column(unique = true, nullable = false, name = "username")
    private String username;

    @Column(nullable = false, name = "password")
    private String password;


    @Column(name = "is_enabled")
    private boolean isEnabled;

    @Column(name = "account_non_expired")
    private boolean accountNonExpired;


    @Column(name ="account_non_locked")
    private boolean accountNonLocked;

    @Column(name = "credentials_non_expired")
    private boolean credentialsNonExpired;

    @JoinTable(name = "user_roles",joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<RoleEntity> roles = new HashSet<>();


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleEnum().name()))
                .collect(Collectors.toSet());
    }
}
