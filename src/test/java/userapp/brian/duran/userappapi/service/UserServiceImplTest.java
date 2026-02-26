package userapp.brian.duran.userappapi.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import userapp.brian.duran.userappapi.dto.CreateUserRequest;
import userapp.brian.duran.userappapi.dto.CreateUserResponse;
import userapp.brian.duran.userappapi.entity.User;
import userapp.brian.duran.userappapi.mapper.CreateUserMapper;
import userapp.brian.duran.userappapi.notification.event.UserCreatedEvent;
import userapp.brian.duran.userappapi.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserServiceImpl Unit Tests")
class UserServiceImplTest {

  @Mock private UserRepository userRepository;
  @Mock private PasswordEncoder passwordEncoder;
  @Mock private CreateUserMapper createUserMapper;
  @Mock private ApplicationEventPublisher eventPublisher;

  @InjectMocks private UserServiceImpl userService;

  @Nested
  @DisplayName("Registration Logic")
  class RegistrationTests {

    @Test
    @DisplayName("Should successfully register a new user")
    void shouldRegisterNewUser() {
      // Given
      User rawUser = User.builder().username("john").password("rawPass").build();
      User savedUser = User.builder().id(1L).username("john").password("encodedPass").build();

      when(passwordEncoder.encode("rawPass")).thenReturn("encodedPass");
      when(userRepository.save(rawUser)).thenReturn(savedUser);

      // When
      User result = userService.registerNewUser(rawUser);

      // Then
      assertThat(result).isNotNull();
      assertThat(result.getId()).isEqualTo(1L);
      assertThat(result.getPassword()).isEqualTo("encodedPass");
      verify(eventPublisher).publishEvent(any(UserCreatedEvent.class));
      verify(userRepository).save(rawUser);
    }
  }

  @Nested
  @DisplayName("Save Method with DTOs")
  class SaveDtoTests {

    @ParameterizedTest
    @ValueSource(strings = {"alice", "bob", "charlie"})
    @DisplayName("Should save user from request for multiple usernames")
    void shouldSaveUserFromRequest(String username) {
      // Given
      CreateUserRequest request =
          new CreateUserRequest(username, username + "@example.com", "password123", Set.of());
      User userEntity = User.builder().username(username).password("password123").build();
      User savedUser = User.builder().id(100L).username(username).password("encoded").build();
      CreateUserResponse expectedResponse =
          new CreateUserResponse(100L, username, username + "@example.com", Set.of());

      when(createUserMapper.toUserEntity(request)).thenReturn(userEntity);
      when(passwordEncoder.encode("password123")).thenReturn("encoded");
      when(userRepository.save(userEntity)).thenReturn(savedUser);
      when(createUserMapper.toCreateUserResponse(savedUser)).thenReturn(expectedResponse);

      // When
      CreateUserResponse response = userService.save(request);

      // Then
      assertThat(response).isNotNull();
      assertThat(response.username()).isEqualTo(username);
      verify(userRepository).save(userEntity);
      verify(eventPublisher).publishEvent(any(UserCreatedEvent.class));
    }
  }
}
