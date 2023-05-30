package com.ratsoft.mineraltracker.controllers;

import com.ratsoft.mineraltracker.commands.AmountContainedCommand;
import com.ratsoft.mineraltracker.commands.FoodCommand;
import com.ratsoft.mineraltracker.commands.MineralCommand;
import com.ratsoft.mineraltracker.converters.FoodMapper;
import com.ratsoft.mineraltracker.model.Food;
import com.ratsoft.mineraltracker.model.Mineral;
import com.ratsoft.mineraltracker.services.FoodService;
import com.ratsoft.mineraltracker.services.MineralService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * The controller to access the foods..
 *
 * @author mpeter
 */
@SuppressWarnings({"HardcodedFileSeparator", "SameReturnValue"})
@Slf4j
@Controller
@RequiredArgsConstructor
public class FoodController {

    private final @NonNull FoodService foodService;

    private final @NonNull FoodMapper foodMapper;

    private final @NonNull MineralService mineralService;

    /**
     * Get a list of all foods, add it to the model and return the name of the template.
     *
     * @param model the model for the template.
     * @return the template name.
     */
    @GetMapping({"/foods", "/foods/"})
    public @NonNull String listFoods(final Model model) {
        log.debug("Getting food list");

        final Set<Food> allFoods = foodService.getAllFoods();

        model.addAttribute("foods", allFoods);

        return "foods/list";
    }

    /**
     * Get a food by it's id, add it to the model and return the name of the template.
     *
     * @param id    the id of the food.
     * @param model the model for the template.
     * @return the template name.
     */
    @SuppressWarnings("NestedMethodCall")
    @GetMapping("/foods/{id}")
    public @NonNull String showFood(@PathVariable final @NonNull String id, final @NonNull Model model) {
        log.debug("Getting food with id: {}", id);

        final Optional<Food> food = foodService.getFood(Long.valueOf(id));
        food.ifPresent(value -> model.addAttribute("food", value));

        return "foods/show";
    }

    /**
     * Get the edit form for a food.
     *
     * @param id    the id of the food.
     * @param model the model for the template.
     * @return the template name or editing.
     */
    @SuppressWarnings("NestedMethodCall")
    @GetMapping("/foods/{id}/editform")
    public @NonNull String getEditFoodForm(@PathVariable final @NonNull String id, final @NonNull Model model) {
        log.debug("Getting edit form for a food with id: {}", id);

        final Optional<Food> food = foodService.getFood(Long.valueOf(id));

        if (food.isPresent()) {
            final FoodCommand attributeValue = foodMapper.foodToCommand(food.get());

            final AmountContainedCommand containedCommand = new AmountContainedCommand();
            containedCommand.setMineral(new MineralCommand());

            final AmountContainedCommand containedCommand1 = new AmountContainedCommand();
            containedCommand1.setMineral(new MineralCommand());

            final AmountContainedCommand containedCommand2 = new AmountContainedCommand();
            containedCommand2.setMineral(new MineralCommand());

            if (attributeValue != null) {
                final List<AmountContainedCommand> containedMinerals = attributeValue.getContainedMinerals();
                containedMinerals.add(containedCommand);
                containedMinerals.add(containedCommand1);
                containedMinerals.add(containedCommand2);

                model.addAttribute("food", attributeValue);
            }
        }
        final Set<Mineral> minerals = mineralService.getAllMinerals();
        model.addAttribute("minerals", minerals);

        return "foods/editform";
    }

    /**
     * Get the form for a new food.
     *
     * @param model the model for the template.
     * @return the template name or editing.
     */
    @SuppressWarnings("NestedMethodCall")
    @GetMapping("/foods/newform")
    public @NonNull String getNewFoodForm(@NonNull final Model model) {
        log.debug("Getting new food form.");

        final Food food = new Food();
        final FoodCommand attributeValue = foodMapper.foodToCommand(food);
        if (attributeValue != null) {
            attributeValue.getContainedMinerals()
                          .add(new AmountContainedCommand());
            attributeValue.getContainedMinerals()
                          .add(new AmountContainedCommand());
            attributeValue.getContainedMinerals()
                          .add(new AmountContainedCommand());

            model.addAttribute("food", attributeValue);
        }
        final Set<Mineral> minerals = mineralService.getAllMinerals();
        model.addAttribute("minerals", minerals);

        return "foods/editform";
    }

    /**
     * Save or update a specific food command.
     *
     * @param command the command to save or update.
     * @return New page being shown afterwars.
     */
    @PostMapping({"/foods", "/foods/"})
    public @NonNull String saveOrUpdateFood(@ModelAttribute final @NonNull FoodCommand command) {
        log.info("Request to save or update food: {}", command);

        command.removeEntryContainments();
        command.removeDeletedContainments();

        final FoodCommand savedCommand = foodService.saveFoodCommand(command);

        log.info("Saved or updated successfully: {}", savedCommand);
        return "redirect:/foods/" + savedCommand.getId()+ "/editform";
    }

    /**
     * Delete a specific food.
     *
     * @param id    the id of the food to delete.
     * @param model the model for the templates.
     * @return New page being shown afterwars.
     */
    @SuppressWarnings("OverlyBroadCatchBlock")
    @GetMapping({"/foods/{id}/delete", "/foods/{id}/delete/"})
    public @NonNull String deleteFood(@PathVariable final @NonNull String id, final @NonNull Model model) {
        log.info("Request to delete a food: {}", id);
        try {
            foodService.deleteFood(Long.valueOf(id));
        } catch (final Exception e) {
            model.addAttribute("error", "Food could not be removed, it is still in use.");
            return "error/error";
        }
        return "redirect:/foods";
    }
}
