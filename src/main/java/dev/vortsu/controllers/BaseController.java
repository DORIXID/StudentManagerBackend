package dev.vortsu.controllers;

import dev.vortsu.dto.CreateStudentUserPasswordDTO;
import dev.vortsu.dto.StudentDTO;
import dev.vortsu.dto.UpdateStudentUserPasswordDTO;
import dev.vortsu.entity.StudentEntity;
import dev.vortsu.services.StudentService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

//http://localhost:8080/api/base/students?page=1&limit=10&sortBy=id
//http://localhost:8080/api/base/students?page=1&limit=10&sortBy=name&searchedType=id&searchedValue=1

@Slf4j
@RestController
@RequestMapping("api/base")
public class BaseController {

    @Autowired
    private StudentService studentService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(value = "students", produces = MediaType.APPLICATION_JSON_VALUE)
    public void createStudent(@Validated @RequestBody CreateStudentUserPasswordDTO newStudent)  {
        studentService.createStudent(newStudent);
    }

    @PatchMapping(value="students", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void editStudent(@Validated @RequestBody UpdateStudentUserPasswordDTO newStudent, Authentication authentication)  {
        studentService.editStudent(newStudent, authentication);
    }

    @DeleteMapping(value = "students",consumes = MediaType.APPLICATION_JSON_VALUE)
    public void deleteStudent(@Validated @RequestBody StudentEntity student, Authentication authentication)  {
        studentService.deleteStudent(student.getId(), authentication);
    }


    //TODO: Возможно добавить эндпоинт для обновления пароля и прочего
    @GetMapping("students")
    public Page<StudentDTO> getAllStudentsts(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(required = false) String searchedType,
            @RequestParam(required = false) String searchedValue,
            Authentication authentication) {
        System.out.println("\n\n...запрос принят...\n\n");
        return  studentService.getAllStudents(
                page,
                limit,
                sortBy,
                searchedType,
                searchedValue,
                authentication);
    }
}
