package com.ratsoft.mineraltracker.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines a food, which contains a given amount of minerals.
 *
 * @author mpeter
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Nullable
    private Long id;

    // TODO: This should be NonNull, but then the mapper is not working, if a food is enhanced with a new amountContained entry.
    // To fix this, the food must be mapped and the minerals must be loaded.
    private @Nullable String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "food", orphanRemoval = true)
    private @NonNull List<AmountContained> containedMinerals = new ArrayList<>(5);

    @SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
    public void setContainedMinerals(final List<AmountContained> containedMinerals) {
        for (final AmountContained containedMineral : containedMinerals) {
            containedMineral.setFood(this);
        }
        this.containedMinerals = containedMinerals;
    }
}
