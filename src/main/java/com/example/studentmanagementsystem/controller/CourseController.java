package com.example.studentmanagementsystem.controller;

import com.example.studentmanagementsystem.dto.CourseCreateRequest;
import com.example.studentmanagementsystem.entity.Course;
import com.example.studentmanagementsystem.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @PostMapping
    public Course create(@Valid @RequestBody CourseCreateRequest req) {
        return courseService.create(req);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        courseService.delete(id);
    }
}
