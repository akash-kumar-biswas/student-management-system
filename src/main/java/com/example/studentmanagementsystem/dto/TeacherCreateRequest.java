package com.example.studentmanagementsystem.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class TeacherCreateRequest {

    @NotBlank
    private String name;

    @Email @NotBlank
    private String email;

    @NotNull
    private Long departmentId;
}
