package dev.vortsu.mapper;


import dev.vortsu.dto.UserDTO;
import dev.vortsu.dto.CreateStudentUserPasswordDTO;
import dev.vortsu.dto.UpdateStudentUserPasswordDTO;
import dev.vortsu.entity.User;
import org.mapstruct.*;

//TODO: не использовать camel case
@Mapper(componentModel = "spring", uses = {PasswordMapper.class})
public interface UserMapper {

        @Mapping(target = "id", ignore = true)
        @Mapping(target = "enabled", expression = "java(true)")
        @Mapping(target = "role", expression = "java(dev.vortsu.entity.Role.STUDENT)")
        @Mapping(target = "password", source = "dto")
        User toEntityFromUserName(CreateStudentUserPasswordDTO dto);


        @Mapping(target = "id", ignore = true)
        @Mapping(target = "enabled", expression = "java(true)")
        @Mapping(target = "userName", expression = "java(dto.getUserName().isBlank() ? user.getUserName() : dto.getUserName())")
        @Mapping(target = "role", expression = "java(dev.vortsu.entity.Role.STUDENT)")
        @Mapping(target = "password", source = "dto")
        @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
        void updateEntityFromUserName(UpdateStudentUserPasswordDTO dto, @MappingTarget User user);
}
