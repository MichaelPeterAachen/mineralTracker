package com.ratsoft.mineraltracker.services;

import com.ratsoft.mineraltracker.model.Mineral;
import lombok.NonNull;

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
    @NonNull Set<Mineral> getAllMinerals();

    /**
     * Return a mineral for a given id.
     *
     * @param id id of the mineral
     * @return Optional mineral.
     */
    @NonNull Optional<Mineral> getMineral(@NonNull Long id);

    /**
     * Save or update a mineral in the database.
     *
     * @param mineral the mineral to save.
     * @return the saved mineral. In case a new mineral was saved, the id is contained.
     */
    @NonNull Mineral saveMineral(@NonNull Mineral mineral);

    /**
     * Delete a mineral.
     *
     * @param id the id of the entitiy to be deleted.
     */
    void deleteMineral(@NonNull Long id);

    /**
     * Save an image for a mineral.
     *
     * @param id         the id of the image.
     * @param imageBytes the image file to be added.
     */
    void saveImageFile(@NonNull Long id, @NonNull Byte @NonNull [] imageBytes);
}
