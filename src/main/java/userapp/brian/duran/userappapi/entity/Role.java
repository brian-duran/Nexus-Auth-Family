package userapp.brian.duran.userappapi.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import userapp.brian.duran.userappapi.enums.RoleEnum;

@Entity
@Table(name = "roles")
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Role extends BaseEntityLong {

  @Enumerated(EnumType.STRING)
  @Column(unique = true, nullable = false)
  private RoleEnum name;
}
