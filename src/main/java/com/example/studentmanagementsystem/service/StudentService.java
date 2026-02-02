package com.example.studentmanagementsystem.service;

import com.example.studentmanagementsystem.dto.StudentCreateRequest;
import com.example.studentmanagementsystem.dto.StudentUpdateMeRequest;
import com.example.studentmanagementsystem.entity.Course;
import com.example.studentmanagementsystem.entity.Department;
import com.example.studentmanagementsystem.entity.Student;
import com.example.studentmanagementsystem.repository.CourseRepository;
import com.example.studentmanagementsystem.repository.DepartmentRepository;
import com.example.studentmanagementsystem.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;
    private final CourseRepository courseRepository;

    // ✅ TEACHER creates student
    public Student create(StudentCreateRequest req) {
        Department dept = departmentRepository.findById(req.getDepartmentId())
                .orElseThrow(() -> new EntityNotFoundException("Department not found"));

        Set<Course> courses = new HashSet<>();
        if (req.getCourseIds() != null && !req.getCourseIds().isEmpty()) {
            courses.addAll(courseRepository.findAllById(req.getCourseIds()));
        }

        Student s = new Student();
        s.setUsername(req.getUsername().trim());              // ✅ important
        s.setName(req.getName().trim());
        s.setEmail(req.getEmail().trim().toLowerCase());
        s.setDepartment(dept);
        s.setCourses(courses);

        return studentRepository.save(s);
    }

    // ✅ TEACHER: view all students
    public List<Student> getAll() {
        return studentRepository.findAll();
    }

    // ✅ STUDENT: view own profile
    public Student me(String username) {
        java.util.Optional<Student> opt = studentRepository.findByUsername(username);
        return opt.orElseThrow(() -> new EntityNotFoundException("Student not found for username: " + username));
    }

    // ✅ STUDENT: update own profile (name/email only)
    public Student updateMe(String username, StudentUpdateMeRequest req) {
        Student s = me(username);
        s.setName(req.name().trim());
        s.setEmail(req.email().trim().toLowerCase());
        return studentRepository.save(s);
    }

    // ✅ TEACHER: update any student (name/email only)
    public Student updateAny(Long id, StudentUpdateMeRequest req) {
        Student s = studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student not found id: " + id));
        s.setName(req.name().trim());
        s.setEmail(req.email().trim().toLowerCase());
        return studentRepository.save(s);
    }

    // ✅ TEACHER deletes student
    public void delete(Long id) {
        studentRepository.deleteById(id);
    }
}
