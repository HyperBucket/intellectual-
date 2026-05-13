package com.intellectual.dto;

import jakarta.validation.constraints.NotNull;

public record AddToCartRequest(@NotNull Long dishId) {}
