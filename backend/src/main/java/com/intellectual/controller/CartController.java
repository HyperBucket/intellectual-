package com.intellectual.controller;

import com.intellectual.domain.CartItem;
import com.intellectual.domain.Dish;
import com.intellectual.domain.OrderRecord;
import com.intellectual.dto.AddToCartRequest;
import com.intellectual.dto.CartItemResponse;
import com.intellectual.repository.CartItemRepository;
import com.intellectual.repository.DishRepository;
import com.intellectual.repository.OrderRecordRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartItemRepository cartItemRepository;
    private final DishRepository dishRepository;
    private final OrderRecordRepository orderRecordRepository;

    @GetMapping
    public List<CartItemResponse> getCart() {
        return cartItemRepository.findAllWithDish().stream()
                .map(item -> CartItemResponse.from(item, 0L))
                .toList();
    }

    @PostMapping("/items")
    public ResponseEntity<CartItemResponse> addToCart(@Valid @RequestBody AddToCartRequest request) {
        Dish dish = dishRepository.findById(request.dishId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dish not found"));

        CartItem item = cartItemRepository.findByDishId(request.dishId())
                .map(existing -> {
                    existing.setQuantity(existing.getQuantity() + 1);
                    return cartItemRepository.save(existing);
                })
                .orElseGet(() -> cartItemRepository.save(
                        CartItem.builder().dish(dish).quantity(1).build()));

        // Record the order so ORDER_COUNT sorting stays accurate
        orderRecordRepository.save(OrderRecord.builder()
                .dish(dish)
                .orderedAt(LocalDateTime.now())
                .quantity(1)
                .build());

        return ResponseEntity.status(HttpStatus.CREATED).body(CartItemResponse.from(item, 0L));
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<Void> removeFromCart(@PathVariable Long id) {
        cartItemRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
