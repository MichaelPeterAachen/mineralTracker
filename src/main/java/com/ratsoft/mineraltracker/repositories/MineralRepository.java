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
    Optional<Mineral> findByName(String name);
}
