package dev.vortsu.mapper;

import dev.vortsu.dto.CreateStudentUserPasswordDTO;
import dev.vortsu.dto.UpdateStudentUserPasswordDTO;
import dev.vortsu.entity.PasswordEntity;
import dev.vortsu.utils.PasswordEncoding;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class PasswordMapper {

    @Autowired
    PasswordEncoding encoder;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", expression = "java(encoder.encode(dto.getPassword()))")
    public abstract PasswordEntity toEntity(CreateStudentUserPasswordDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", expression = "java(encoder.encode(dto.getPassword()))")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void updateEntityFromPsssword(UpdateStudentUserPasswordDTO dto, @MappingTarget PasswordEntity password);
}
