package dev.vortsu.mapper;

import dev.vortsu.dto.StudentDTO;
import dev.vortsu.dto.CreateStudentUserPasswordDTO;
import dev.vortsu.dto.UpdateStudentUserPasswordDTO;
import dev.vortsu.entity.Student;
import org.mapstruct.*;


@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface StudentMapper {

    @Mapping(source = "user.id", target = "userId")
    StudentDTO toDto(Student student);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "dto")
    Student toEntityFromUserStudent(CreateStudentUserPasswordDTO dto);

    @Mapping(target="id", ignore= true)
    @Mapping(target = "user", source = "dto")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromUserStudent(UpdateStudentUserPasswordDTO dto, @MappingTarget Student student);
}
