package userapp.brian.duran.userappapi.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import userapp.brian.duran.userappapi.dto.AuthenticationRegisterRequest;
import userapp.brian.duran.userappapi.dto.AuthenticationRequest;
import userapp.brian.duran.userappapi.dto.AuthenticationResponse;
import userapp.brian.duran.userappapi.security.AuthenticationService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;
    private static final String CLIENT_TYPE_REQUESTS = "unknown";
    
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody @Valid AuthenticationRegisterRequest request, HttpServletRequest httpServletRequest) {

        String ipAddress = getClientIp(httpServletRequest);
        String deviceInfo = httpServletRequest.getHeader("User-Agent");

        return ResponseEntity.ok(service.register(request, ipAddress, deviceInfo));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody @Valid AuthenticationRequest request, HttpServletRequest httpServletRequest) {

        String ipAddress = getClientIp(httpServletRequest);
        String deviceInfo = httpServletRequest.getHeader("User-Agent");

        return ResponseEntity.ok(service.authenticate(request, ipAddress, deviceInfo));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(HttpServletRequest request) {

        return ResponseEntity.ok(service.refreshToken(request));
    }

    private String getClientIp(HttpServletRequest request) {

        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || CLIENT_TYPE_REQUESTS.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || CLIENT_TYPE_REQUESTS.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || CLIENT_TYPE_REQUESTS.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // En caso de múltiples proxies, la primera IP es la del cliente real
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}
