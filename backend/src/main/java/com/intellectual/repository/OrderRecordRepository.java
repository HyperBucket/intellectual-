package com.intellectual.repository;

import com.intellectual.domain.OrderRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRecordRepository extends JpaRepository<OrderRecord, Long> {

    @Query("""
            SELECT o.dish.id, COALESCE(SUM(o.quantity), 0)
            FROM OrderRecord o
            WHERE o.orderedAt >= :since
            GROUP BY o.dish.id
            """)
    List<Object[]> sumQuantityPerDishSince(@Param("since") LocalDateTime since);

    @Query("""
            SELECT o.dish.id, COALESCE(SUM(o.quantity), 0)
            FROM OrderRecord o
            GROUP BY o.dish.id
            """)
    List<Object[]> sumQuantityPerDishAllTime();
}
