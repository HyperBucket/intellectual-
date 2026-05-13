package com.intellectual.dto;

import java.time.LocalDateTime;

public enum TimePeriod {
    LAST_WEEK,
    LAST_MONTH,
    LAST_YEAR,
    ALL_TIME;

    public LocalDateTime toStartDate() {
        LocalDateTime now = LocalDateTime.now();
        return switch (this) {
            case LAST_WEEK  -> now.minusWeeks(1);
            case LAST_MONTH -> now.minusMonths(1);
            case LAST_YEAR  -> now.minusYears(1);
            case ALL_TIME   -> null;   // null means no lower bound
        };
    }
}
