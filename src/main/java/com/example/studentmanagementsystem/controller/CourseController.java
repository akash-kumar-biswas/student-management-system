package com.example.studentmanagementsystem.controller;

import com.example.studentmanagementsystem.entity.Course;
import com.example.studentmanagementsystem.repository.CourseRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseRepository repo;

    public CourseController(CourseRepository repo) {
        this.repo = repo;
    }
    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping
    public Course create(@RequestBody Course c) {
        return repo.save(c);
    }

    @GetMapping
    public List<Course> getAll() {
        return repo.findAll();
    }

    @DeleteMapping("/{id}")
    @org.springframework.security.access.prepost.PreAuthorize("hasRole('TEACHER')")
    public void delete(@PathVariable Long id) {

        Course course = repo.findById(id).orElseThrow();

        // detach from students first (removes rows from student_courses)
        if (course.getStudents() != null) {
            course.getStudents().forEach(s -> s.getCourses().remove(course));
        }

        repo.delete(course);
    }

}
