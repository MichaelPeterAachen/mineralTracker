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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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
@RequestMapping("/foods")
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
    @GetMapping({"", "/"})
    public @NonNull String listFoods(final Model model) {
        log.debug("Getting food list");

        final Set<Food> allFoods = foodService.getAllFoods();

        model.addAttribute("foods", allFoods);

        return "foods/list";
    }

    /**
     * Get a food by it's id, add it to the model and return the name of the template.
     *
     * @param id the id of the food.
     * @return the template name.
     */
    @GetMapping("/{id}")
    public @NonNull ModelAndView showFood(@PathVariable final @NonNull String id) {
        log.debug("Getting food with id: {}", id);

        final ModelAndView mav = new ModelAndView("foods/show");

        final Optional<Food> food = foodService.getFood(Long.valueOf(id));
        food.ifPresent(value -> mav.addObject("food", value));

        return mav;
    }

    /**
     * Get the edit form for a food.
     *
     * @param id    the id of the food.
     * @param model the model for the template.
     * @return the template name or editing.
     */
    @SuppressWarnings("NestedMethodCall")
    @GetMapping("/{id}/editform")
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
    @GetMapping("/newform")
    public @NonNull String getNewFoodForm(final @NonNull Model model) {
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
    @PostMapping({"", "/"})
    public @NonNull String saveOrUpdateFood(@ModelAttribute final @NonNull FoodCommand command) {
        log.info("Request to save or update food: {}", command);

        final FoodCommand savedCommand = foodService.saveFoodCommand(command);

        return "redirect:/foods/" + savedCommand.getId() + "/editform";
    }

    /**
     * Delete a specific food.
     *
     * @param id    the id of the food to delete.
     * @param model the model for the templates.
     * @return New page being shown afterwars.
     */
    @SuppressWarnings("OverlyBroadCatchBlock")
    @GetMapping({"/{id}/delete", "/{id}/delete/"})
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
