package com.example.studentmanagementsystem.controller;

import com.example.studentmanagementsystem.dto.StudentCreateRequest;
import com.example.studentmanagementsystem.dto.StudentUpdateMeRequest;
import com.example.studentmanagementsystem.entity.Student;
import com.example.studentmanagementsystem.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    // ✅ TEACHER creates student
    @PostMapping
    @PreAuthorize("hasRole('TEACHER')")
    public Student create(@Valid @RequestBody StudentCreateRequest req) {
        return studentService.create(req);
    }

    // ✅ TEACHER sees all students
    @GetMapping
    @PreAuthorize("hasRole('TEACHER')")
    public List<Student> getAll() {
        return studentService.getAll();
    }

    // ✅ STUDENT sees only own profile
    @GetMapping("/me")
    @PreAuthorize("hasRole('STUDENT')")
    public Student me(Authentication auth) {
        return studentService.me(auth.getName());
    }

    // ✅ STUDENT updates own profile
    @PutMapping("/me")
    @PreAuthorize("hasRole('STUDENT')")
    public Student updateMe(Authentication auth, @Valid @RequestBody StudentUpdateMeRequest req) {
        return studentService.updateMe(auth.getName(), req);
    }

    // ✅ TEACHER updates any student
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public Student updateAny(@PathVariable Long id, @Valid @RequestBody StudentUpdateMeRequest req) {
        return studentService.updateAny(id, req);
    }

    // ✅ TEACHER deletes student
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public void delete(@PathVariable Long id) {
        studentService.delete(id);
    }
}
