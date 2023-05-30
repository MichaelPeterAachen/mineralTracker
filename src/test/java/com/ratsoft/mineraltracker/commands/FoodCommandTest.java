package com.ratsoft.mineraltracker.commands;

import com.ratsoft.mineraltracker.model.Unit;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@NoArgsConstructor
class FoodCommandTest {

    @Test
    void cleanupEmptyCommands() {
        final List<AmountContainedCommand> amountContainedCommands = new ArrayList<>(3);
        amountContainedCommands.add(new AmountContainedCommand());
        amountContainedCommands.add(new AmountContainedCommand());
        amountContainedCommands.add(new AmountContainedCommand());

        final FoodCommand command = new FoodCommand(1L, "Spinat", amountContainedCommands);

        command.removeEntryContainments();

        assertThat(command.getContainedMinerals()).isEmpty();
    }

    @Test
    void cleanupNotEmptyCommands() {
        final AmountContainedCommand containedCommand = new AmountContainedCommand(1L, new MineralCommand(1L, "SELEN", null), 2.0f, Unit.mg, false);

        final List<AmountContainedCommand> amountContainedCommands = new ArrayList<>(3);
        amountContainedCommands.add(new AmountContainedCommand());
        amountContainedCommands.add(containedCommand);
        amountContainedCommands.add(new AmountContainedCommand());

        final FoodCommand command = new FoodCommand(1L, "Spinat", amountContainedCommands);
        command.removeEntryContainments();

        assertThat(command.getContainedMinerals()).hasSize(1);
    }
}