package userapp.brian.duran.userappapi.security;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import userapp.brian.duran.userappapi.dto.*;
import userapp.brian.duran.userappapi.entity.Token;
import userapp.brian.duran.userappapi.entity.User;
import userapp.brian.duran.userappapi.factory.TokenFactory;
import userapp.brian.duran.userappapi.mapper.AuthenticationMapper;
import userapp.brian.duran.userappapi.service.RoleService;
import userapp.brian.duran.userappapi.service.TokenRotationService;
import userapp.brian.duran.userappapi.service.TokenService;
import userapp.brian.duran.userappapi.service.UserService;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final TokenService tokenService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final AuthenticationMapper authenticationMapper;
    private final RoleService roleService;
    private final TokenFactory tokenFactory;
    private final TokenRotationService tokenRotationService;

    private AuthenticationResponse generateTokensSequence(User user, String ipAddress, String deviceInfo) {

        UserPrincipal userPrincipal = new UserPrincipal(user);
        UUID refreshTokenId = UUID.randomUUID();
        ClientContext clientContext = new ClientContext(ipAddress, deviceInfo);

        String accessToken = jwtService.generateAccessToken(userPrincipal, Map.of("roles", userPrincipal.getRolesAsList()));
        String refreshToken = jwtService.generateRefreshToken(userPrincipal, refreshTokenId);

        var refreshTokenCommand = SaveRefreshTokenCommand.builder()
                .user(user)
                .rawRefreshToken(refreshToken)
                .jti(refreshTokenId)
                .client(clientContext)
                .build();

        Token tokenEntity = tokenFactory.buildNew(refreshTokenCommand);
        tokenService.save(tokenEntity);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public AuthenticationResponse register(AuthenticationRegisterRequest request, String ip, String device) {

        User user = authenticationMapper.toUserEntity(request);
        user.setRoles(roleService.findRolesByNames(request.roles()));

        User savedUser = userService.registerNewUser(user);

        return generateTokensSequence(savedUser, ip, device);
    }

    @Transactional
    public AuthenticationResponse authenticate(AuthenticationRequest request, String ipAddress, String deviceInfo) {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        User user = ((UserPrincipal) auth.getPrincipal()).getUser();

        return generateTokensSequence(user, ipAddress, deviceInfo);
    }

    @Transactional
    public AuthenticationResponse refreshToken(HttpServletRequest request) {

        String refreshToken = jwtService.extractTokenFromRequest(request)
                .orElseThrow(() -> new RuntimeException("Refresh token faltante o inválido"));

        String jti = jwtService.getTokenId(refreshToken);

        Token storedToken = tokenService.findByJti(UUID.fromString(jti))
                .orElseThrow(() -> new RuntimeException("Token no encontrado en BD"));

        if (storedToken.isRevoked()) {
            tokenRotationService.handleReuse(storedToken);
            throw new IllegalArgumentException("Intento de reuso de token. Sesión invalidada por seguridad.");
        }

        ClientContext client = new ClientContext(request.getRemoteAddr(), request.getHeader("User-Agent"));
        UUID newJti = UUID.randomUUID();
        UserPrincipal userPrincipal = new UserPrincipal(storedToken.getUser());

        String newRefreshString = jwtService.generateRefreshToken(userPrincipal, newJti);
        String newAccessString = jwtService.generateAccessToken(userPrincipal, Map.of("roles", userPrincipal.getRolesAsList()));

        tokenRotationService.rotate(storedToken, newRefreshString, newJti, client);

        return AuthenticationResponse.builder()
                .accessToken(newAccessString)
                .refreshToken(newRefreshString)
                .build();
    }
}
