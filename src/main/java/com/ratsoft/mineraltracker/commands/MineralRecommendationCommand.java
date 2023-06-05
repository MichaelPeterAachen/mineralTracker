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

    @Nullable
    private MineralCommand mineral;

    @Nullable
    private Long mineralid;

    private float minAmount;
    private float maxAmount;

    @Nullable
    private Unit unit;

    private long timePeriodLength;

    @Nullable
    private RecommendationPeriodType timePeriodDimension;

    /**
     * @return the id of the mineral as a Long. This is required for the web pages.
     */
    @Nullable
    public Long getMineralId() {
        return mineralid;
    }

    /**
     * Set the id of the mineral as a Long. This is required for the web pages.
     *
     * @param id the id of the mineral.
     */
    public void setMineralId(final @NonNull Long id) {
        mineralid = id;
    }
}
