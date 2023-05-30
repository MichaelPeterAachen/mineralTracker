package com.ratsoft.mineraltracker.services;

import com.ratsoft.mineraltracker.commands.AmountContainedCommand;
import com.ratsoft.mineraltracker.commands.FoodCommand;
import com.ratsoft.mineraltracker.commands.MineralCommand;
import com.ratsoft.mineraltracker.converters.FoodMapper;
import com.ratsoft.mineraltracker.model.AmountContained;
import com.ratsoft.mineraltracker.model.Food;
import com.ratsoft.mineraltracker.model.Mineral;
import com.ratsoft.mineraltracker.model.Unit;
import com.ratsoft.mineraltracker.repositories.FoodRepository;
import com.ratsoft.mineraltracker.services.impl.FoodServiceImpl;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@NoArgsConstructor
class FoodServiceTest {
    @Mock
    private @NonNull FoodRepository foodRepository;

    private @NonNull FoodService foodService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        final FoodMapper mapper = Mappers.getMapper(FoodMapper.class);

        foodService = new FoodServiceImpl(foodRepository, mapper);
    }

    @Test
    public void getAllFoods() {
        final Food food1 = createFood(1L);
        final Food food2 = createFood(2L);

        final List<Food> foodList = List.of(food1, food2);

        when(foodRepository.findAll()).thenReturn(foodList);

        final Set<Food> allFoods = foodService.getAllFoods();

        assertThat(allFoods).containsExactlyInAnyOrder(food1, food2);
    }

    @Test
    public void getFoodPresent() {
        final Food food2 = createFood(3L);

        when(foodRepository.findById(3L)).thenReturn(Optional.of(food2));

        final Optional<Food> foodResult = foodService.getFood(3L);

        assertThat(foodResult).isPresent()
                              .contains(food2);
    }

    @Test
    public void getFoodNotPresent() {
        when(foodRepository.findById(4L)).thenReturn(Optional.empty());

        final Optional<Food> food = foodService.getFood(4L);

        assertThat(food).isEmpty();
    }

    @Test
    public void saveFood() {
        final Food foodNew = createFood(5L);
        foodNew.setId(null);
        final Food foodSaved = createFood(5L);

        when(foodRepository.save(foodNew)).thenReturn(foodSaved);

        final FoodCommand foodCommandNew = createFoodCommand(5L);
        foodCommandNew.setId(null);

        final FoodCommand foodCommandNewSaved = foodService.saveFoodCommand(foodCommandNew);

        assertThat(foodCommandNewSaved).usingRecursiveComparison().ignoringFields("containedMinerals").isEqualTo(foodSaved);
        assertThat(foodCommandNewSaved.getContainedMinerals()).usingRecursiveFieldByFieldElementComparatorIgnoringFields("doDelete").isEqualTo(foodSaved.getContainedMinerals());
    }

    @Test
    public void deleteFoodl() {
        foodService.deleteFood(2L);
        verify(foodRepository, times(1)).deleteById(2L);
    }

    @SuppressWarnings("StringConcatenationMissingWhitespace")
    private static Food createFood(final long id) {
        final Mineral food = new Mineral(id, "SELEN" + id, null);
        final AmountContained amountContained = new AmountContained(id, food, 1.0f + id, Unit.mg, null);
        return new Food(id, "Spinat", List.of(amountContained));
    }

    @SuppressWarnings("StringConcatenationMissingWhitespace")
    private static FoodCommand createFoodCommand(@NonNull final long id) {
        final MineralCommand mineralCommand = new MineralCommand(id, "SELEN" + id, null);
        final AmountContainedCommand amountContainedCommand = new AmountContainedCommand(id, mineralCommand, 1.0f + id, Unit.mg, false);
        return new FoodCommand(id, "Spinat", List.of(amountContainedCommand));
    }
}