package com.ratsoft.mineraltracker.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.lang.Nullable;

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
    @Nullable
    private Long id;

    private @NonNull String name;

    @Nullable
    private byte[] image;
}
