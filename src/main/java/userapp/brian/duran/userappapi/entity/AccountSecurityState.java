package userapp.brian.duran.userappapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;

import lombok.*;

@Embeddable
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AccountSecurityState {

  @Builder.Default
  @Column(nullable = false)
  private boolean enabled = true;

  @Builder.Default
  @Column(name = "account_non_locked", nullable = false)
  private boolean accountNonLocked = true;

  @Builder.Default
  @Column(name = "account_non_expired", nullable = false)
  private boolean accountNonExpired = true;

  @Builder.Default
  @Column(name = "credentials_non_expired", nullable = false)
  private boolean credentialsNonExpired = true;

  @Builder.Default
  @Column(name = "failed_login_attempts")
  private int failedLoginAttempts = 0;

  @Column(name = "lock_time")
  private LocalDateTime lockTime;

  @Column(name = "last_login")
  private LocalDateTime lastLogin;

  public void registerFailedAttempt(int maxAttempts) {
    this.failedLoginAttempts++;
    if (this.failedLoginAttempts >= maxAttempts) {
      this.accountNonLocked = false;
      this.lockTime = LocalDateTime.now();
    }
  }

  public void resetAttemptsOnSuccess() {
    this.failedLoginAttempts = 0;
    this.accountNonLocked = true;
    this.lockTime = null;
    this.lastLogin = LocalDateTime.now();
  }
}
