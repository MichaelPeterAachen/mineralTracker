package com.ratsoft.mineraltracker.converters;

import com.ratsoft.mineraltracker.commands.AmountContainedCommand;
import com.ratsoft.mineraltracker.model.AmountContained;
import org.mapstruct.Mapper;
import org.springframework.lang.Nullable;

/**
 * Mapper between domain and command.
 *
 * @author mpeter
 */
@Mapper(componentModel = "spring")
public interface AmountContainedMapper {
    /**
     * Convert amount contained command to a domain object.
     *
     * @param command the command object.
     * @return the domain object.
     */
    @Nullable AmountContained commandToAmountContained(@Nullable AmountContainedCommand command);

    /**
     * Convert amount contained domain object to a command.
     *
     * @param destination the domain object.
     * @return the command object.
     */
    @Nullable AmountContainedCommand amountContainedToCommand(@Nullable AmountContained destination);
}
