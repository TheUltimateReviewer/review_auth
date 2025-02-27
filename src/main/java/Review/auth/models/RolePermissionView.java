package Review.auth.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name = "role_permissions_view")
@Getter
@Setter
public class RolePermissionView {
    @Id
    @Column(name = "permission_name")
    private String permissionName;

    @Column(name = "role_name")
    private String roleName;
}
