package com.example.studentmanagementsystem.service;

import com.example.studentmanagementsystem.dto.StudentCreateRequest;
import com.example.studentmanagementsystem.entity.Course;
import com.example.studentmanagementsystem.entity.Department;
import com.example.studentmanagementsystem.entity.Student;
import com.example.studentmanagementsystem.repository.CourseRepository;
import com.example.studentmanagementsystem.repository.DepartmentRepository;
import com.example.studentmanagementsystem.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock StudentRepository studentRepository;
    @Mock DepartmentRepository departmentRepository;
    @Mock CourseRepository courseRepository;
    @Mock PasswordEncoder passwordEncoder;

    @InjectMocks StudentService studentService;

    @Test
    void create_shouldTrimAndNormalizeFields_andEncodePassword() {
        // Arrange
        var dept = new Department();
        dept.setId(10L);

        var course1 = new Course();
        course1.setId(1L);

        var req = new StudentCreateRequest(
                "  apu  ",
                "pass123",
                "  Apu Sharma  ",
                "  APU@Example.Com  ",
                10L,
                Set.of(1L)
        );

        when(departmentRepository.findById(10L)).thenReturn(Optional.of(dept));
        when(courseRepository.findAllById(Set.of(1L))).thenReturn(List.of(course1));
        when(passwordEncoder.encode("pass123")).thenReturn("ENC(pass123)");
        when(studentRepository.save(any(Student.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Student saved = studentService.create(req);

        // Assert (return value)
        assertEquals("apu", saved.getUsername());
        assertEquals("ENC(pass123)", saved.getPassword());
        assertEquals("Apu Sharma", saved.getName());
        assertEquals("apu@example.com", saved.getEmail());
        assertSame(dept, saved.getDepartment());
        assertEquals(1, saved.getCourses().size());
        assertTrue(saved.getCourses().stream().anyMatch(c -> c.getId().equals(1L)));

        // Assert (captured saved entity)
        ArgumentCaptor<Student> captor = ArgumentCaptor.forClass(Student.class);
        verify(studentRepository).save(captor.capture());
        Student toSave = captor.getValue();

        assertEquals("apu", toSave.getUsername());
        assertEquals("apu@example.com", toSave.getEmail());
        verify(passwordEncoder).encode("pass123");
    }

    @Test
    void create_shouldThrow_ifDepartmentNotFound() {
        var req = new StudentCreateRequest(
                "u", "p", "n", "e@e.com", 99L, Set.of()
        );

        when(departmentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> studentService.create(req));
        verify(studentRepository, never()).save(any());
    }
}
