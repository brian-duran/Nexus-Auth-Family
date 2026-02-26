package userapp.brian.duran.userappapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import userapp.brian.duran.userappapi.dto.AuthenticationRegisterRequest;
import userapp.brian.duran.userappapi.entity.User;

@Mapper(componentModel = "spring")
public interface AuthenticationMapper {

    // --- Mapeos Principales ---
    @Mapping(target = "roles", ignore = true)

    // --- Campos Ignorados (Gestión interna o automática) ---
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "publicId", ignore = true)
    @Mapping(target = "tokens", ignore = true)

    // --- Auditoría JPA (Se llenan solos) ---
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)

    @Mapping(target = "security", ignore = true)
    User toUserEntity(AuthenticationRegisterRequest request);
}
