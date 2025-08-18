package dev.vortsu.mapper;


import dev.vortsu.dto.UserDTO;
import dev.vortsu.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

        @Mapping(source = "password.id", target = "passwordId")
        UserDTO toDto(User student);

        @Mapping(source = "passwordId", target = "password.id")
        User toEntity(UserDTO dto);
}
