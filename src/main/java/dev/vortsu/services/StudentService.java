package dev.vortsu.services;

import dev.vortsu.dto.CreateStudentUserPasswordDTO;
import dev.vortsu.dto.StudentDTO;
import dev.vortsu.dto.StudentResponse;
import dev.vortsu.dto.UpdateStudentUserPasswordDTO;
import dev.vortsu.entity.Student;
import dev.vortsu.entity.User;
import dev.vortsu.mapper.StudentMapper;
import dev.vortsu.repositories.StudentRepository;
import dev.vortsu.repositories.UserRepository;
import dev.vortsu.utils.ChekingForAccess;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional//TODO: сделать для каждого метода свой уровень проверки транзакции
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final StudentMapper studentMapper;
    private final ChekingForAccess chekingForAccess;

    public void createStudent(CreateStudentUserPasswordDTO dto) {
        studentRepository.save(studentMapper.toEntityFromUserStudent(dto));
    }

    //TODO:кастомная валидация сделать
    public void editStudent(UpdateStudentUserPasswordDTO dto, Authentication authentication) {
        if(chekingForAccess.authenticationCheck(dto.getId(), authentication)) {
            if (!dto.getUserName().isBlank() && dto.getUserName().length() < 3) {
                throw new IllegalArgumentException("Имя пользователя должно быть не менее 3 символов");
            }
            if (!dto.getPassword().isBlank() && dto.getPassword().length() < 6) {
                throw new IllegalArgumentException("Пароль должен быть не менее 6 символов");
            }
            Optional<Student> student = studentRepository.findById(dto.getId());
            //TODO: потичать как с optional работать
            if (student.isPresent()) {
                Student newStudent = student.get();
                studentMapper.updateEntityFromUserStudent(dto, newStudent);
                studentRepository.save(newStudent);
            } else {
                throw new EntityNotFoundException("Student with id: " + dto.getId() + " was not found");
            }
        }
    }

    public void deleteStudent(Long id, Authentication authentication) {
        if(chekingForAccess.authenticationCheck(id, authentication)){
            studentRepository.deleteById(id);
        }
    }

    public StudentResponse getAllStudents(Integer page, Integer limit, String sortBy, String searchedType, String searchedValue, Authentication authentication) {


        //TODO: Principal, что в нем лежит, документацию спринга
        String userName = authentication.getName();
        User user = userRepository.findByUserName(userName).orElseThrow(() -> new IllegalArgumentException("User with name: " + userName + " was not found"));
        boolean isAdminOrTeacher = authentication.getAuthorities().stream()
                .map(auth -> auth.getAuthority())
                .anyMatch(role -> role.equals("ADMIN") || role.equals("TEACHER"));


        Sort.Direction direction = sortBy.startsWith("-") ? Sort.Direction.DESC : Sort.Direction.ASC;
        String sortField = sortBy.startsWith("-") ? sortBy.substring(1) : sortBy;

        if (isAdminOrTeacher) {
            Pageable pageable = PageRequest.of(page, limit, Sort.by(direction, sortField));
            Page<Student> pageResult;

            if (searchedType == null || searchedValue == null || searchedValue.isBlank()) {
                pageResult = studentRepository.findAll(pageable);
            } else {
                //TODO: сделать в один запрос - копипаста
                switch (searchedType) {
                    case "name":
                        pageResult = studentRepository.findByNameContainingIgnoreCase(searchedValue, pageable);
                        break;
                    case "id":
                        Optional<Student> studentOpt = studentRepository.findById(Long.parseLong(searchedValue));
                        List<Student> resultList = studentOpt.map(List::of).orElse(List.of());
                        pageResult = new PageImpl<>(resultList, pageable, resultList.size());
                        break;
                    case "age":
                        pageResult = studentRepository.findByAge(Integer.parseInt(searchedValue), pageable);
                        break;
                    case "surname":
                        pageResult = studentRepository.findBySurnameContainingIgnoreCase(searchedValue, pageable);
                        break;
                    default:
                        pageResult = Page.empty();
                }
            }
            List<StudentDTO> studentsDtoList = new ArrayList<>();
            for (Student student : pageResult) {
                studentsDtoList.add(studentMapper.toDto(student));
            }
            //TODO: обернуть в 1 сущность и так передавать, а то на 10 строчке и 14(15?) по дэбильному сделано, вроде как он говорил, что маппер сам это делать должен
            //TODO: Маппер сам должен возвращать StudentResponse
            return new StudentResponse(studentsDtoList, pageResult.getTotalElements(), pageResult.getTotalPages(), pageResult.getNumber(), pageResult.getSize());
        } else {
            Student studentOfUser = studentRepository.findByUser(user).orElseThrow(() -> new EntityNotFoundException("Student with name: " + userName + " was not found"));
            StudentDTO DtoStudentOfUser = studentMapper.toDto(studentOfUser);
            return new StudentResponse(List.of(DtoStudentOfUser), 1L, 1, 0, 1);
        }
    }

}
