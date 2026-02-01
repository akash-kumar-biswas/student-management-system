package com.example.studentmanagementsystem.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class CourseCreateRequest {
    @NotBlank
    private String code;

    @NotBlank
    private String title;
}
