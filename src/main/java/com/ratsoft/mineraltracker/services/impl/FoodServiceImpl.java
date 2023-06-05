package com.ratsoft.mineraltracker.services.impl;

import com.ratsoft.mineraltracker.commands.FoodCommand;
import com.ratsoft.mineraltracker.converters.FoodMapper;
import com.ratsoft.mineraltracker.converters.MineralMapper;
import com.ratsoft.mineraltracker.model.Food;
import com.ratsoft.mineraltracker.repositories.FoodRepository;
import com.ratsoft.mineraltracker.services.FoodService;
import com.ratsoft.mineraltracker.services.MineralService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Service to access the foods.
 *
 * @author mpeter
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class FoodServiceImpl implements FoodService {
    private final @NonNull FoodRepository foodRepositoryRepository;

    private final @NonNull FoodMapper mapper;

    private final @NonNull MineralService mineralService;

    private final @NonNull MineralMapper mineralMapper;

    @Override
    public Set<Food> getAllFoods() {
        final Iterable<Food> repositoryAll = foodRepositoryRepository.findAll();
        final Set<Food> foods = new HashSet<>(10);
        for (final Food food : repositoryAll) {
            foods.add(food);
        }
        return foods;
    }

    @Override
    public Optional<Food> getFood(final Long id) {
        return foodRepositoryRepository.findById(id);
    }

    @Override
    public FoodCommand saveFoodCommand(final FoodCommand foodCommand) {
        log.debug("Save or update food: {}", foodCommand);
        final Food food = mapper.commandToFood(foodCommand);
        //noinspection DataFlowIssue
        final Food foodSaved = foodRepositoryRepository.save(food);
        //noinspection DataFlowIssue
        return mapper.foodToCommand(foodSaved);
    }

    @Override
    public void deleteFood(final Long id) {
        log.debug("Delete food: {}", id);
        foodRepositoryRepository.deleteById(id);
    }
}
