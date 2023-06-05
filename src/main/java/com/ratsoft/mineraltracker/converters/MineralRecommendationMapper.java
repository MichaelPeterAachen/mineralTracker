package com.ratsoft.mineraltracker.converters;

import com.ratsoft.mineraltracker.commands.MineralRecommendationCommand;
import com.ratsoft.mineraltracker.model.MineralRecommendation;
import org.mapstruct.Mapper;
import org.springframework.lang.Nullable;

/**
 * Mapper between domain and command.
 *
 * @author mpeter
 */
@Mapper(componentModel = "spring")
public interface MineralRecommendationMapper {
    /**
     * Convert mineral recommendation command to a domain object.
     *
     * @param command the command object.
     * @return the domain object.
     */
    @Nullable
    MineralRecommendation commandToMineralRecommendation(@Nullable MineralRecommendationCommand command);

    /**
     * Convert mineral recommendation domain object to a command.
     *
     * @param destination the domain object.
     * @return the command object.
     */
    @Nullable
    MineralRecommendationCommand mineralRecommendationToCommand(@Nullable MineralRecommendation destination);
}
