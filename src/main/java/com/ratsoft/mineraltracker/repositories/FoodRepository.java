package com.ratsoft.mineraltracker.repositories;

import com.ratsoft.mineraltracker.model.Food;
import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * The jpa repository for the foods.
 *
 * @author mpeter
 */
@SuppressWarnings("InterfaceNeverImplemented")
public interface FoodRepository extends CrudRepository<Food, Long> {
    /**
     * Search a food by name.
     *
     * @param name the name to search for.
     * @return the resulting food (if present)
     */
    @NonNull Optional<Food> findByName(@NonNull String name);
}
