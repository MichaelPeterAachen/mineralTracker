package com.ratsoft.mineraltracker.services.it;


import com.ratsoft.mineraltracker.commands.AmountContainedCommand;
import com.ratsoft.mineraltracker.commands.FoodCommand;
import com.ratsoft.mineraltracker.commands.MineralCommand;
import com.ratsoft.mineraltracker.controllers.FoodController;
import com.ratsoft.mineraltracker.converters.FoodMapper;
import com.ratsoft.mineraltracker.model.AmountContained;
import com.ratsoft.mineraltracker.model.Food;
import com.ratsoft.mineraltracker.model.Mineral;
import com.ratsoft.mineraltracker.model.Unit;
import com.ratsoft.mineraltracker.repositories.FoodRepository;
import com.ratsoft.mineraltracker.services.FoodService;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.context.WebApplicationContext;

import java.net.URI;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration test for the food service.
 *
 * @author mpeter
 */
@SuppressWarnings({"PackageVisibleField", "ProhibitedExceptionDeclared", "OptionalGetWithoutIsPresent", "DataFlowIssue"})
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Slf4j
@NoArgsConstructor
public class FoodServiceIT {
    @Autowired
    @NonNull
    FoodService foodService;

    @Autowired
    @NonNull
    FoodController foodController;

    @Autowired
    @NonNull
    FoodRepository foodRepository;

    @Autowired
    FoodMapper foodMapper;

    @Autowired
    private @NonNull WebApplicationContext webApplicationContext;

    @Mock
    private @NonNull Model controllerModel;

    private @NonNull MockMvc mockMvc;

    @BeforeEach
    public void setup() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                                 .build();
    }

    @Transactional
    @Test
    public void testSaveofDescriptionViaService() {
        final FoodCommand foodCommand = createFoodCommand(1L);
        foodCommand.setId(null);


        final FoodCommand saveFoodCommand = foodService.saveFoodCommand(foodCommand);

        final FoodCommand expectedFoodCommand = createExpectedFoodCommand(1L, saveFoodCommand);

        assertThat(saveFoodCommand).usingRecursiveComparison()
                                   .isEqualTo(expectedFoodCommand);
    }

    @Transactional
    @Test
    public void testAddNewFoodViaController() {
        final FoodCommand newFood = createFoodCommand(1L);
        newFood.setId(null);

        final Optional<Food> foodOptionalBefore = foodRepository.findByName(newFood.getName());
        assertThat(foodOptionalBefore).withFailMessage("Precondition for test not correct. Food already in database.")
                                      .isEmpty();

        final String result = foodController.saveOrUpdateFood(newFood);

        final Optional<Food> foodOptionalAfter = foodRepository.findByName(newFood.getName());

        assertThat(foodOptionalAfter).isPresent();

        final Food foodAfter = foodOptionalAfter.get();

        final FoodCommand expectedFood = createExpectedFoodCommand(1L, foodAfter);

        assertThat(foodAfter).usingRecursiveComparison().ignoringFields("containedMinerals")
                             .isEqualTo(expectedFood);

        final List<AmountContained> containedMineralsBefore = foodAfter.getContainedMinerals();
        final List<AmountContainedCommand> containedMineralsExpected = expectedFood.getContainedMinerals();
        assertThat(containedMineralsBefore.size()).isEqualTo(containedMineralsExpected.size());
    }

    @Transactional
    @Test
    public void testUpdateFoodViaController() {
        final Optional<Food> foodOptionalBefore = foodRepository.findById(1L);

        assertThat(foodOptionalBefore).withFailMessage("Precondition for test not correct. Food not in database.")
                                      .isPresent();


        final Food foodBefore = foodOptionalBefore.get();
        final int amountBefore = foodBefore.getContainedMinerals()
                                           .size();

        final FoodCommand foodCommand = foodMapper.foodToCommand(foodBefore);
        foodCommand.getContainedMinerals()
                   .get(0)
                   .setDoDelete(true);

        final String result = foodController.saveOrUpdateFood(foodCommand);

        final Optional<Food> foodOptionalAfter = foodRepository.findById(1L);

        assertThat(foodOptionalAfter).isPresent();

        final int amountAfter = foodOptionalAfter.get()
                                                 .getContainedMinerals()
                                                 .size();

        assertThat(amountAfter).isEqualTo(amountBefore - 1);
    }

    @Transactional
    @Test
    public void testUpdateofDescriptionViaRest() throws Exception {
        final FoodCommand newFood = new FoodCommand(null, "Spinat3", Collections.emptyList());

        final Optional<Food> foodOptionalBefore = foodRepository.findByName(newFood.getName());
        assertThat(foodOptionalBefore).withFailMessage("Precondition for test not correct. Food already in database.")
                                      .isEmpty();

        mockMvc.perform(post(URI.create("/foods")).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                                  .param("name", newFood.getName()))
               .andExpect(status().isMovedTemporarily());

        final Optional<Food> foodOptionalAfter = foodRepository.findByName(newFood.getName());

        assertThat(foodOptionalAfter).isPresent();

        final Food foodAfter = foodOptionalAfter.get();

        final FoodCommand expectedFood = new FoodCommand(foodAfter.getId(), "Spinat3", Collections.emptyList());

        assertThat(foodAfter).usingRecursiveComparison()
                             .isEqualTo(expectedFood);
    }

    @SuppressWarnings("StringConcatenationMissingWhitespace")
    private static Food createFood(final long id) {
        final Mineral mineral = new Mineral(1L, "Eisen", null);
        final AmountContained amountContained = new AmountContained(1L, mineral, 1.0f, Unit.mg, null);

        final Collection<AmountContained> amountContaineds = new ArrayList<>(5);
        amountContaineds.add(amountContained);
        return new Food(id, "Spinat" + id, List.of(amountContained));
    }

    @SuppressWarnings("StringConcatenationMissingWhitespace")
    private static FoodCommand createFoodCommand(final long id) {
        final MineralCommand mineralCommand = new MineralCommand(1L, "Eisen", null);

        final AmountContainedCommand amountContainedCommand = new AmountContainedCommand(1L, mineralCommand, 1.0f, Unit.mg, false);
        amountContainedCommand.setId(null);

        final List<AmountContainedCommand> amountContainedCommands = new ArrayList<>(5);
        amountContainedCommands.add(amountContainedCommand);

        return new FoodCommand(id, "Spinat" + id, amountContainedCommands);
    }

    private static FoodCommand createExpectedFoodCommand(final @NonNull Long id, final FoodCommand saveFoodCommand) {
        final Long savedFoodId = saveFoodCommand.getId();
        final Long amoundContainedId = saveFoodCommand.getContainedMinerals()
                                                      .get(0)
                                                      .getId();

        final FoodCommand expectedFoodCommand = createFoodCommand(id);
        expectedFoodCommand.setId(savedFoodId);
        final AmountContainedCommand amountContained = expectedFoodCommand.getContainedMinerals()
                                                                          .get(0);
        amountContained.setId(amoundContainedId);
        return expectedFoodCommand;
    }

    private static FoodCommand createExpectedFoodCommand(final @NonNull Long id, final Food saveFoodCommand) {
        final Long savedFoodId = saveFoodCommand.getId();

        final FoodCommand expectedFoodCommand = createFoodCommand(id);
        expectedFoodCommand.setId(savedFoodId);

        if (!saveFoodCommand.getContainedMinerals()
                            .isEmpty()) {
            final Long amoundContainedId = saveFoodCommand.getContainedMinerals()
                                                          .get(0)
                                                          .getId();
            final AmountContainedCommand amountContained = expectedFoodCommand.getContainedMinerals()
                                                                              .get(0);
            amountContained.setId(amoundContainedId);
        }
        return expectedFoodCommand;
    }
}
