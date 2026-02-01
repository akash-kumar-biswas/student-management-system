package com.example.studentmanagementsystem.controller;

import com.example.studentmanagementsystem.dto.DepartmentCreateRequest;
import com.example.studentmanagementsystem.entity.Department;
import com.example.studentmanagementsystem.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping
    public Department create(@Valid @RequestBody DepartmentCreateRequest req) {
        return departmentService.create(req);
    }
}
