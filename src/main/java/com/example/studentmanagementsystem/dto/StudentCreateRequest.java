package com.example.studentmanagementsystem.dto;

import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class StudentCreateRequest {

    @NotBlank
    private String username;   // ✅ Step 6.2

    @NotBlank(message = "Password is required")
    private String password;   // Password for login

    @NotBlank
    private String name;

    @Email @NotBlank
    private String email;

    @NotNull
    private Long departmentId;

    private Set<Long> courseIds;
}
