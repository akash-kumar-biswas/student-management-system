package com.example.studentmanagementsystem.service;

import com.example.studentmanagementsystem.dto.DepartmentCreateRequest;
import com.example.studentmanagementsystem.entity.Department;
import com.example.studentmanagementsystem.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public Department create(DepartmentCreateRequest req) {
        Department d = new Department();
        d.setName(req.getName().trim());
        return departmentRepository.save(d);
    }
}
