package com.ratsoft.mineraltracker.commands;

import com.ratsoft.mineraltracker.model.Mineral;
import com.ratsoft.mineraltracker.model.RecommendationPeriodType;
import com.ratsoft.mineraltracker.model.Unit;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * The recommended amount of a mineral, a person should eat or dring in a given period of time.
 * @author mpeter
 */
@Data
@NoArgsConstructor
public class MineralRecommendationCommand {
    private Long id;

    private MineralCommand mineral;

    private float minAmount;
    private float maxAmount;

    private Unit unit;

    private long timePeriodLength;

    private RecommendationPeriodType timePeriodDimension;

}
