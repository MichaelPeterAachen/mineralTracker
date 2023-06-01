package com.ratsoft.mineraltracker.commands;

import com.ratsoft.mineraltracker.model.RecommendationPeriodType;
import com.ratsoft.mineraltracker.model.Unit;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.lang.Nullable;


/**
 * The recommended amount of a mineral, a person should eat or dring in a given period of time.
 *
 * @author mpeter
 */
@Data
@NoArgsConstructor
public class MineralRecommendationCommand {
    @Nullable
    private Long id;

    private @Nullable MineralCommand mineral;

    private @Nullable Long mineralid;

    private float minAmount;
    private float maxAmount;

    private @Nullable Unit unit;

    private long timePeriodLength;

    private @Nullable RecommendationPeriodType timePeriodDimension;

    @Nullable
    public Long getMineralId() {
        return mineralid;
    }

    public void setMineralId(final @NonNull Long id) {
        mineralid = id;
    }
}
