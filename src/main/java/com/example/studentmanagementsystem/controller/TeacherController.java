package com.example.studentmanagementsystem.controller;

import com.example.studentmanagementsystem.dto.TeacherCreateRequest;
import com.example.studentmanagementsystem.entity.Teacher;
import com.example.studentmanagementsystem.service.TeacherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teachers")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @PostMapping
    public Teacher create(@Valid @RequestBody TeacherCreateRequest req) {
        return teacherService.create(req);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        teacherService.delete(id);
    }
}
