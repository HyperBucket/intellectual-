package com.intellectual.service;

import com.intellectual.domain.Dish;
import com.intellectual.domain.Tag;
import com.intellectual.dto.*;
import com.intellectual.repository.DishRepository;
import com.intellectual.repository.OrderRecordRepository;
import com.intellectual.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DishService {

    private final DishRepository dishRepository;
    private final TagRepository tagRepository;
    private final OrderRecordRepository orderRecordRepository;

    @Transactional(readOnly = true)
    public List<DishResponse> listDishes(SortBy sortBy, TimePeriod timePeriod) {
        List<Dish> dishes = dishRepository.findAllWithTags();

        // Build dishId → orderCount map
        Map<Long, Long> orderCounts = buildOrderCountMap(timePeriod);

        List<DishResponse> responses = dishes.stream()
                .map(d -> DishResponse.from(d, orderCounts.getOrDefault(d.getId(), 0L)))
                .collect(Collectors.toCollection(ArrayList::new));

        // Sort
        Comparator<DishResponse> comparator = switch (sortBy) {
            case ORDER_COUNT  -> Comparator.comparingLong(DishResponse::orderCount).reversed();
            case CREATED_DATE -> Comparator.comparing(DishResponse::createdAt).reversed();
        };
        responses.sort(comparator);

        return responses;
    }

    @Transactional
    public DishResponse createDish(DishRequest request) {
        Set<Tag> tags = resolveTags(request.tags());
        Dish dish = Dish.builder()
                .name(request.name())
                .description(request.description())
                .tags(tags)
                .build();
        dish = dishRepository.save(dish);
        return DishResponse.from(dish, 0L);
    }

    @Transactional
    public DishResponse updateDish(Long id, DishRequest request) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Dish not found: " + id));
        dish.setName(request.name());
        dish.setDescription(request.description());
        dish.setTags(resolveTags(request.tags()));
        dish = dishRepository.save(dish);

        long count = buildOrderCountMap(TimePeriod.LAST_MONTH).getOrDefault(id, 0L);
        return DishResponse.from(dish, count);
    }

    @Transactional
    public void deleteDish(Long id) {
        dishRepository.deleteById(id);
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    private Map<Long, Long> buildOrderCountMap(TimePeriod period) {
        List<Object[]> rows = (period == TimePeriod.ALL_TIME || period == null)
                ? orderRecordRepository.sumQuantityPerDishAllTime()
                : orderRecordRepository.sumQuantityPerDishSince(period.toStartDate());

        Map<Long, Long> map = new HashMap<>();
        for (Object[] row : rows) {
            map.put(((Number) row[0]).longValue(), ((Number) row[1]).longValue());
        }
        return map;
    }

    private Set<Tag> resolveTags(Set<String> names) {
        if (names == null || names.isEmpty()) return new HashSet<>();
        Set<Tag> existing = tagRepository.findByNameIn(names);
        Set<String> existingNames = existing.stream().map(Tag::getName).collect(Collectors.toSet());
        names.stream()
                .filter(n -> !existingNames.contains(n))
                .map(n -> tagRepository.save(Tag.builder().name(n).build()))
                .forEach(existing::add);
        return existing;
    }
}
