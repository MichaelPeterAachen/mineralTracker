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
    /**
     * Return a list of all minerals.
     *
     * @return all minerals in the database.
     */
    Set<Mineral> getAllMinerals();

    /**
     * Return a mineral for a given id.
     *
     * @param id id of the mineral
     * @return Optional mineral.
     */
    Optional<Mineral> getMineral(Long id);

    /**
     * Save or update a mineral in the database.
     *
     * @param mineralCommand the mineral to save.
     * @return the saved mineral as a command. In case a new mineral was saved, the id is contained.
     */
    MineralCommand saveMineralCommand(MineralCommand mineralCommand);

    /**
     * Delete a mineral.
     *
     * @param id the id of the entitiy to be deleted.
     */
    void deleteMineral(Long id);
}
