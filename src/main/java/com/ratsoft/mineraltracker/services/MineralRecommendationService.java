package com.ratsoft.mineraltracker.services;

import com.ratsoft.mineraltracker.commands.MineralRecommendationCommand;
import com.ratsoft.mineraltracker.model.MineralRecommendation;

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
    Set<MineralRecommendation> getAllMineralRecommendations();

    /**
     * Return a mineral recommendation for a given id.
     *
     * @param id id of the mineral recommendation
     * @return Optional mineral recommendation.
     */
    Optional<MineralRecommendation> getMineralRecommendation(Long id);

    /**
     * Save or update a mineral recommendation in the database.
     *
     * @param mineralCommand the mineral recommendation to save.
     * @return the saved mineral recommendation as a command. In case a new mineral recommendation was saved, the id is contained.
     */
    MineralRecommendationCommand saveMineralRecommendationCommand(MineralRecommendationCommand mineralCommand);

    /**
     * Delete a mineral recommendation.
     *
     * @param id the id of the entitiy to be deleted.
     */
    void deleteMineralRecommendation(Long id);
}
