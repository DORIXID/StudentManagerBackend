package dev.vortsu.controllers;

import dev.vortsu.dto.createStudentUserPasswordDTO;
import dev.vortsu.dto.updateStudentUserPasswordDTO;
import dev.vortsu.services.StudentService;

import dev.vortsu.dto.StudentResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


//http://localhost:4200/api/base/students?page=1&limit=10&sortBy=id

@Slf4j
@RestController
@RequestMapping("api/base")
public class BaseController {

    @Autowired
    private StudentService studentService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(value = "students", produces = MediaType.APPLICATION_JSON_VALUE)
    public void createStudent(@Validated @RequestBody createStudentUserPasswordDTO newStudent)  {
        studentService.createStudent(newStudent);
    }

    @PutMapping(value="students",  consumes = MediaType.APPLICATION_JSON_VALUE)
    public void editStudent(@Validated @RequestBody updateStudentUserPasswordDTO newStudent)  {
        studentService.editStudent(newStudent);
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
