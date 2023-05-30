package com.ratsoft.mineraltracker.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.lang.Nullable;

/**
 * Defines an amount with a given unit. It is used to describe the amount of a mineral being contained in a food.
 *
 * @author mpeter
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AmountContained {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Nullable
    private Long id;

    @ManyToOne
    private @NonNull Mineral mineral;

    private float amount;

    @Enumerated(EnumType.STRING)
    private @NonNull Unit unit;

    @ManyToOne
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Nullable
    private Food food;
}
