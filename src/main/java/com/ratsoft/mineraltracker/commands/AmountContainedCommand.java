package com.ratsoft.mineraltracker.commands;

import com.ratsoft.mineraltracker.model.Unit;
import lombok.*;
import org.springframework.lang.Nullable;

/**
 * Defines an command for editing the amount of a mineral being contained in a food.
 *
 * @author mpeter
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AmountContainedCommand {
    @Nullable
    private Long id;

    @Nullable
    private MineralCommand mineral;

    private float amount;

    @Nullable
    private Unit unit;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private boolean doDelete;

    /**
     * @return true if the entry is empty.
     */
    public boolean isEmpty() {
        return id == null && (mineral == null || mineral.getId()== null) && amount == 0.0f && unit==null;
    }
}
