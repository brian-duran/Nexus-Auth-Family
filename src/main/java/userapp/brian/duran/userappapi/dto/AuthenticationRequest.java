package userapp.brian.duran.userappapi.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthenticationRequest(

        @NotBlank(message = "The username is required")
        String username,

        @NotBlank(message = "La contraseña es obligatoria")
        String password
) {
}
