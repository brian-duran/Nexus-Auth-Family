package userapp.brian.duran.userappapi.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${app.security.jwt.secret}")
    private String secretKey;

    @Value("${app.security.jwt.access-expiration}")
    private long accessTokenExpiration;

    @Getter
    @Value("${app.security.jwt.refresh-expiration}")
    private long refreshTokenExpiration;

    private String buildToken(UserDetails user, long expiration, Consumer<JwtBuilder> customizer) {
        JwtBuilder builder = Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey());

        customizer.accept(builder);

        return builder.compact();
    }

    public String generateAccessToken(UserDetails user, Map<String, Object> extraClaims) {
        return buildToken(user, accessTokenExpiration, builder -> builder.claims(extraClaims));
    }

    public String generateRefreshToken(UserDetails user, UUID tokenId) {
        return buildToken(user, refreshTokenExpiration, builder -> builder.id(tokenId.toString()));
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    private <T> T getClaim(String token, Function<Claims, T> resolver) {
        final Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return resolver.apply(claims);
    }

    public String getUsername(String token) {
        return getClaim(token, Claims::getSubject);
    }

    public String getTokenId(String token) {
        return getClaim(token, Claims::getId);
    }

    private boolean isTokenExpired(String token) {
        return getClaim(token, Claims::getExpiration).before(new Date());
    }

    public boolean isTokenValid(String token, UserDetails user) {
        final String username = getUsername(token);
        return (username.equals(user.getUsername())) && !isTokenExpired(token);
    }

    public Optional<String> extractTokenFromRequest(HttpServletRequest request) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return Optional.of(authHeader.substring(7));
        }
        return Optional.empty();
    }
}
