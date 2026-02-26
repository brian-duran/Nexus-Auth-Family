package userapp.brian.duran.userappapi.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import userapp.brian.duran.userappapi.entity.Role;
import userapp.brian.duran.userappapi.enums.RoleEnum;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

  Optional<Role> findByName(RoleEnum name);
}
