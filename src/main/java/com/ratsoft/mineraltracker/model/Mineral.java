package com.ratsoft.mineraltracker.model;


import jakarta.persistence.*;
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
    @Lob
    private Byte[] image;
}
