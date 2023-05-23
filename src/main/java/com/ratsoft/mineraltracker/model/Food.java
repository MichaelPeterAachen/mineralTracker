package com.ratsoft.mineraltracker.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "FOOD_ID", referencedColumnName = "ID")
    private List<AmountContained> containedMinerals = new ArrayList<>(5);
}
