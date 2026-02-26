package userapp.brian.duran.userappapi.configuration;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import userapp.brian.duran.userappapi.security.JwtAuthenticationFilter;

@Configuration
public class SecurityConfig {

  @Value("${app.security.admin-password}")
  private String adminPassword;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean("memoryUserDetailsService")
  public UserDetailsService memoryUserDetailsService(PasswordEncoder passwordEncoder) {

    String miPassword = adminPassword;

    String passwordEncriptada = passwordEncoder.encode(miPassword);

    UserDetails usuarioPrueba =
        User.builder()
            .username("brian")
            .password(passwordEncriptada)
            .roles("ADMIN")
            .build();

    return new InMemoryUserDetailsManager(usuarioPrueba);
  }

  @Bean("memoryAuthProvider")
  public AuthenticationProvider memoryAuthProvider(
          @Qualifier("memoryUserDetailsService") UserDetailsService memoryService,
          PasswordEncoder passwordEncoder) {

    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(memoryService);
    provider.setPasswordEncoder(passwordEncoder);
    return provider;
  }

  @Bean("dbAuthProvider")
  public AuthenticationProvider dbAuthProvider(
          @Qualifier("userDetailsServiceImpl") UserDetailsService dbService,
          PasswordEncoder passwordEncoder
  ) {

    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(dbService);
    authProvider.setPasswordEncoder(passwordEncoder);
    return authProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(
          @Qualifier("dbAuthProvider") AuthenticationProvider dbAuthProvider,
          @Qualifier("memoryAuthProvider") AuthenticationProvider memoryAuthProvider) {

    return new ProviderManager(dbAuthProvider, memoryAuthProvider);
  }

  @Bean
  public SecurityFilterChain filterChain(
          HttpSecurity http,
          JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {

    return http
        // desactivado porque usaremos Tokens (Stateless), no Cookies.
        .csrf(AbstractHttpConfigurer::disable)

        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

        // Este repo será publico. En un futuro usar variables de entorno para ocultar las rutas de los endpoints
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers(HttpMethod.POST, "/api/users", "/api/roles").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .anyRequest().authenticated()
            )
        .httpBasic(withDefaults())
        .build();
  }
}
