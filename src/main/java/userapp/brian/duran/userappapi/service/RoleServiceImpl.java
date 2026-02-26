package userapp.brian.duran.userappapi.service;

import jakarta.transaction.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import userapp.brian.duran.userappapi.dto.CreateRoleRequest;
import userapp.brian.duran.userappapi.dto.CreateRoleResponse;
import userapp.brian.duran.userappapi.entity.Role;
import userapp.brian.duran.userappapi.enums.RoleEnum;
import userapp.brian.duran.userappapi.mapper.CreateRoleMapper;
import userapp.brian.duran.userappapi.repository.RoleRepository;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

  private final RoleRepository roleRepository;
  private final CreateRoleMapper createRoleMapper;

  @Override
  @Transactional
  public CreateRoleResponse save(CreateRoleRequest request) {

    Optional.of(request.name())
        .orElseThrow(() -> new IllegalArgumentException("Role " + request.name() + " not exists"));

    Role roleEntity = createRoleMapper.toRoleEntity(request);
    Role roleSaved = roleRepository.save(roleEntity);

    return createRoleMapper.toCreateRoleResponse(roleSaved);
  }

  @Override
  public Set<Role> findRolesByNames(Set<String> roles) {

    return Optional.ofNullable(roles)
        .map(r -> r.stream()
          .map(RoleEnum::toEnum)
          .map(this::findByName)
          .collect(Collectors.toSet())
        )
        .orElseGet(() -> Set.of(findByName(RoleEnum.USER)));
  }

  private Role findByName(RoleEnum roleEnum) {
    return roleRepository.findByName(roleEnum)
        .orElseThrow(() -> new IllegalArgumentException("Role not exists: " + roleEnum));
  }
}
