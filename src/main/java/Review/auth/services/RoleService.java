package Review.auth.services;

import Review.auth.models.RoleEnum;
import Review.auth.respositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;
    public Set<String> getPermissions(List<String> role) {


        return role.stream().flatMap(particularRole -> {
            String roleWithoutPrefix = particularRole.replace("ROLE_", "");
             return this.roleRepository
                    .findPermissionsByRole(RoleEnum.valueOf(roleWithoutPrefix))
                    .orElseThrow(() -> new NoSuchElementException("no hay role"))
                    .stream();
        }).collect(Collectors.toSet());




    }

}
