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

    public abstract List<StudentDTO> toDto(List<StudentEntity> studentEntities);

    public Page<StudentDTO> toDto(Page<StudentEntity> page) {
        List<StudentDTO> newPage = toDto(page.getContent());
        return new PageImpl<>(newPage, page.getPageable(), page.getTotalElements());
    }

    @Mapping(target = "id", ignore = true)
    public abstract StudentEntity toEntity(StudentDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "dto")
    public abstract StudentEntity toEntityFromUserStudent(CreateStudentUserPasswordDTO dto);

    @Mapping(target="id", ignore= true)
    @Mapping(target = "user", source = "dto")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void updateEntityFromUserStudent(UpdateStudentUserPasswordDTO dto, @MappingTarget StudentEntity student);
}
