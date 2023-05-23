package com.ratsoft.mineraltracker.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Defines an amount with a given unit. It is used to describe the amount of a mineral being contained in a food.
 *
 * @author mpeter
 */
@Entity
@Data
public class AmountContained {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Mineral mineral;

    private float amount;

    @Enumerated(value = EnumType.STRING)
    private Unit unit;
}
