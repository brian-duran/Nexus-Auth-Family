package userapp.brian.duran.userappapi.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ActiveSessionResponse(

        UUID id,
        String ipAddress,
        String deviceInfo,
        LocalDateTime createdAt,
        LocalDateTime expiresAt
) { }
