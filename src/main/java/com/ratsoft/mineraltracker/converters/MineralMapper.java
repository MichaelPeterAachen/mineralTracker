package com.ratsoft.mineraltracker.converters;

import com.ratsoft.mineraltracker.commands.MineralCommand;
import com.ratsoft.mineraltracker.model.Mineral;
import org.mapstruct.Mapper;

/**
 * Mapper between domain and command.
 *
 * @author mpeter
 */
@Mapper(componentModel = "spring")
public interface MineralMapper {
    /**
     * Convert mineral command to a domain object.
     *
     * @param command the command object.
     * @return the domain object.
     */
    Mineral commandToMineral(MineralCommand command);
    /**
     * Convert mineral domain object to a command.
     *
     * @param destination the domain object.
     * @return the command object.
     */
    MineralCommand mineralToCommand(Mineral destination);
}
