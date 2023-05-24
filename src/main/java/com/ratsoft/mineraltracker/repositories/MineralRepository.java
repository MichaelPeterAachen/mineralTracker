package com.ratsoft.mineraltracker.repositories;

import com.ratsoft.mineraltracker.model.Mineral;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * The jpa repository for the minerals.
 *
 * @author mpeter
 */
@SuppressWarnings("InterfaceNeverImplemented")
public interface MineralRepository extends CrudRepository<Mineral, Long> {
    /**
     * Find a mineral by it's name.
     * @param name the mineral name.
     * @return The optional mineral. if found or empty if not.
     */
    Optional<Mineral> findByName(String name);
}
