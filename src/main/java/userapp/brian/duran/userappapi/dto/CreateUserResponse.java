package userapp.brian.duran.userappapi.dto;

import java.util.Set;

public record CreateUserResponse(Long id, String username, String email, Set<String> roles) {}
