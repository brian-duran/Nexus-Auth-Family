package userapp.brian.duran.userappapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import userapp.brian.duran.userappapi.dto.ActiveSessionResponse;
import userapp.brian.duran.userappapi.entity.Token;

@Mapper(componentModel = "spring")
public interface TokenMapper {

    @Mapping(target = "ipAddress", source = "clientInfo.ipAddress")
    @Mapping(target = "deviceInfo", source = "clientInfo.deviceInfo")
    ActiveSessionResponse toSessionResponse(Token token);
}
