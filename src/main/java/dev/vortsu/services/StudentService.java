package dev.vortsu.services;

import dev.vortsu.dto.StudentDTO;
import dev.vortsu.dto.StudentResponse;
import dev.vortsu.entity.Password;
import dev.vortsu.entity.Role;
import dev.vortsu.entity.Student;
import dev.vortsu.entity.User;
import dev.vortsu.mapper.StudentMapper;
import dev.vortsu.repositories.PasswordRepository;
import dev.vortsu.repositories.StudentRepository;
import dev.vortsu.repositories.UserRepository;
import dev.vortsu.utils.PasswordEncoding;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
// сохраняет изменения в бд, вызванные методом, если при завершении он не выкинул исключение
@Transactional
public class StudentService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final StudentMapper studentMapper;
    private final PasswordEncoding encoder;
    private final PasswordRepository passwordRepository;

    public StudentDTO createStudent(@Valid StudentDTO newStudent, String nikName, String userPassword) {

        String userName = nikName;
        // дополнительная проверка
        if (userRepository.existsByUserName(userName)) {
            throw new IllegalArgumentException("UserName уже занят: " + userName);
        }

        // Пароль
        Password password = new Password();
        password.setPassword(encoder.encode(userPassword));

        // Пользователь
        User user = new User();
        user.setUserName(userName);
        user.setRole(Role.STUDENT);
        user.setEnabled(true);
        user.setPassword(password);

        user = userRepository.save(user);

        // Студент
        newStudent.setId(null);
        Student student = studentMapper.toEntity(newStudent);
        student.setUser(user);
        student = studentRepository.save(student);

        // DTOшку в ответ
        StudentDTO out = studentMapper.toDto(student);
        out.setUserId(user.getId());
        return out;
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    public StudentDTO changeStudent(@Valid Long id, StudentDTO studentDto, Authentication authentication, String newUserName, String newPassword) {
        String userName = authentication.getName();
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new IllegalArgumentException("User with name: " + userName + " was not found"));
        boolean isAdminOrTeacher = authentication.getAuthorities().stream() .map(auth -> auth.getAuthority()) .anyMatch(role -> role.equals("ADMIN") || role.equals("TEACHER"));
        Student existing = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student with id: " + id + " was not found"));

        if (isAdminOrTeacher || existing.getUser().getId().equals(user.getId())){
            if (!(newPassword == null || newPassword.isBlank())){
                Password password = passwordRepository.findById(user.getPassword().getId()).orElseThrow(() -> new IllegalArgumentException("Password with id: " + user.getPassword().getId() + " was not found"));
                password.setPassword(encoder.encode(newPassword));
                user.setPassword(password);
            }
            if (!(newUserName == null || newUserName.isBlank())){
                user.setUserName(newUserName);
            }
            existing.setName(studentDto.getName());
            existing.setSurname(studentDto.getSurname());
            existing.setAge(studentDto.getAge());
            studentRepository.save(existing);

            return studentMapper.toDto(existing);
        } else {
            throw new AccessDeniedException("You are not allowed to change this student");
        }
    }

    public Student getCurrentStudent(Principal principal) {
        User user = userRepository.findByUserName(principal.getName()).
                orElseThrow(() -> new IllegalArgumentException("User with name: " + principal.getName() + " was not found"));
        return studentRepository.findByUser(user).
                orElseThrow(() -> new IllegalArgumentException("User with name: " + principal.getName() + " was not found"));
    }

    public StudentResponse getAllStudents(
            Integer page,
            Integer limit,
            String sortBy,
            String searchedType,
            String searchedValue,
            Authentication authentication) {

        String userName = authentication.getName();
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new IllegalArgumentException("User with name: " + userName + " was not found"));
        boolean isAdminOrTeacher = authentication.getAuthorities().stream() .map(auth -> auth.getAuthority()) .anyMatch(role -> role.equals("ADMIN") || role.equals("TEACHER"));


        Sort.Direction direction = sortBy.startsWith("-") ? Sort.Direction.DESC : Sort.Direction.ASC;
        String sortField = sortBy.startsWith("-") ? sortBy.substring(1) : sortBy;

        if (isAdminOrTeacher) {
            Pageable pageable = PageRequest.of(page, limit, Sort.by(direction, sortField));
            Page<Student> pageResult;

            if (searchedType == null || searchedValue == null || searchedValue.isBlank()) {
                pageResult = studentRepository.findAll(pageable);
            } else {
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
            return new StudentResponse(
                    studentsDtoList,
                    pageResult.getTotalElements(),
                    pageResult.getTotalPages(),
                    pageResult.getNumber(),
                    pageResult.getSize()
            );
        } else {
            Student studentOfUser = studentRepository.findByUser(user).
                    orElseThrow(() -> new EntityNotFoundException("Student with name: " + userName + " was not found"));
            StudentDTO DtoStudentOfUser = studentMapper.toDto(studentOfUser);
            return new StudentResponse(
                    List.of(DtoStudentOfUser),
                    1L,
                    1,
                    0,
                    1
            );
        }
    }

}
