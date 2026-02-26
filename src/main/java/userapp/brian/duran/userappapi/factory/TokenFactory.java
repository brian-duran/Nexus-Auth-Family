package userapp.brian.duran.userappapi.factory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import userapp.brian.duran.userappapi.dto.SaveRefreshTokenCommand;
import userapp.brian.duran.userappapi.entity.ClientInfo;
import userapp.brian.duran.userappapi.entity.Token;
import userapp.brian.duran.userappapi.security.JwtService;
import userapp.brian.duran.userappapi.service.HashingService;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TokenFactory {

    private final HashingService hashingService;
    private final JwtService jwtService;

    public Token buildNew(SaveRefreshTokenCommand cmd) {

        UUID family = cmd.familyId() != null ? cmd.familyId() : UUID.randomUUID();

        LocalDateTime exp = cmd.expiresAt() != null
                ? cmd.expiresAt()
                : LocalDateTime.now().plusNanos(jwtService.getRefreshTokenExpiration()); //.plusMillis!!

        return Token.builder()
                .jti(cmd.jti())
                .familyId(family)
                .parentJti(cmd.parentJti())
                .tokenHash(hashingService.hash(cmd.rawRefreshToken()))
                .expiresAt(exp)
                .clientInfo(new ClientInfo(cmd.client().ipAddress(), cmd.client().deviceInfo()))
                .user(cmd.user())
                .build();
    }
}
