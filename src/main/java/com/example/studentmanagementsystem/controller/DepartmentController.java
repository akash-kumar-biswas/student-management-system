package com.example.studentmanagementsystem.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.studentmanagementsystem.dto.DepartmentCreateRequest;
import com.example.studentmanagementsystem.entity.Department;
import com.example.studentmanagementsystem.service.DepartmentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping
    @PreAuthorize("hasRole('TEACHER')")
    public Department create(@Valid @RequestBody DepartmentCreateRequest req) {
        return departmentService.create(req);
    }

    @GetMapping
    public List<Department> getAll() {
        return departmentService.getAll();
    }
}
