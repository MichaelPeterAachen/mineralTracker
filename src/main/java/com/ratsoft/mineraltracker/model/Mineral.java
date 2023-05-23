package com.ratsoft.mineraltracker.model;


import jakarta.persistence.*;
import lombok.*;

/**
 * Defines a mineral.
 *
 * @author mpeter
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mineral {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
}
