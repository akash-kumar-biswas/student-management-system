package com.example.studentmanagementsystem.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.studentmanagementsystem.dto.DepartmentCreateRequest;
import com.example.studentmanagementsystem.entity.Department;
import com.example.studentmanagementsystem.repository.DepartmentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public Department create(DepartmentCreateRequest req) {
        String departmentName = req.getName().trim();
        
        // Check if department already exists
        if (departmentRepository.findAll().stream()
                .anyMatch(d -> d.getName().equalsIgnoreCase(departmentName))) {
            throw new RuntimeException("Department with name '" + departmentName + "' already exists");
        }
        
        Department d = new Department();
        d.setName(departmentName);
        return departmentRepository.save(d);
    }

    public List<Department> getAll() {
        return departmentRepository.findAll();
    }
}
