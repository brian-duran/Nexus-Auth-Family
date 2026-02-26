package userapp.brian.duran.userappapi.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import userapp.brian.duran.userappapi.entity.Token;

@Repository
public interface TokenRepository extends JpaRepository<Token, UUID> {

    /**
     * Útil para: "Cerrar sesión en todos los dispositivos".
     * Es un proyecto de codigo abierto. A futuro ocultar las consultas SQL por seguridad.
     */
    @Query("""
      SELECT t FROM Token t INNER JOIN User u
      ON t.user.id = u.id
      WHERE u.id = :id AND (t.expired = false AND t.revoked = false)
      """)
    List<Token> findAllValidTokensByUserId(Long id);

    Optional<Token> findByJti(UUID jti);

    List<Token> findAllByFamilyIdOrderByCreatedAtDesc(UUID familyId);

    Optional<Token> findFirstByUser_IdOrderByCreatedAtDesc(Long userId);
}
