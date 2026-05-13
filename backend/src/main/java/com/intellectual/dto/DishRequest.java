package com.intellectual.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public record DishRequest(
        @NotBlank String name,
        String description,
        Set<String> tags   // tag names — created on the fly if new
) {}
