package com.ratsoft.mineraltracker.converters;

import com.ratsoft.mineraltracker.commands.AmountContainedCommand;
import com.ratsoft.mineraltracker.commands.FoodCommand;
import com.ratsoft.mineraltracker.commands.MineralCommand;
import com.ratsoft.mineraltracker.model.Food;
import com.ratsoft.mineraltracker.model.Mineral;
import com.ratsoft.mineraltracker.services.MineralService;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;

import java.util.Optional;

/**
 * Mapper between domain and command.
 *
 * @author mpeter
 */
@Mapper(componentModel = "spring", uses = {MineralService.class, MineralMapper.class})
@Slf4j
@NoArgsConstructor
public abstract class FoodMapper {
    /**
     * Mineral Service for resolving the minerals when mapping the food.
     */
    @SuppressWarnings("FieldHasSetterButNoGetter")
    @Autowired
    @Setter
    protected @NonNull MineralService mineralService;

    /**
     * Mineral mapper for mapping the minerals when mapping the food.
     */
    @SuppressWarnings("FieldHasSetterButNoGetter")
    @Autowired
    @Setter
    protected @NonNull MineralMapper mineralMapper;

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

    /**
     * Apply cleanup and resolve minerals when mapping from command to domain object.
     *
     * @param command the command object.
     */
    @BeforeMapping
    public void beforeMappingDo(@Nullable final FoodCommand command) {
        if (command == null) {
            return;
        }
        command.removeEntryContainments();
        command.removeDeletedContainments();
        validateAndCompleteMinerals(command);
    }

    private void validateAndCompleteMinerals(final @NonNull FoodCommand foodCommand) {
        //noinspection DataFlowIssue
        for (final AmountContainedCommand containedMineral : foodCommand.getContainedMinerals()) {
            final MineralCommand mineralCommand1 = containedMineral.getMineral();
            if (mineralCommand1 == null || !mineralCommand1.notLoaded()) {
                continue;
            }
            final Long id = mineralCommand1.getId();
            //noinspection DataFlowIssue
            final Optional<Mineral> mineralOptional = mineralService.getMineral(id);
            if (mineralOptional.isEmpty()) {
                log.error("Mineral with id {} not present", id);
                throw new IllegalArgumentException("Mineral with id " + id + " not present");
            }
            //noinspection NestedMethodCall
            final MineralCommand mineralCommand = mineralMapper.mineralToCommand(mineralOptional.get());
            containedMineral.setMineral(mineralCommand);
        }
    }
}
