package userapp.brian.duran.userappapi.dto;

import lombok.Builder;
import userapp.brian.duran.userappapi.entity.User;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record SaveRefreshTokenCommand(
        User user,
        String rawRefreshToken,
        UUID jti,
        UUID familyId,
        UUID parentJti,
        ClientContext client,
        LocalDateTime expiresAt
) { }
