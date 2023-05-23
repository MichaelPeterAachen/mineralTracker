package com.ratsoft.mineraltracker.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The recommended amount of a mineral, a person should eat or dring in a given period of time.
 * @author mpeter
 */
@Entity
@NoArgsConstructor
@Data
public class MineralRecommendation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    private Mineral mineral;

    private float minAmount;
    private float maxAmount;

    @Enumerated(value = EnumType.STRING)
    private Unit unit;

    private long timePeriodLength;

    @Enumerated(value = EnumType.STRING)
    private RecommendationPeriodType timePeriodDimension;

}
