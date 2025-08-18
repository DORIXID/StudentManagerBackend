package dev.vortsu.mapper;

import dev.vortsu.dto.PasswordDTO;
import dev.vortsu.entity.Password;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

    @Mapper(componentModel = "spring")
    public interface PasswordMapper {

        @Mapping(target = "password", expression = "java(passwordEncoder.encode(dto.getPassword()))")
        Password toEntity(PasswordDTO dto, @Context org.springframework.security.crypto.password.PasswordEncoder passwordEncoder);

        @Mapping(target = "password", ignore = true)
        PasswordDTO toDto(Password student);
    }
