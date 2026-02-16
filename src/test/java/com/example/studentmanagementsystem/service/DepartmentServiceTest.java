package com.example.studentmanagementsystem.service;

import com.example.studentmanagementsystem.dto.DepartmentCreateRequest;
import com.example.studentmanagementsystem.entity.Department;
import com.example.studentmanagementsystem.repository.DepartmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceTest {

    @Mock DepartmentRepository departmentRepository;
    @InjectMocks DepartmentService departmentService;

    @Test
    void create_whenUniqueName_savesTrimmedName() {
        when(departmentRepository.findAll()).thenReturn(List.of());
        when(departmentRepository.save(any(Department.class))).thenAnswer(inv -> inv.getArgument(0));

        DepartmentCreateRequest req = new DepartmentCreateRequest("  CSE  ");
        Department saved = departmentService.create(req);

        assertEquals("CSE", saved.getName());

        ArgumentCaptor<Department> captor = ArgumentCaptor.forClass(Department.class);
        verify(departmentRepository).save(captor.capture());
        assertEquals("CSE", captor.getValue().getName());
    }

    @Test
    void create_whenDuplicateNameIgnoringCase_throwsAndDoesNotSave() {
        Department existing = new Department();
        existing.setName("CSE");

        when(departmentRepository.findAll()).thenReturn(List.of(existing));

        DepartmentCreateRequest req = new DepartmentCreateRequest("  cse  ");

        RuntimeException ex = assertThrows(RuntimeException.class, () -> departmentService.create(req));
        assertTrue(ex.getMessage().toLowerCase().contains("already exists"));

        verify(departmentRepository, never()).save(any());
    }
}
