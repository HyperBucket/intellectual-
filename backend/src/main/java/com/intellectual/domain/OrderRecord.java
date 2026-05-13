package com.intellectual.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "order_records",
        indexes = @Index(name = "idx_order_dish_date", columnList = "dish_id, ordered_at"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dish_id", nullable = false)
    private Dish dish;

    @Column(name = "ordered_at", nullable = false)
    private LocalDateTime orderedAt;

    @Column(nullable = false)
    private Integer quantity;
}
