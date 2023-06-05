package com.ratsoft.mineraltracker.services;

import com.ratsoft.mineraltracker.model.Mineral;
import com.ratsoft.mineraltracker.model.MineralRecommendation;
import lombok.NonNull;

import java.util.Optional;
import java.util.Set;

/**
 * Interface for the service to access the mineral recommendations.
 *
 * @author mpeter
 */
public interface MineralRecommendationService {
    /**
     * Return a list of all mineral recommendations.
     *
     * @return all mineral recommendations in the database.
     */
    @NonNull Set<MineralRecommendation> getAllMineralRecommendations();

    /**
     * Return a mineral recommendation for a given id.
     *
     * @param id id of the mineral recommendation
     * @return Optional mineral recommendation.
     */
    @NonNull Optional<MineralRecommendation> getMineralRecommendation(@NonNull Long id);

    /**
     * Save or update a mineral recommendation in the database.
     *
     * @param mineralRecommendation the mineral recommendation to save.
     * @return the saved mineral recommendation. In case a new mineral recommendation was saved, the id is contained.
     */
    @NonNull MineralRecommendation saveMineralRecommendation(@NonNull MineralRecommendation mineralRecommendation);

    /**
     * Delete a mineral recommendation.
     *
     * @param id the id of the entitiy to be deleted.
     */
    void deleteMineralRecommendation(@NonNull Long id);

    /**
     * Query all minerals, for which already a recommendation exists. There can only be one recommendation for each mineral.
     *
     * @return Set of all minerals, being used.
     */
    @NonNull Set<Mineral> getMineralsAlreadyUsed();

    /**
     * Find the recommendation for a given mineral.
     *
     * @param mineral the mineral of interest.
     * @return the recommendation for this mineral if present.
     */
    @NonNull Optional<MineralRecommendation> findRecommendationForMineral(@NonNull String mineral);
}
