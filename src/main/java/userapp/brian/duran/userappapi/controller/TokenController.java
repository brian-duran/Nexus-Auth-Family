package userapp.brian.duran.userappapi.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import userapp.brian.duran.userappapi.dto.ActiveSessionResponse;
import userapp.brian.duran.userappapi.enums.RevocationReasonEnum;
import userapp.brian.duran.userappapi.security.UserPrincipal;
import userapp.brian.duran.userappapi.service.TokenService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/sessions")
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService;

    @GetMapping
    public ResponseEntity<List<ActiveSessionResponse>> getActiveSessions(
            @AuthenticationPrincipal UserPrincipal currentUser
    ) {
        List<ActiveSessionResponse> response = tokenService.getActiveSessions(currentUser.getUser());
        return ResponseEntity.ok(response);
    }

    /**
     * Revoca una sesión específica por su ID (Cerrar sesión en otro dispositivo).
     */
    @DeleteMapping("/{tokenId}")
    public ResponseEntity<Void> revokeSession(
            @PathVariable UUID tokenId,
            @AuthenticationPrincipal UserPrincipal currentUser
    ) {
        tokenService.revokeSession(currentUser.getUser(), tokenId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Revoca absolutamente todas las sesiones del usuario (Cerrar sesión en todos lados).
     */
    @DeleteMapping("/all")
    public ResponseEntity<Void> revokeAllSessions(
            @AuthenticationPrincipal UserPrincipal currentUser
    ) {
        tokenService.revokeAllUserTokens(currentUser.getUser(), RevocationReasonEnum.LOGOUT);
        return ResponseEntity.noContent().build();
    }
}
