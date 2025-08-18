package dev.vortsu.controllers;


import java.security.Principal;


import ch.qos.logback.classic.Logger;
import dev.vortsu.dto.StudentDTO;
import dev.vortsu.entity.Student;
import dev.vortsu.repositories.UserRepository;
import dev.vortsu.services.StudentService;
import jakarta.validation.Valid;

import dev.vortsu.dto.StudentResponse;
import dev.vortsu.repositories.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


//http://localhost:4200/api/base/students?page=1&limit=10&sortBy=id

@Slf4j
@RestController
@RequestMapping("api/base")
public class BaseController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentService studentService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(value = "students", produces = MediaType.APPLICATION_JSON_VALUE)
    public StudentDTO createStudent(@Valid @RequestBody StudentDTO newStudent,
                                    @RequestParam String password,
                                    @RequestParam String userName)  {
        return studentService.createStudent(newStudent, userName, password);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping(value = "students/{id}")
    public void deleteStudent(@PathVariable("id") Long id) {
        studentService.deleteStudent(id);
    }

    @PutMapping(value = "students/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public StudentDTO changeStudent(@PathVariable Long id, @RequestBody StudentDTO student, Authentication authentication, @RequestParam String password, @RequestParam String userName) {
        log.info("PUT /students/{} called by {}", id, authentication != null ? authentication.getName() : "anonymous");
        return studentService.changeStudent(id, student, authentication, userName, password);
    }

    @PreAuthorize("hasAuthority('STUDENT')")
    @GetMapping("students/me")
    public Student getCurrentStudent(Principal principal) {
        return studentService.getCurrentStudent(principal);
    }

    @GetMapping("students")
    public StudentResponse getAllStudents(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(required = false) String searchedType,
            @RequestParam(required = false) String searchedValue,
            Authentication authentication) {

        return studentService.getAllStudents(
                page,
                limit,
                sortBy,
                searchedType,
                searchedValue,
                authentication);
    }


}
