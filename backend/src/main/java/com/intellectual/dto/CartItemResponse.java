package com.intellectual.dto;

import com.intellectual.domain.CartItem;

public record CartItemResponse(
        Long id,
        DishResponse dish,
        int quantity
) {
    public static CartItemResponse from(CartItem item, long orderCount) {
        return new CartItemResponse(item.getId(), DishResponse.from(item.getDish(), orderCount), item.getQuantity());
    }
}
