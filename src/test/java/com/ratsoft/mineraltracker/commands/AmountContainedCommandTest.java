package com.ratsoft.mineraltracker.commands;

import com.ratsoft.mineraltracker.model.Unit;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


@SuppressWarnings({"MissingJavadoc", "NestedMethodCall"})
@NoArgsConstructor
class AmountContainedCommandTest {

    @Test
    void isEmpty() {
        final AmountContainedCommand command = new AmountContainedCommand();

        assertThat(command.isEmpty()).isTrue();

        final AmountContainedCommand command2 = new AmountContainedCommand();
        command2.setMineral(new MineralCommand());

        assertThat(command2.isEmpty()).isTrue();
    }

    @Test
    void isNotEmptyIfId() {
        final AmountContainedCommand command = new AmountContainedCommand();
        command.setId(1L);

        assertThat(command.isEmpty()).isFalse();
    }

    @Test
    void isNotEmptyIfMineral() {
        final AmountContainedCommand command1 = new AmountContainedCommand();
        command1.setMineral(new MineralCommand(1L, "TEST", null));

        assertThat(command1.isEmpty()).isFalse();
    }

    @Test
    void isNotEmptyIfAmount() {
        final AmountContainedCommand command = new AmountContainedCommand();
        command.setAmount(1.0f);

        assertThat(command.isEmpty()).isFalse();
    }

    @Test
    void isNotEmptyIfUnit() {
        final AmountContainedCommand command = new AmountContainedCommand();
        command.setUnit(Unit.mg);

        assertThat(command.isEmpty()).isFalse();
    }

}