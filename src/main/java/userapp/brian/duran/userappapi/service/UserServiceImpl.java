package userapp.brian.duran.userappapi.service;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import userapp.brian.duran.userappapi.dto.CreateUserRequest;
import userapp.brian.duran.userappapi.dto.CreateUserResponse;
import userapp.brian.duran.userappapi.entity.User;
import userapp.brian.duran.userappapi.mapper.CreateUserMapper;
import userapp.brian.duran.userappapi.notification.event.UserCreatedEvent;
import userapp.brian.duran.userappapi.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final CreateUserMapper createUserMapper;
  private final ApplicationEventPublisher eventPublisher;

  @Override
  @Transactional
  public User registerNewUser(User user) {

    user.setPassword(passwordEncoder.encode(user.getPassword()));
    User savedUser = userRepository.save(user);
    eventPublisher.publishEvent(new UserCreatedEvent(savedUser));
    return savedUser;
  }

  @Override
  @Transactional
  public CreateUserResponse save(CreateUserRequest request) {
    log.info("🧵 [Hilo: {}] 📧 Iniciando creación de usuario para: {}",
        Thread.currentThread().getName(),
        request.username());

    User user = createUserMapper.toUserEntity(request);
    User userSaved = this.registerNewUser(user);

    log.info("✅ [Hilo: {}] 📧 Creado exitosamente.", Thread.currentThread().getName());
    return createUserMapper.toCreateUserResponse(userSaved);
  }
}
