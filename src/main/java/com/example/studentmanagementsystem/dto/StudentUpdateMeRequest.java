package com.example.studentmanagementsystem.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record StudentUpdateMeRequest(
        @NotBlank String name,
        @Email @NotBlank String email
) {}
