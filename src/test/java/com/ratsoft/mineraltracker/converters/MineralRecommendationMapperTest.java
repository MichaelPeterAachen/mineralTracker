package com.ratsoft.mineraltracker.converters;

import com.ratsoft.mineraltracker.commands.MineralCommand;
import com.ratsoft.mineraltracker.commands.MineralRecommendationCommand;
import com.ratsoft.mineraltracker.model.Mineral;
import com.ratsoft.mineraltracker.model.MineralRecommendation;
import com.ratsoft.mineraltracker.model.RecommendationPeriodType;
import com.ratsoft.mineraltracker.model.Unit;
import lombok.NoArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

/**
 * Test the mineral to command mapping.
 *
 * @author mpeter
 */
@SuppressWarnings({"ProhibitedExceptionDeclared", "DataFlowIssue"})
@NoArgsConstructor
class MineralRecommendationMapperTest {

    @Test
    public void testMappingToCommand() throws Exception {
        final Mineral mineral = new Mineral(2L, "SELEN", null);

        final MineralRecommendation mineralRecommendation = new MineralRecommendation();
        mineralRecommendation.setId(1L);
        mineralRecommendation.setMineral(mineral);
        mineralRecommendation.setMinAmount(0.1f);
        mineralRecommendation.setMaxAmount(9.9f);
        mineralRecommendation.setUnit(Unit.mg);
        mineralRecommendation.setTimePeriodDimension(RecommendationPeriodType.HOURS);
        mineralRecommendation.setTimePeriodLength(3L);


        final MineralRecommendationMapper mapper = Mappers.getMapper(MineralRecommendationMapper.class);

        final MineralRecommendationCommand mineralRecommendationCommand = mapper.mineralRecommendationToCommand(mineralRecommendation);

        Assertions.assertThat(mineralRecommendationCommand.getId())
                  .isEqualTo(mineralRecommendation.getId());
        Assertions.assertThat(mineralRecommendationCommand.getMinAmount())
                  .isEqualTo(mineralRecommendation.getMinAmount());
        Assertions.assertThat(mineralRecommendationCommand.getMaxAmount())
                  .isEqualTo(mineralRecommendation.getMaxAmount());
        Assertions.assertThat(mineralRecommendationCommand.getUnit())
                  .isEqualTo(mineralRecommendation.getUnit());
        Assertions.assertThat(mineralRecommendationCommand.getTimePeriodLength())
                  .isEqualTo(mineralRecommendation.getTimePeriodLength());
        Assertions.assertThat(mineralRecommendationCommand.getTimePeriodDimension())
                  .isEqualTo(mineralRecommendation.getTimePeriodDimension());

        final MineralCommand mineralCommand = mineralRecommendationCommand.getMineral();
        Assertions.assertThat(mineralCommand.getId())
                  .isEqualTo(mineral.getId());
        Assertions.assertThat(mineralCommand.getName())
                  .isEqualTo(mineral.getName());
    }

    @Test
    public void testMappingfromCommand() throws Exception {
        final MineralCommand mineralCommand = new MineralCommand(2L, "SELEN", null);

        final MineralRecommendationCommand mineralRecommendationCommand = new MineralRecommendationCommand();
        mineralRecommendationCommand.setId(1L);
        mineralRecommendationCommand.setMineral(mineralCommand);
        mineralRecommendationCommand.setMinAmount(0.1f);
        mineralRecommendationCommand.setMaxAmount(9.9f);
        mineralRecommendationCommand.setUnit(Unit.mg);
        mineralRecommendationCommand.setTimePeriodDimension(RecommendationPeriodType.HOURS);
        mineralRecommendationCommand.setTimePeriodLength(3L);


        final MineralRecommendationMapper mapper = Mappers.getMapper(MineralRecommendationMapper.class);

        final MineralRecommendation mineralRecommendation = mapper.commandToMineralRecommendation(mineralRecommendationCommand);

        Assertions.assertThat(mineralRecommendation.getId())
                  .isEqualTo(mineralRecommendationCommand.getId());
        Assertions.assertThat(mineralRecommendation.getMinAmount())
                  .isEqualTo(mineralRecommendationCommand.getMinAmount());
        Assertions.assertThat(mineralRecommendation.getMaxAmount())
                  .isEqualTo(mineralRecommendationCommand.getMaxAmount());
        Assertions.assertThat(mineralRecommendation.getUnit())
                  .isEqualTo(mineralRecommendationCommand.getUnit());
        Assertions.assertThat(mineralRecommendation.getTimePeriodLength())
                  .isEqualTo(mineralRecommendationCommand.getTimePeriodLength());
        Assertions.assertThat(mineralRecommendation.getTimePeriodDimension())
                  .isEqualTo(mineralRecommendationCommand.getTimePeriodDimension());

        final Mineral mineral = mineralRecommendation.getMineral();
        Assertions.assertThat(mineral.getId())
                  .isEqualTo(mineralCommand.getId());
        Assertions.assertThat(mineral.getName())
                  .isEqualTo(mineralCommand.getName());
    }

    @Test
    public void testMappingToCommandNull() throws Exception {
        final MineralRecommendationMapper mapper = Mappers.getMapper(MineralRecommendationMapper.class);
        final MineralRecommendationCommand mineralCommand = mapper.mineralRecommendationToCommand(null);

        Assertions.assertThat(mineralCommand)
                  .isNull();
    }

    @Test
    public void testMappingfromCommandNull() throws Exception {
        final MineralRecommendationMapper mapper = Mappers.getMapper(MineralRecommendationMapper.class);
        final MineralRecommendation mineral = mapper.commandToMineralRecommendation(null);

        Assertions.assertThat(mineral)
                  .isNull();
    }
}