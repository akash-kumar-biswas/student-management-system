package com.example.studentmanagementsystem.service;

import java.util.Collections;
import java.util.Optional;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.studentmanagementsystem.entity.Student;
import com.example.studentmanagementsystem.entity.Teacher;
import com.example.studentmanagementsystem.repository.StudentRepository;
import com.example.studentmanagementsystem.repository.TeacherRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Try to find student first
        Optional<Student> student = studentRepository.findByUsername(username);
        if (student.isPresent()) {
            Student s = student.get();
            return User.builder()
                    .username(s.getUsername())
                    .password(s.getPassword())
                    .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_STUDENT")))
                    .build();
        }

        // Try to find teacher
        Optional<Teacher> teacher = teacherRepository.findByEmail(username);
        if (teacher.isPresent()) {
            Teacher t = teacher.get();
            return User.builder()
                    .username(t.getEmail())
                    .password(t.getPassword() != null ? t.getPassword() : "{noop}teacher123") // fallback
                    .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_TEACHER")))
                    .build();
        }

        throw new UsernameNotFoundException("User not found: " + username);
    }
}
