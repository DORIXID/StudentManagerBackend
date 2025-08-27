package dev.vortsu.mapper;

import dev.vortsu.dto.createStudentUserPasswordDTO;
import dev.vortsu.dto.updateStudentUserPasswordDTO;
import dev.vortsu.entity.Password;
import dev.vortsu.utils.PasswordEncoding;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class PasswordMapper {

    @Autowired
    PasswordEncoding encoder;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", expression = "java(encoder.encode(dto.getPassword()))")
    public abstract Password toEntity(createStudentUserPasswordDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", expression = "java(dto.getPassword().isBlank() ? password.getPassword() : encoder.encode(dto.getPassword()))")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void updateEntityFromPsssword(updateStudentUserPasswordDTO dto, @MappingTarget Password password);
}
