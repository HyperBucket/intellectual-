package com.intellectual;

import com.intellectual.domain.Dish;
import com.intellectual.domain.Tag;
import com.intellectual.repository.DishRepository;
import com.intellectual.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final DishRepository dishRepository;
    private final TagRepository tagRepository;

    @Override
    public void run(String... args) {
        if (dishRepository.count() > 0) return;

        Tag italian   = tagRepository.save(Tag.builder().name("Italian").build());
        Tag seafood   = tagRepository.save(Tag.builder().name("Seafood").build());
        Tag american  = tagRepository.save(Tag.builder().name("American").build());

        dishRepository.save(Dish.builder()
                .name("Truffle Risotto")
                .description("Creamy Arborio rice with black truffle and aged parmesan")
                .tags(Set.of(italian))
                .build());

        dishRepository.save(Dish.builder()
                .name("Grilled Salmon")
                .description("Atlantic salmon with lemon butter sauce and seasonal vegetables")
                .tags(Set.of(seafood))
                .build());

        dishRepository.save(Dish.builder()
                .name("Wagyu Beef Burger")
                .description("A5 Wagyu patty, caramelised onion, aged cheddar on a brioche bun")
                .tags(Set.of(american))
                .build());
    }
}
