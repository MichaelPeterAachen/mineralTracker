package com.ratsoft.mineraltracker.converters;

import com.ratsoft.mineraltracker.commands.MineralRecommendationCommand;
import com.ratsoft.mineraltracker.model.MineralRecommendation;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.TargetType;
import org.springframework.lang.Nullable;

import java.lang.annotation.Target;

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
