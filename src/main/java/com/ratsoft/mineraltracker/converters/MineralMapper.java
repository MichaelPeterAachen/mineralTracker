package com.ratsoft.mineraltracker.converters;

import com.ratsoft.mineraltracker.commands.MineralCommand;
import com.ratsoft.mineraltracker.model.Mineral;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MineralMapper {
    Mineral commandToMineral(MineralCommand command);
    MineralCommand mineralToCommand(Mineral destination);
}
