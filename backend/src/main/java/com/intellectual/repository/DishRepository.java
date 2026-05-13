package com.intellectual.repository;

import com.intellectual.domain.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DishRepository extends JpaRepository<Dish, Long> {

    @Query("SELECT d FROM Dish d LEFT JOIN FETCH d.tags ORDER BY d.createdAt DESC")
    List<Dish> findAllWithTags();
}
