package dev.vortsu.mapper;


import dev.vortsu.dto.UserDTO;
import dev.vortsu.dto.createStudentUserPasswordDTO;
import dev.vortsu.dto.updateStudentUserPasswordDTO;
import dev.vortsu.entity.User;
import org.mapstruct.*;


@Mapper(componentModel = "spring", uses = {PasswordMapper.class})
public interface UserMapper {

        @Mapping(source = "password.id", target = "passwordId")
        UserDTO toDto(User student);

        @Mapping(target = "id", ignore = true)
        @Mapping(target = "enabled", expression = "java(true)")
        @Mapping(target = "role", expression = "java(dev.vortsu.entity.Role.STUDENT)")
        @Mapping(target = "password", source = "dto")
        User toEntityFromUserName(createStudentUserPasswordDTO dto);


        @Mapping(target = "id", ignore = true)
        @Mapping(target = "enabled", expression = "java(true)")
        @Mapping(target = "userName", expression = "java(dto.getUserName().isBlank() ? user.getUserName() : dto.getUserName())")
        @Mapping(target = "role", expression = "java(dev.vortsu.entity.Role.STUDENT)")
        @Mapping(target = "password", source = "dto")
        @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
        void updateEntityFromUserName(updateStudentUserPasswordDTO dto, @MappingTarget User user);
}
