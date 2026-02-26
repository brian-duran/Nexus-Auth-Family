package userapp.brian.duran.userappapi.service;

import org.mapstruct.Named;
import userapp.brian.duran.userappapi.dto.CreateRoleRequest;
import userapp.brian.duran.userappapi.dto.CreateRoleResponse;
import userapp.brian.duran.userappapi.entity.Role;
import java.util.Set;

public interface RoleService {
  CreateRoleResponse save(CreateRoleRequest request);

  @Named("findRolesByNames")
  Set<Role> findRolesByNames(Set<String> roles);
}
