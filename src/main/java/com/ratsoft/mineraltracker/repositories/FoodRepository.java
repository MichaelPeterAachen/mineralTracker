package com.ratsoft.mineraltracker.repositories;

import com.ratsoft.mineraltracker.model.Food;
import org.springframework.data.repository.CrudRepository;

/**
 * The jpa repository for the foods.
 *
 * @author mpeter
 */
@SuppressWarnings("InterfaceNeverImplemented")
public interface FoodRepository extends CrudRepository<Food, Long> {
}
