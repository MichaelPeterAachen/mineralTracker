package com.ratsoft.mineraltracker.services;

import com.ratsoft.mineraltracker.commands.FoodCommand;
import com.ratsoft.mineraltracker.model.Food;
import lombok.NonNull;

import java.util.Optional;
import java.util.Set;

/**
 * Interface for the service to access the foods.
 *
 * @author mpeter
 */
public interface FoodService {
    /**
     * Return a list of all foods.
     *
     * @return all foods in the database.
     */
    @NonNull Set<Food> getAllFoods();

    /**
     * Return a food for a given id.
     *
     * @param id id of the food
     * @return Optional food.
     */
    @NonNull Optional<Food> getFood(@NonNull Long id);

    /**
     * Save or update a food in the database.
     *
     * @param foodCommand the food to save.
     * @return the saved food as a command. In case a new food was saved, the id is contained.
     */
    @NonNull FoodCommand saveFoodCommand(@NonNull FoodCommand foodCommand);

    /**
     * Delete a food entity.
     *
     * @param id the id of the entitiy to be deleted.
     */
    void deleteFood(@NonNull Long id);
}
