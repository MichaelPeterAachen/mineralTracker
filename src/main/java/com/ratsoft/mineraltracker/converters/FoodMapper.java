package com.ratsoft.mineraltracker.converters;

import com.ratsoft.mineraltracker.commands.FoodCommand;
import com.ratsoft.mineraltracker.model.Food;
import org.mapstruct.Mapper;
import org.springframework.lang.Nullable;

/**
 * Mapper between domain and command.
 *
 * @author mpeter
 */
@Mapper(componentModel = "spring")
public interface FoodMapper {
    /**
     * Convert food command to a domain object.
     *
     * @param command the command object.
     * @return the domain object.
     */
    @Nullable
    Food commandToFood(@Nullable FoodCommand command);
    /**
     * Convert food contained domain object to a command.
     *
     * @param destination the domain object.
     * @return the command object.
     */
    @Nullable
    FoodCommand foodToCommand(@Nullable Food destination);
}
