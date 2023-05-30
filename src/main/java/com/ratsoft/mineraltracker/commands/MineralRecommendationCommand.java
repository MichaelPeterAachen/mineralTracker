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

    private @NonNull MineralCommand mineral;

    private @NonNull Long mineralid;

    private float minAmount;
    private float maxAmount;

    private @NonNull Unit unit;

    private long timePeriodLength;

    private @NonNull RecommendationPeriodType timePeriodDimension;

    @Nullable
    public Long getMineralId() {
        return mineral.getId();
    }

    public void setMineralId(final @NonNull Long id) {
        mineralid = id;
    }
}
