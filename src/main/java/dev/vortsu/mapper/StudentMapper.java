package dev.vortsu.mapper;

import dev.vortsu.dto.StudentDTO;
import dev.vortsu.dto.createStudentUserPasswordDTO;
import dev.vortsu.dto.updateStudentUserPasswordDTO;
import dev.vortsu.entity.Student;
import org.mapstruct.*;


@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface StudentMapper {

    @Mapping(source = "user.id", target = "userId")
    StudentDTO toDto(Student student);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "dto")
    Student toEntityFromUserStudent(createStudentUserPasswordDTO dto);

    @Mapping(target="id", ignore= true)
    @Mapping(target = "user", source = "dto")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromUserStudent(updateStudentUserPasswordDTO dto, @MappingTarget Student student);
}
