package com.example.studentmanagementsystem.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.studentmanagementsystem.entity.Department;
import com.example.studentmanagementsystem.entity.Teacher;
import com.example.studentmanagementsystem.repository.DepartmentRepository;
import com.example.studentmanagementsystem.repository.TeacherRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final TeacherRepository teacherRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Create default teacher if not exists
        if (teacherRepository.findByEmail("teacher1@admin.com").isEmpty()) {
            // Create a default department first
            Department defaultDept = departmentRepository.findById(1L).orElseGet(() -> {
                Department d = new Department();
                d.setName("Administration");
                return departmentRepository.save(d);
            });

            Teacher teacher = new Teacher();
            teacher.setName("Admin Teacher");
            teacher.setEmail("teacher1@admin.com");
            teacher.setPassword(passwordEncoder.encode("teacher123"));
            teacher.setDepartment(defaultDept);
            teacherRepository.save(teacher);
            
            System.out.println("✅ Default teacher created: teacher1@admin.com / teacher123");
        }
    }
}
