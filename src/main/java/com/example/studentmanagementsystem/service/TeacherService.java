package com.example.studentmanagementsystem.service;

import com.example.studentmanagementsystem.dto.TeacherCreateRequest;
import com.example.studentmanagementsystem.entity.Department;
import com.example.studentmanagementsystem.entity.Teacher;
import com.example.studentmanagementsystem.repository.DepartmentRepository;
import com.example.studentmanagementsystem.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final DepartmentRepository departmentRepository;

    public Teacher create(TeacherCreateRequest req) {
        Department dept = departmentRepository.findById(req.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        Teacher t = new Teacher();
        t.setName(req.getName().trim());
        t.setEmail(req.getEmail().trim().toLowerCase());
        t.setDepartment(dept);

        return teacherRepository.save(t);
    }

    public void delete(Long id) {
        teacherRepository.deleteById(id);
    }
}
