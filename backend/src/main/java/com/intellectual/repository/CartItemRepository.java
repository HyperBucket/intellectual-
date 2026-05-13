package com.intellectual.repository;

import com.intellectual.domain.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    @Query("SELECT ci FROM CartItem ci JOIN FETCH ci.dish d LEFT JOIN FETCH d.tags")
    List<CartItem> findAllWithDish();

    Optional<CartItem> findByDishId(Long dishId);
}
