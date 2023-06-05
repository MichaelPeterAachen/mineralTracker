package com.ratsoft.mineraltracker.controllers;

import com.ratsoft.mineraltracker.commands.AmountContainedCommand;
import com.ratsoft.mineraltracker.commands.FoodCommand;
import com.ratsoft.mineraltracker.commands.MineralCommand;
import com.ratsoft.mineraltracker.converters.FoodMapper;
import com.ratsoft.mineraltracker.model.AmountContained;
import com.ratsoft.mineraltracker.model.Food;
import com.ratsoft.mineraltracker.model.Mineral;
import com.ratsoft.mineraltracker.model.Unit;
import com.ratsoft.mineraltracker.services.FoodService;
import com.ratsoft.mineraltracker.services.MineralService;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Tests for the controller for accessing the food entities.
 *
 * @author mpeter
 */
@SuppressWarnings({"ProhibitedExceptionDeclared", "MissingJavadoc", "HardcodedFileSeparator"})
@ExtendWith(MockitoExtension.class)
@SpringBootTest
@NoArgsConstructor
class FoodControllerTest {

    @Autowired
    private @NonNull FoodController foodController;

    @Autowired
    private @NonNull FoodMapper foodMapper;

    @Mock
    private @NonNull FoodService foodService;

    @Mock
    private @NonNull MineralService mineralService;

    @Mock
    private @NonNull Model model;

    private @NonNull MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        foodController = new FoodController(foodService, foodMapper, mineralService);
        mockMvc = MockMvcBuilders.standaloneSetup(foodController)
                                 .build();
    }

    @SuppressWarnings({"unchecked", "NestedMethodCall"})
    @Test
    void listFoods() throws Exception {
        final Food food1 = createFood(1L);
        final Food food2 = createFood(2L);
        final Food food3 = createFood(3L);

        final Set<Food> foodList = Set.of(food1, food2, food3);

        when(foodService.getAllFoods()).thenReturn(foodList);

        final String result = foodController.listFoods(model);

        final ArgumentCaptor<Set<Food>> argumentCaptor = ArgumentCaptor.forClass(Set.class);

        verify(model, times(1))
                .addAttribute(eq("foods"), argumentCaptor.capture());

        final Set<Food> resultFoods = argumentCaptor.getValue();
        assertThat(resultFoods).containsExactlyInAnyOrder(food1, food2, food3);

        mockMvc.perform(get("/foods"))
               .andExpect(status().isOk())
               .andExpect(view().name("foods/list"))
               .andExpect(MockMvcResultMatchers.model()
                                               .attribute("foods", Matchers.equalToObject(foodList)));
    }

    @SuppressWarnings("NestedMethodCall")
    @Test
    void showFood() throws Exception {
        final Food food2 = createFood(2L);

        when(foodService.getFood(2L)).thenReturn(Optional.of(food2));

        final ModelAndView result = foodController.showFood("2");

        mockMvc.perform(get("/foods/2"))
               .andExpect(status().isOk())
               .andExpect(view().name("foods/show"))
               .andExpect(MockMvcResultMatchers.model()
                                               .attribute("food", food2));
    }

    @SuppressWarnings("NestedMethodCall")
    @Test
    void getEditFormForFood() throws Exception {
        final Food food2 = createFood(2L);
        final FoodCommand foodCommand2 = createFoodCommand(2L);

        when(foodService.getFood(2L)).thenReturn(Optional.of(food2));

        mockMvc.perform(get("/foods/2/editform"))
               .andExpect(status().isOk())
               .andExpect(view().name("foods/editform"))
               .andExpect(MockMvcResultMatchers.model()
                                               .attributeExists("food"));
    }

    @SuppressWarnings("NestedMethodCall")
    @Test
    void getNewFormForFood() throws Exception {
        final FoodCommand foodCommandNew = new FoodCommand();

        mockMvc.perform(get("/foods/newform"))
               .andExpect(status().isOk())
               .andExpect(view().name("foods/editform"))
               .andExpect(MockMvcResultMatchers.model()
                                               .attributeExists("food"));

        // TODO: Test minerals
    }

    @SuppressWarnings("NestedMethodCall")
    @Test
    void newFood() throws Exception {
        final FoodCommand foodCommandNew = createFoodCommand(3L);
        foodCommandNew.setId(null);

        final FoodCommand foodCommandNewSaved = createFoodCommand(3L);

        when(foodService.saveFoodCommand(any())).thenReturn(foodCommandNewSaved);

        mockMvc.perform(post(URI.create("/foods")).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                                  .param("name", "NewFood2"))
               .andExpect(status().is3xxRedirection())
               .andExpect(view().name("redirect:/foods/3/editform"));

        // TODO: Test minerals
    }

    @SuppressWarnings("NestedMethodCall")
    @Test
    void deleteFood() throws Exception {
        mockMvc.perform(get("/foods/1/delete"))
               .andExpect(status().is3xxRedirection())
               .andExpect(view().name("redirect:/foods"));

        verify(foodService, times(1)).deleteFood(1L);
    }

    @SuppressWarnings("StringConcatenationMissingWhitespace")
    private static Food createFood(final long id) {
        final Mineral mineral = new Mineral(id, "SELEN" + id, null);
        final AmountContained amountContained = new AmountContained(id, mineral, 1.0f + id, Unit.mg, null);
        return new Food(id, "Spinat", List.of(amountContained));
    }

    @SuppressWarnings("StringConcatenationMissingWhitespace")
    private static FoodCommand createFoodCommand(final long id) {
        final MineralCommand mineralCommand = new MineralCommand(id, "SELEN" + id, null);
        final AmountContainedCommand amountContainedCommand = new AmountContainedCommand(id, mineralCommand, 1.0f + id, Unit.mg, false);
        return new FoodCommand(id, "Spinat", List.of(amountContainedCommand));
    }
}