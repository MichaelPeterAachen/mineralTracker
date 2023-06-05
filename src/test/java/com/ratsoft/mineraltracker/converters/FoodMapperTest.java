package com.ratsoft.mineraltracker.converters;

import com.ratsoft.mineraltracker.commands.AmountContainedCommand;
import com.ratsoft.mineraltracker.commands.FoodCommand;
import com.ratsoft.mineraltracker.commands.MineralCommand;
import com.ratsoft.mineraltracker.model.AmountContained;
import com.ratsoft.mineraltracker.model.Food;
import com.ratsoft.mineraltracker.model.Mineral;
import com.ratsoft.mineraltracker.model.Unit;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test the food to command mapping.
 *
 * @author mpeter
 */
@SuppressWarnings({"DataFlowIssue", "MissingJavadoc"})
@ExtendWith(SpringExtension.class)
@NoArgsConstructor
@SpringBootTest
class FoodMapperTest {


    @Autowired
    private @NonNull FoodMapper foodMapper;

    @Test
    void testMappingToCommand() {
        final Mineral mineral = new Mineral(1L, "SELEN", null);
        final AmountContained amountContained = new AmountContained(1L, mineral, 1.0f, Unit.mg, null);
        final Food food = new Food(1L, "Spinat", List.of(amountContained));

        final FoodCommand foodCommand = foodMapper.foodToCommand(food);

        assertThat(foodCommand).usingRecursiveComparison()
                               .ignoringFields("containedMinerals")
                               .isEqualTo(food);
        assertThat(foodCommand.getContainedMinerals()).usingRecursiveFieldByFieldElementComparatorIgnoringFields("doDelete", "food")
                                                      .isEqualTo(foodCommand.getContainedMinerals());
    }

    @SuppressWarnings("NestedMethodCall")
    @Test
    void testMappingfromCommand() {
        final MineralCommand mineralCommand = new MineralCommand(1L, "SELEN", null);
        final AmountContainedCommand amountContainedCommand = new AmountContainedCommand(1L, mineralCommand, 1.0f, Unit.mg, false);
        final List<AmountContainedCommand> amountContainedCommand1 = new ArrayList<>(List.of(amountContainedCommand));
        final FoodCommand foodCommand = new FoodCommand(1L, "Spinat", amountContainedCommand1);

        final Food food = foodMapper.commandToFood(foodCommand);

        assertThat(food).usingRecursiveComparison()
                        .ignoringFields("containedMinerals")
                        .isEqualTo(foodCommand);
        assertThat(food.getContainedMinerals()
                       .size()).isEqualTo(foodCommand.getContainedMinerals()
                                                     .size());
    }

    @Test
    void testMappingToCommandNull() {
        final FoodCommand foodCommand = foodMapper.foodToCommand(null);

        assertThat(foodCommand)
                .isNull();
    }

    @Test
    void testMappingfromCommandNull() {
        final Food food = foodMapper.commandToFood(null);

        assertThat(food)
                .isNull();
    }
}