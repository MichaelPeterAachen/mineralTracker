package com.ratsoft.mineraltracker.converters;

import com.ratsoft.mineraltracker.commands.MineralCommand;
import com.ratsoft.mineraltracker.commands.MineralRecommendationCommand;
import com.ratsoft.mineraltracker.model.Mineral;
import com.ratsoft.mineraltracker.model.MineralRecommendation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MineralRecommendationMapper {
    MineralRecommendation commandToMineralRecommendation(MineralRecommendationCommand command);
    MineralRecommendationCommand mineralRecommendationToCommand(MineralRecommendation destination);
}
