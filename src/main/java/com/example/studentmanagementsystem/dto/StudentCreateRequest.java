package com.example.studentmanagementsystem.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class StudentCreateRequest {

    @NotBlank
    private String name;

    @Email @NotBlank
    private String email;

    @NotNull
    private Long departmentId;

    // optional: student can be created with courses
    private Set<Long> courseIds;
}
