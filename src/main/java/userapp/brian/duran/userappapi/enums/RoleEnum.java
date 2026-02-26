package userapp.brian.duran.userappapi.enums;

import java.util.Arrays;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum RoleEnum {
  ADMIN("Super Usuario"),
  MANAGMENT("Administrador"),
  USER("Usuario"),
  GUEST("Invitado");

  private final String description;

  public static boolean exist(String name) {
    return Arrays.stream(RoleEnum.values())
        .anyMatch(roleEnum -> roleEnum.name().equalsIgnoreCase(name));
  }

  public static RoleEnum toEnum(String name) {
    return Arrays.stream(RoleEnum.values())
            .filter(role -> role.name().equalsIgnoreCase(name))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Role not fund: " + name));
  }
}
