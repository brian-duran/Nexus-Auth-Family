package userapp.brian.duran.userappapi.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, @Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            // Si hay token Y no está autenticado -> procesarlo
            jwtService.extractTokenFromRequest(request)
                    .filter(this::isNotAuthenticated)
                    .ifPresent(jwt -> processToken(jwt, request));

        } catch (Exception e) {
            log.warn("Access Token rechazado (Request: {}): {}", request.getRequestURI(), e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private boolean isNotAuthenticated(String jwt) {
        return SecurityContextHolder.getContext().getAuthentication() == null;
    }

    private void processToken(String jwt, HttpServletRequest request) {
        /* Pipeline de Autenticación: "Extraer usuario -> Cargar detalles -> Validar token -> Autenticar" */
        Optional.ofNullable(jwtService.getUsername(jwt))
                .map(userDetailsService::loadUserByUsername)
                .filter(userDetails -> jwtService.isTokenValid(jwt, userDetails))
                .ifPresent(userDetails -> authenticateUser(userDetails, request));
    }

    private void authenticateUser(UserDetails userDetails, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }
}
