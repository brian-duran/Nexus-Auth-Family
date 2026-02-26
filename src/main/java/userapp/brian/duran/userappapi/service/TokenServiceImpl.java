package userapp.brian.duran.userappapi.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import userapp.brian.duran.userappapi.dto.ActiveSessionResponse;
import userapp.brian.duran.userappapi.entity.Token;
import userapp.brian.duran.userappapi.entity.User;
import userapp.brian.duran.userappapi.enums.RevocationReasonEnum;
import userapp.brian.duran.userappapi.mapper.TokenMapper;
import userapp.brian.duran.userappapi.repository.TokenRepository;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final TokenRepository tokenRepository;
    private final TokenMapper tokenMapper;

    @Override
    @Transactional
    public Token save(Token token) {
        return tokenRepository.save(token);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Token> findByJti(UUID jti) {
        return tokenRepository.findByJti(jti);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActiveSessionResponse> getActiveSessions(User user) {

        return tokenRepository.findAllValidTokensByUserId(user.getId())
                .stream()
                .map(tokenMapper::toSessionResponse)
                .toList();
    }

    @Override
    @Transactional
    public void revokeSession(User user, UUID tokenId) {
        Token token = tokenRepository.findById(tokenId)
                .orElseThrow(() -> new IllegalArgumentException("Sesión no encontrada"));

        if (!token.getUser().getId().equals(user.getId())) {
            throw new SecurityException("No tienes permiso para revocar esta sesión");
        }

        token.revoke(RevocationReasonEnum.LOGOUT);
        tokenRepository.save(token);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Token> findAllValidTokensByUserId(Long id) {
        return tokenRepository.findAllValidTokensByUserId(id);
    }

    @Override
    @Transactional
    public void revokeAllUserTokens(User user, RevocationReasonEnum reason) {
        var validTokens = tokenRepository.findAllValidTokensByUserId(user.getId());
        if (!validTokens.isEmpty()) {
            validTokens.forEach(token -> token.revoke(reason));
            tokenRepository.saveAll(validTokens);
        }
    }
}
