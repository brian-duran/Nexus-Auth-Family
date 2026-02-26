package userapp.brian.duran.userappapi.dto;

import jakarta.validation.constraints.NotBlank;
import userapp.brian.duran.userappapi.annotation.ValidRole;

public record CreateRoleRequest(@NotBlank(message = "The username is required") @ValidRole String name) {}
