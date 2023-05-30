package com.ratsoft.mineraltracker.repositories;

import com.ratsoft.mineraltracker.model.Food;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for the food repository.
 *
 * @author mpeter
 */
@SuppressWarnings("PackageVisibleField")
@ExtendWith(SpringExtension.class)
@DataJpaTest
@NoArgsConstructor
class FoodRepositoryTest {

    @Autowired
    @NonNull
    FoodRepository foodRepository;

    /**
     * Test the find by id method of the repository.
     */
    @Test
    public void testFindById() {
        final Optional<Food> foodOpt = foodRepository.findById(1L);
        assertThat(foodOpt).isPresent();

        final Food food = foodOpt.get();

        assertThat(food.getId()).isEqualTo(1L);
        assertThat(food.getName()).isEqualTo("Fenchel");
        assertThat(food.getContainedMinerals()).hasSize(2);
    }
}