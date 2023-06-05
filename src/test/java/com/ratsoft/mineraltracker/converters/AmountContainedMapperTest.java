package com.ratsoft.mineraltracker.converters;

import com.ratsoft.mineraltracker.commands.AmountContainedCommand;
import com.ratsoft.mineraltracker.commands.MineralCommand;
import com.ratsoft.mineraltracker.model.AmountContained;
import com.ratsoft.mineraltracker.model.Mineral;
import com.ratsoft.mineraltracker.model.Unit;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test the amount contained to command mapping.
 *
 * @author mpeter
 */
@SuppressWarnings("MissingJavadoc")
@NoArgsConstructor
class AmountContainedMapperTest {

    @Test
    void testMappingToCommand() {
        final Mineral mineral = new Mineral(1L, "SELEN", null);
        final AmountContained amountContained = new AmountContained(1L, mineral, 1.0f, Unit.mg, null);

        final AmountContainedMapper mapper = Mappers.getMapper(AmountContainedMapper.class);
        final AmountContainedCommand amountContainedCommand = mapper.amountContainedToCommand(amountContained);

        assertThat(amountContainedCommand).usingRecursiveComparison()
                                          .ignoringFields("doDelete")
                                          .isEqualTo(amountContained);
    }

    @Test
    void testMappingfromCommand() {
        final MineralCommand mineralCommand = new MineralCommand(1L, "SELEN", null);
        final AmountContainedCommand amountContainedCommand = new AmountContainedCommand(1L, mineralCommand, 1.0f, Unit.mg, false);

        final AmountContainedMapper mapper = Mappers.getMapper(AmountContainedMapper.class);
        final AmountContained amountContained = mapper.commandToAmountContained(amountContainedCommand);

        assertThat(amountContained).usingRecursiveComparison()
                                   .ignoringFields("food")
                                   .isEqualTo(amountContainedCommand);
    }

    @Test
    void testMappingToCommandNull() {
        final AmountContainedMapper mapper = Mappers.getMapper(AmountContainedMapper.class);
        final AmountContainedCommand amountContainedCommand = mapper.amountContainedToCommand(null);

        assertThat(amountContainedCommand)
                .isNull();
    }

    @Test
    void testMappingfromCommandNull() {
        final AmountContainedMapper mapper = Mappers.getMapper(AmountContainedMapper.class);
        final AmountContained amountContained = mapper.commandToAmountContained(null);

        assertThat(amountContained)
                .isNull();
    }
}