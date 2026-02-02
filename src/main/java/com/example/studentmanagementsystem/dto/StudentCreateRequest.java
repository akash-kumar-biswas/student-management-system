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
    private String username;   // ✅ Step 6.2

    @NotBlank
    private String name;

    @Email @NotBlank
    private String email;

    @NotNull
    private Long departmentId;

    private Set<Long> courseIds;
}
