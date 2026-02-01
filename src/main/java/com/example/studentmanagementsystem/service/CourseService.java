package com.example.studentmanagementsystem.service;

import com.example.studentmanagementsystem.dto.CourseCreateRequest;
import com.example.studentmanagementsystem.entity.Course;
import com.example.studentmanagementsystem.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    public Course create(CourseCreateRequest req) {
        Course c = new Course();
        c.setCode(req.getCode().trim().toUpperCase());
        c.setTitle(req.getTitle().trim());
        return courseRepository.save(c);
    }

    public void delete(Long id) {
        courseRepository.deleteById(id);
    }
}
