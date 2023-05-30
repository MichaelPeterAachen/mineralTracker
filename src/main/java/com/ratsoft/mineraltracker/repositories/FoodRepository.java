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
    @NonNull Optional<Food> findByName(@NonNull String name);
}
