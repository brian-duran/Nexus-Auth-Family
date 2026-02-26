package userapp.brian.duran.userappapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ClientInfo {

  @Column(name = "ip_address")
  private String ipAddress;

  @Column(name = "device_info")
  private String deviceInfo;
}
