package userapp.brian.duran.userappapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import userapp.brian.duran.userappapi.dto.ClientContext;
import userapp.brian.duran.userappapi.dto.SaveRefreshTokenCommand;
import userapp.brian.duran.userappapi.entity.Token;
import userapp.brian.duran.userappapi.enums.RevocationReasonEnum;
import userapp.brian.duran.userappapi.factory.TokenFactory;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenRotationService {

    private final TokenService tokenService;
    private final TokenFactory tokenFactory;

    @Transactional
    public void rotate(Token usedToken, String newRawRefresh, UUID newJti, ClientContext client) {

        log.info("Rotando token para la familia: {}", usedToken.getFamilyId());

        usedToken.revoke(RevocationReasonEnum.ROTATED);
        tokenService.save(usedToken);

        var refreshTokenCommand = SaveRefreshTokenCommand.builder()
                .user(usedToken.getUser())
                .rawRefreshToken(newRawRefresh)
                .jti(newJti)
                .familyId(usedToken.getFamilyId())
                .parentJti(usedToken.getJti())
                .client(client)
                .build();

        Token nextToken = tokenFactory.buildNew(refreshTokenCommand);
        tokenService.save(nextToken);
    }

    @Transactional
    public void handleReuse(Token compromisedToken) {
        log.warn("🚨 ¡ALERTA DE SEGURIDAD! Intento de reuso detectado en el token jti: {}", compromisedToken.getJti());

        // Si alguien usa un token revocado, matar a TODOS los tokens del usuario.
        // Opcional: podría buscar solo los de esa familia, pero revocar ALL es más seguro.
        tokenService.revokeAllUserTokens(compromisedToken.getUser(), RevocationReasonEnum.FRAUD_DETECTED);
    }
}
