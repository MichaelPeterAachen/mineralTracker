package com.ratsoft.mineraltracker.repositories;

import com.ratsoft.mineraltracker.model.MineralRecommendation;
import org.springframework.data.repository.CrudRepository;

/**
 * The jpa repository for the mineral recommendations.
 *
 * @author mpeter
 */
@SuppressWarnings("InterfaceNeverImplemented")
public interface MineralRecommendationRepository extends CrudRepository<MineralRecommendation, Long> {
}
