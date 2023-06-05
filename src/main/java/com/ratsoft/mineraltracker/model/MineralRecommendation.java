package com.ratsoft.mineraltracker.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.lang.Nullable;

/**
 * The recommended amount of a mineral, a person should eat or dring in a given period of time.
 *
 * @author mpeter
 */
@Entity
@NoArgsConstructor
@Data
public class MineralRecommendation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Nullable
    private Long id;

    @OneToOne
    @JoinColumn(unique = true)
    private @NonNull Mineral mineral;

    private float minAmount;
    private float maxAmount;

    @Enumerated(EnumType.STRING)
    private @NonNull Unit unit;

    private long timePeriodLength;

    @Enumerated(EnumType.STRING)
    private @NonNull RecommendationPeriodType timePeriodDimension;

    /**
     * Return if the input name is equal the mineral name.
     *
     * @param mineralName the name of the mineral.
     * @return true if name is equal minerals name attribute.
     */
    public boolean isForMineral(final @NonNull String mineralName) {
        final String name = mineral.getName();
        return name.equals(mineralName);
    }

}
