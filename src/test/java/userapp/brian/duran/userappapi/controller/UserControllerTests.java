package userapp.brian.duran.userappapi.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import userapp.brian.duran.userappapi.configuration.SecurityConfig;
import userapp.brian.duran.userappapi.dto.CreateUserRequest;
import userapp.brian.duran.userappapi.dto.CreateUserResponse;
import userapp.brian.duran.userappapi.repository.UserRepository;
import userapp.brian.duran.userappapi.security.JwtAuthenticationFilter;
import userapp.brian.duran.userappapi.security.JwtService;
import userapp.brian.duran.userappapi.service.UserService;

@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
@DisplayName("UserController Integration Tests")
class UserControllerTests {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private UserService userService;

  @MockitoBean private UserRepository userRepository; // Required by UniqueEmailValidator

  @MockitoBean private JwtService jwtService; // Required by JwtAuthenticationFilter

  @MockitoBean(name = "userDetailsServiceImpl")
  private org.springframework.security.core.userdetails.UserDetailsService userDetailsService;

  @MockitoBean(name = "memoryUserDetailsService")
  private org.springframework.security.core.userdetails.UserDetailsService memoryUserDetailsService;

  @Autowired private ObjectMapper objectMapper;

  @Nested
  @DisplayName("POST /api/users")
  class CreateUserTests {

    @Test
    @DisplayName("Should return 201 Created when request is valid")
    void shouldCreateUser() throws Exception {
      // Given
      CreateUserRequest request =
          new CreateUserRequest("brian", "brian@example.com", "password123", Set.of());
      CreateUserResponse response =
          new CreateUserResponse(1L, "brian", "brian@example.com", Set.of());

      when(userService.save(any(CreateUserRequest.class))).thenReturn(response);

      // When & Then
      mockMvc
          .perform(
              post("/api/users")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isCreated())
          .andExpect(header().exists("Location"))
          .andExpect(jsonPath("$.username").value("brian"))
          .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("Should return 400 Bad Request when validation fails")
    void shouldReturnBadRequestWhenValidationFails() throws Exception {
      // Given (empty username and invalid email)
      CreateUserRequest invalidRequest =
          new CreateUserRequest("", "invalid-email", "123", Set.of());

      // When & Then
      mockMvc
          .perform(
              post("/api/users")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(invalidRequest)))
          .andExpect(status().isBadRequest());
    }
  }
}
