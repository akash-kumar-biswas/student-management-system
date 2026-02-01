package com.example.studentmanagementsystem.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class DepartmentCreateRequest {
    @NotBlank
    private String name;
}
