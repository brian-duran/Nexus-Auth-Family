package userapp.brian.duran.userappapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import userapp.brian.duran.userappapi.annotation.UniqueEmail;
import userapp.brian.duran.userappapi.annotation.ValidRole;

import java.util.Set;

public record CreateUserRequest(

    @NotBlank(message = "The username is required")
    String username,

    @NotBlank(message = "The email is required")
    @Email(message = "Invalid email format")
    @UniqueEmail
    String email,

    @NotBlank(message = "The password is required")
    @Size(min = 6, message = "The password must be at least 6 characters")
    String password,

    Set<@ValidRole String> roles) {}
