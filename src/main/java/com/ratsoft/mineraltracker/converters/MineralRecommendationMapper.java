package com.ratsoft.mineraltracker.converters;

import com.ratsoft.mineraltracker.commands.MineralRecommendationCommand;
import com.ratsoft.mineraltracker.model.MineralRecommendation;
import org.mapstruct.Mapper;

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
    MineralRecommendation commandToMineralRecommendation(MineralRecommendationCommand command);

    /**
     * Convert mineral recommendation domain object to a command.
     *
     * @param destination the domain object.
     * @return the command object.
     */
    MineralRecommendationCommand mineralRecommendationToCommand(MineralRecommendation destination);
}
