package Review.auth.respositories;

import Review.auth.models.PermissionEntity;
import Review.auth.models.RoleEntity;
import Review.auth.models.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    @Query("SELECT p.permission FROM RoleEntity r JOIN r.permissionList p WHERE r.roleEnum = :name")
    Optional<List<String>> findPermissionsByRole(@Param("name") RoleEnum name);
}
