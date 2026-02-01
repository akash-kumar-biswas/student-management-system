package com.example.studentmanagementsystem.service;

import com.example.studentmanagementsystem.dto.StudentCreateRequest;
import com.example.studentmanagementsystem.entity.Course;
import com.example.studentmanagementsystem.entity.Department;
import com.example.studentmanagementsystem.entity.Student;
import com.example.studentmanagementsystem.repository.CourseRepository;
import com.example.studentmanagementsystem.repository.DepartmentRepository;
import com.example.studentmanagementsystem.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;
    private final CourseRepository courseRepository;

    public Student create(StudentCreateRequest req) {
        Department dept = departmentRepository.findById(req.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        Student s = new Student();
        s.setName(req.getName().trim());
        s.setEmail(req.getEmail().trim().toLowerCase());
        s.setDepartment(dept);

        if (req.getCourseIds() != null && !req.getCourseIds().isEmpty()) {
            s.setCourses(new HashSet<Course>(courseRepository.findAllById(req.getCourseIds())));
        }
        return studentRepository.save(s);
    }

    public void delete(Long id) {
        studentRepository.deleteById(id);
    }
}
