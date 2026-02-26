package userapp.brian.duran.userappapi.service;

import userapp.brian.duran.userappapi.dto.ActiveSessionResponse;
import userapp.brian.duran.userappapi.entity.Token;
import userapp.brian.duran.userappapi.entity.User;
import userapp.brian.duran.userappapi.enums.RevocationReasonEnum;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TokenService {

    Token save(Token token);
    Optional<Token> findByJti(UUID jti);

    List<Token> findAllValidTokensByUserId(Long id); // Eliminar ??
    List<ActiveSessionResponse> getActiveSessions(User user);
    void revokeSession(User user, UUID tokenId);
    void revokeAllUserTokens(User user, RevocationReasonEnum reason);

}
