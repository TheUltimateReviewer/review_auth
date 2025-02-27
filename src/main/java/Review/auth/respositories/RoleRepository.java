package Review.auth.respositories;

import Review.auth.models.RoleEntity;
import Review.auth.models.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import Review.auth.models.RolePermissionView;
import java.util.List;
import java.util.Optional;


@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    @Query("SELECT r.permissionName FROM RolePermissionView r WHERE r.roleName = :name")
    Optional<List<String>> findPermissionsByRole(@Param("name") String name);
}
