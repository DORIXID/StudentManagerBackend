package dev.vortsu.mapper;

import dev.vortsu.dto.StudentDTO;
import dev.vortsu.dto.CreateStudentUserPasswordDTO;
import dev.vortsu.dto.UpdateStudentUserPasswordDTO;
import dev.vortsu.entity.StudentEntity;
import org.mapstruct.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.List;


@Mapper(componentModel = "spring", uses = {UserMapper.class})
public abstract class StudentMapper {

    public abstract StudentDTO toDto(StudentEntity student);

    public Page<StudentDTO> toDto(Page<StudentEntity> pagedStudentEntities) {
        if (pagedStudentEntities == null) {
            return null;
        }
        List<StudentEntity> studentEntities = pagedStudentEntities.getContent();
        List<StudentDTO> studentDTOS = new ArrayList<>();
        for (StudentEntity studentEntity : studentEntities) {
            StudentDTO dto = toDto(studentEntity);
            studentDTOS.add(dto);
        }
        return new PageImpl<>(studentDTOS, pagedStudentEntities.getPageable(), pagedStudentEntities.getTotalElements());
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "dto")
    public abstract StudentEntity toEntityFromUserStudent(CreateStudentUserPasswordDTO dto);

    @Mapping(target="id", ignore= true)
    @Mapping(target = "user", source = "dto")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void updateEntityFromUserStudent(UpdateStudentUserPasswordDTO dto, @MappingTarget StudentEntity student);
}
