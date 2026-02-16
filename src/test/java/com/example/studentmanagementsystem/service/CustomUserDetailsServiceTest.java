package com.example.studentmanagementsystem.service;

import com.example.studentmanagementsystem.entity.Student;
import com.example.studentmanagementsystem.entity.Teacher;
import com.example.studentmanagementsystem.repository.StudentRepository;
import com.example.studentmanagementsystem.repository.TeacherRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock StudentRepository studentRepository;
    @Mock TeacherRepository teacherRepository;

    @InjectMocks CustomUserDetailsService service;

    @Test
    void loadUserByUsername_whenStudentFound_returnsRoleStudent() {
        Student s = new Student();
        s.setUsername("student1");
        s.setPassword("ENC_PASS");

        when(studentRepository.findByUsername("student1")).thenReturn(Optional.of(s));

        UserDetails ud = service.loadUserByUsername("student1");

        assertEquals("student1", ud.getUsername());
        assertEquals("ENC_PASS", ud.getPassword());
        assertTrue(ud.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_STUDENT")));

        verify(studentRepository).findByUsername("student1");
        verifyNoInteractions(teacherRepository);
    }

    @Test
    void loadUserByUsername_whenTeacherFound_returnsRoleTeacher_andUsesFallbackPasswordIfNull() {
        Teacher t = new Teacher();
        t.setEmail("teacher1@admin.com");
        t.setPassword(null); // to trigger fallback

        when(studentRepository.findByUsername("teacher1@admin.com")).thenReturn(Optional.empty());
        when(teacherRepository.findByEmail("teacher1@admin.com")).thenReturn(Optional.of(t));

        UserDetails ud = service.loadUserByUsername("teacher1@admin.com");

        assertEquals("teacher1@admin.com", ud.getUsername());
        assertEquals("{noop}teacher123", ud.getPassword()); // fallback in your code
        assertTrue(ud.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_TEACHER")));

        verify(studentRepository).findByUsername("teacher1@admin.com");
        verify(teacherRepository).findByEmail("teacher1@admin.com");
    }

    @Test
    void loadUserByUsername_whenNotFound_throws() {
        when(studentRepository.findByUsername("x")).thenReturn(Optional.empty());
        when(teacherRepository.findByEmail("x")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername("x"));
    }
}
