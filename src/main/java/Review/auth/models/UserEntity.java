package Review.auth.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "app_user")
public class UserEntity {
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


}
