package com.ratsoft.mineraltracker.converters;

import com.ratsoft.mineraltracker.commands.MineralCommand;
import com.ratsoft.mineraltracker.model.Mineral;
import lombok.NoArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

/**
 * Test the mineral to command mapping.
 *
 * @author mpeter
 */
@SuppressWarnings({"DataFlowIssue", "MissingJavadoc"})
@NoArgsConstructor
class MineralMapperTest {

    @Test
    void testMappingToCommand() {
        final Mineral mineral = new Mineral(1L, "SELEN", null);

        final MineralMapper mapper = Mappers.getMapper(MineralMapper.class);
        final MineralCommand mineralCommand = mapper.mineralToCommand(mineral);

        Assertions.assertThat(mineralCommand.getId())
                  .isEqualTo(mineral.getId());
        Assertions.assertThat(mineralCommand.getName())
                  .isEqualTo(mineral.getName());
    }

    @Test
    void testMappingfromCommand() {
        final MineralCommand mineralCommand = new MineralCommand(1L, "SELEN", null);

        final MineralMapper mapper = Mappers.getMapper(MineralMapper.class);
        final Mineral mineral = mapper.commandToMineral(mineralCommand);

        Assertions.assertThat(mineral.getId())
                  .isEqualTo(mineralCommand.getId());
        Assertions.assertThat(mineral.getName())
                  .isEqualTo(mineralCommand.getName());
    }

    @Test
    void testMappingToCommandNull() {
        final MineralMapper mapper = Mappers.getMapper(MineralMapper.class);
        final MineralCommand mineralCommand = mapper.mineralToCommand(null);

        Assertions.assertThat(mineralCommand)
                  .isNull();
    }

    @Test
    void testMappingfromCommandNull() {
        final MineralMapper mapper = Mappers.getMapper(MineralMapper.class);
        final Mineral mineral = mapper.commandToMineral(null);

        Assertions.assertThat(mineral)
                  .isNull();
    }
}