package com.ratsoft.mineraltracker.converters;

import com.ratsoft.mineraltracker.commands.AmountContainedCommand;
import com.ratsoft.mineraltracker.commands.FoodCommand;
import com.ratsoft.mineraltracker.commands.MineralCommand;
import com.ratsoft.mineraltracker.model.AmountContained;
import com.ratsoft.mineraltracker.model.Food;
import com.ratsoft.mineraltracker.model.Mineral;
import com.ratsoft.mineraltracker.model.Unit;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test the food to command mapping.
 *
 * @author mpeter
 */
@SuppressWarnings({"ProhibitedExceptionDeclared", "DataFlowIssue"})
@NoArgsConstructor
class FoodMapperTest {

    @Test
    public void testMappingToCommand() throws Exception {
        final Mineral mineral = new Mineral(1L, "SELEN", null);
        final AmountContained amountContained = new AmountContained(1L, mineral, 1.0f, Unit.mg, null);
        final Food food = new Food(1L, "Spinat", List.of(amountContained));

        final FoodMapper mapper = Mappers.getMapper(FoodMapper.class);
        final FoodCommand foodCommand = mapper.foodToCommand(food);

        assertThat(foodCommand).usingRecursiveComparison()
                               .ignoringFields("containedMinerals")
                               .isEqualTo(food);
        assertThat(foodCommand.getContainedMinerals()).usingRecursiveFieldByFieldElementComparatorIgnoringFields("doDelete", "food")
                                                      .isEqualTo(foodCommand.getContainedMinerals());
    }

    @Test
    public void testMappingfromCommand() throws Exception {
        final MineralCommand mineralCommand = new MineralCommand(1L, "SELEN", null);
        final AmountContainedCommand amountContainedCommand = new AmountContainedCommand(1L, mineralCommand, 1.0f, Unit.mg, false);
        final FoodCommand foodCommand = new FoodCommand(1L, "Spinat", List.of(amountContainedCommand));

        final FoodMapper mapper = Mappers.getMapper(FoodMapper.class);
        final Food food = mapper.commandToFood(foodCommand);

        assertThat(food).usingRecursiveComparison()
                        .ignoringFields("containedMinerals")
                        .isEqualTo(foodCommand);
        assertThat(food.getContainedMinerals()
                       .size()).isEqualTo(foodCommand.getContainedMinerals()
                                                     .size());
    }

    @Test
    public void testMappingToCommandNull() throws Exception {
        final FoodMapper mapper = Mappers.getMapper(FoodMapper.class);
        final FoodCommand foodCommand = mapper.foodToCommand(null);

        assertThat(foodCommand)
                .isNull();
    }

    @Test
    public void testMappingfromCommandNull() throws Exception {
        final FoodMapper mapper = Mappers.getMapper(FoodMapper.class);
        final Food food = mapper.commandToFood(null);

        assertThat(food)
                .isNull();
    }
}