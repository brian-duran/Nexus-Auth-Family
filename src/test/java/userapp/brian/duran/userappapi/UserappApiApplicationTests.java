package userapp.brian.duran.userappapi;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Application Context Load Test")
class UserappApiApplicationTests {

  @Test
  @DisplayName("Should successfully load the Spring Boot application context")
  void contextLoads() {}
}
