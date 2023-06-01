package com.ratsoft.mineraltracker.converters;

import com.ratsoft.mineraltracker.commands.AmountContainedCommand;
import com.ratsoft.mineraltracker.commands.FoodCommand;
import com.ratsoft.mineraltracker.commands.MineralCommand;
import com.ratsoft.mineraltracker.commands.MineralRecommendationCommand;
import com.ratsoft.mineraltracker.model.Food;
import com.ratsoft.mineraltracker.model.Mineral;
import com.ratsoft.mineraltracker.services.MineralService;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;

import java.util.Optional;

/**
 * Mapper between domain and command.
 *
 * @author mpeter
 */
@Mapper(componentModel = "spring", uses={MineralService.class, MineralMapper.class})
@Slf4j
public abstract class FoodMapper {
    @Autowired
    @Setter
    protected MineralService mineralService;

    @Autowired
    @Setter
    protected MineralMapper mineralMapper;

    /**
     * Convert food command to a domain object.
     *
     * @param command the command object.
     * @return the domain object.
     */
    @Nullable
   public abstract Food commandToFood(@Nullable FoodCommand command);

    /**
     * Convert food contained domain object to a command.
     *
     * @param destination the domain object.
     * @return the command object.
     */
    @Nullable
    public abstract FoodCommand foodToCommand(@Nullable Food destination);

    @BeforeMapping
    public void beforeMappingDo(final FoodCommand command) {
        System.out.println("Before FoodCommand mapping called");
        if (command == null) {
            return;
        }
        command.removeEntryContainments();
        command.removeDeletedContainments();
        validateAndCompleteMinerals(command);
    }

    private void validateAndCompleteMinerals(final @NonNull FoodCommand foodCommand) {
        for (final AmountContainedCommand containedMineral : foodCommand.getContainedMinerals()) {
            final MineralCommand mineralCommand1 = containedMineral.getMineral();
            if (!mineralCommand1.notLoaded()) {
                continue;
            }
            final Long id = mineralCommand1.getId();
            final Optional<Mineral> mineralOptional = mineralService.getMineral(id);
            if (mineralOptional.isEmpty()) {
                log.error("Mineral with id {} not present", id);
                throw new IllegalArgumentException("Mineral with id " + id + " not present");
            }
            final MineralCommand mineralCommand = mineralMapper.mineralToCommand(mineralOptional.get());
            containedMineral.setMineral(mineralCommand);
        }
    }
}
