package com.intellectual.dto;

import com.intellectual.domain.Dish;

import java.time.LocalDateTime;
import java.util.List;

public record DishResponse(
        Long id,
        String name,
        String description,
        List<TagDto> tags,
        long orderCount,
        LocalDateTime createdAt,
        boolean hasPhoto
) {
    public static DishResponse from(Dish dish, long orderCount) {
        return new DishResponse(
                dish.getId(),
                dish.getName(),
                dish.getDescription(),
                dish.getTags().stream().map(TagDto::from).toList(),
                orderCount,
                dish.getCreatedAt(),
                dish.isHasPhoto()
        );
    }
}
