package userapp.brian.duran.userappapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import userapp.brian.duran.userappapi.dto.CreateRoleRequest;
import userapp.brian.duran.userappapi.dto.CreateRoleResponse;
import userapp.brian.duran.userappapi.entity.Role;
import userapp.brian.duran.userappapi.enums.RoleEnum;

@Mapper(componentModel = "spring")
public interface CreateRoleMapper {

  @Named("stringToRoleEnum")
  default RoleEnum mapStringToEnum(String name) {
    return RoleEnum.toEnum(name);
  }

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "publicId", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "lastModifiedBy", ignore = true)
  @Mapping(target = "name", source = "name", qualifiedByName = "stringToRoleEnum")
  Role toRoleEntity(CreateRoleRequest request);

  CreateRoleResponse toCreateRoleResponse(Role role);
}
