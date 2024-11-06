package com.example.project_.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserDto(@NotBlank String firstName, @NotBlank String lastName, @NotBlank String email, @NotNull int age, @NotBlank String question) {
}
