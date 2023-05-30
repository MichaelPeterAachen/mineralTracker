package com.ratsoft.mineraltracker.commands;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Command for editing or creating a food, which contains a given amount of minerals.
 *
 * @author mpeter
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodCommand {
    @Nullable
    private Long id;

    @Nullable
    // TODO: This may be null, if the id is set, this is because in the ui only the id is set, not the name.
    private String name;

    @Nullable
    private List<AmountContainedCommand> containedMinerals = new ArrayList<>(5);

    /**
     * Remove all contained minerals entries, which are empty.
     */
    public void removeEntryContainments() {
        if (containedMinerals == null) {
            return;
        }
        containedMinerals.removeIf(AmountContainedCommand::isEmpty);
    }

    /**
     * Remove all entried, for which the do delete flag is true.
     */
    public void removeDeletedContainments() {
        if (containedMinerals == null) {
            return;
        }
        containedMinerals.removeIf(AmountContainedCommand::isDoDelete);
    }
}
