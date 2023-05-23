package com.ratsoft.mineraltracker.services;

import com.ratsoft.mineraltracker.commands.MineralCommand;
import com.ratsoft.mineraltracker.model.Mineral;

import java.util.Optional;
import java.util.Set;

/**
 * Interface for the service to access the minerals.
 *
 * @author mpeter
 */
public interface MineralService {
    Set<Mineral> getAllMinerals();

    Optional<Mineral> getMineral(Long id);

    MineralCommand saveMineralCommand(MineralCommand mineralCommand);

    void deleteMineral(Long id);
}
