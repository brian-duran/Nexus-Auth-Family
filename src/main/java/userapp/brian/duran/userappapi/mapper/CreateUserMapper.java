package userapp.brian.duran.userappapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import userapp.brian.duran.userappapi.dto.CreateUserRequest;
import userapp.brian.duran.userappapi.dto.CreateUserResponse;
import userapp.brian.duran.userappapi.entity.Role;
import userapp.brian.duran.userappapi.entity.User;
import userapp.brian.duran.userappapi.service.RoleService;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {RoleService.class})
public interface CreateUserMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "password", ignore = true)
  @Mapping(target = "roles", qualifiedByName = "findRolesByNames")

  // --- Campos Base / Auditoría (Auto-generados por JPA/Hibernate) ---
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "lastModifiedBy", ignore = true)
  @Mapping(target = "publicId", ignore = true)

  // --- Relaciones y Estado Interno (Valores por defecto / Vacíos) ---
  @Mapping(target = "tokens", ignore = true)
  @Mapping(target = "security", ignore = true)
  User toUserEntity(CreateUserRequest request);

  @Named("rolesToStrings")
  default Set<String> rolesToStrings(Set<Role> roles) {

    return Optional.ofNullable(roles)
            .orElseGet(Collections::emptySet)
            .stream()
            .map(Role::getName)
            .map(Enum::name)
            .collect(Collectors.toSet());
  }

  @Mapping(target = "roles", source = "roles", qualifiedByName = "rolesToStrings")
  CreateUserResponse toCreateUserResponse(User user);
}
