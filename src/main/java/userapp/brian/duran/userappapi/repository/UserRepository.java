package userapp.brian.duran.userappapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import userapp.brian.duran.userappapi.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  @EntityGraph(attributePaths = "roles")
  @Query("SELECT u FROM User u WHERE u.username = :username OR u.email = :username")
  Optional<User> findByUsernameOREmail(String username);

  boolean existsByEmail(String email);
}
