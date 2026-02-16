package com.example.studentmanagementsystem.service;

import com.example.studentmanagementsystem.dto.TeacherCreateRequest;
import com.example.studentmanagementsystem.entity.Department;
import com.example.studentmanagementsystem.entity.Teacher;
import com.example.studentmanagementsystem.repository.DepartmentRepository;
import com.example.studentmanagementsystem.repository.TeacherRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {

    @Mock TeacherRepository teacherRepository;
    @Mock DepartmentRepository departmentRepository;

    @InjectMocks TeacherService teacherService;

    @Test
    void create_shouldTrimName_andLowercaseEmail_andAttachDepartment() {
        Department dept = new Department();
        dept.setId(10L);

        when(departmentRepository.findById(10L)).thenReturn(Optional.of(dept));
        when(teacherRepository.save(any(Teacher.class))).thenAnswer(inv -> inv.getArgument(0));

        TeacherCreateRequest req = new TeacherCreateRequest(
                "  Apu Sharma  ",
                "  APU@Example.Com  ",
                10L
        );

        Teacher saved = teacherService.create(req);

        assertEquals("Apu Sharma", saved.getName());
        assertEquals("apu@example.com", saved.getEmail());
        assertSame(dept, saved.getDepartment());

        ArgumentCaptor<Teacher> captor = ArgumentCaptor.forClass(Teacher.class);
        verify(teacherRepository).save(captor.capture());
        assertEquals("apu@example.com", captor.getValue().getEmail());
    }

    @Test
    void create_whenDepartmentMissing_shouldThrow_andNotSave() {
        when(departmentRepository.findById(99L)).thenReturn(Optional.empty());

        TeacherCreateRequest req = new TeacherCreateRequest("n", "a@a.com", 99L);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> teacherService.create(req));
        assertTrue(ex.getMessage().toLowerCase().contains("department"));

        verify(teacherRepository, never()).save(any());
    }

    @Test
    void delete_shouldCallRepositoryDeleteById() {
        teacherService.delete(7L);
        verify(teacherRepository).deleteById(7L);
    }
}
