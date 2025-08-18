package dev.vortsu.mapper;

import dev.vortsu.dto.StudentDTO;
import dev.vortsu.entity.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import dev.vortsu.entity.User;
import dev.vortsu.dto.UserDTO;


@Mapper(componentModel = "spring")
public interface StudentMapper {

    @Mapping(source = "user.id", target = "userId")
    StudentDTO toDto(Student student);

    @Mapping(source = "userId", target = "user.id")
    Student toEntity(StudentDTO dto);
}
