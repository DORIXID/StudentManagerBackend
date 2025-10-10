package dev.vortsu.services;

import dev.vortsu.dto.CreateStudentUserPasswordDTO;
import dev.vortsu.dto.StudentDTO;
import dev.vortsu.dto.UpdateStudentUserPasswordDTO;
import dev.vortsu.dto.UserUpdateDTO;
import dev.vortsu.entity.StudentEntity;
import dev.vortsu.mapper.StudentMapper;
import dev.vortsu.mapper.UserMapper;
import dev.vortsu.repositories.StudentRepository;
import dev.vortsu.repositories.UserRepository;
import dev.vortsu.utils.CheckingForAccess;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@Transactional//TODO: сделать для каждого метода свой уровень проверки транзакции
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final StudentMapper studentMapper;
    private final CheckingForAccess checkingForAccess;
    private final UserMapper userMapper;
    private Sort sort;

    public void createStudent(CreateStudentUserPasswordDTO dto) {
        studentRepository.save(studentMapper.toEntityFromUserStudent(dto));
    }

    public void editStudent(StudentDTO dto, Authentication authentication){
        if (checkingForAccess.authenticationCheck(dto.getId(), authentication)) studentRepository.findById(dto.getId()).
                ifPresent(student -> studentMapper.toEntity(dto));
    }

    public void deleteStudent(Long id, Authentication authentication) {
        if(checkingForAccess.authenticationCheck(id, authentication)){
            studentRepository.deleteById(id);
        }
    }

    //Проверить как работает
    public void updateUser(UserUpdateDTO dto, Authentication authentication) {
        if (checkingForAccess.authenticationCheck(studentRepository.findById(dto.getId()).orElseThrow().getUser().getId(), authentication)) userRepository.findById(dto.getId()).
                ifPresent(user -> userMapper.updateEntityFromUserUpdateDTO(dto, user));
    }

    public Page<StudentDTO> getAllStudents(Integer page, Integer limit, String sortBy, String searchedType, String searchedValue, Authentication authentication) {
        sort = sortBy.startsWith("-") ? Sort.by(Sort.Direction.DESC, sortBy.substring(1)) : Sort.by(Sort.Direction.ASC, sortBy);
        System.out.println("\n\n\n   ... recievedPageIndex = " + page + " ...   \n\n\n");
        Page<StudentEntity> result = (searchedType == null) || (searchedValue == null) || searchedType.isBlank() || searchedValue.isBlank() ?
                studentRepository.findAll(PageRequest.of(page, limit, sort)) :
                studentRepository.findAll(searchedValue, PageRequest.of(page, limit, sort));
        System.out.println("\n\n\n   ... result = " + result + " ...   \n\n\n");
        return studentMapper.toDto(result);
    }

}
