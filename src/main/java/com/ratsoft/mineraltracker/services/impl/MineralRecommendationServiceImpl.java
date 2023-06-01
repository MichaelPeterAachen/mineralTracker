package com.ratsoft.mineraltracker.services.impl;

import com.ratsoft.mineraltracker.converters.MineralRecommendationMapper;
import com.ratsoft.mineraltracker.model.Mineral;
import com.ratsoft.mineraltracker.model.MineralRecommendation;
import com.ratsoft.mineraltracker.repositories.MineralRecommendationRepository;
import com.ratsoft.mineraltracker.services.MineralRecommendationService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Service to access the mineral recommendations.
 *
 * @author mpeter
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MineralRecommendationServiceImpl implements MineralRecommendationService {

    private final @NonNull MineralRecommendationRepository mineralRecommendationRepository;

    private final @NonNull MineralRecommendationMapper mapper;


    @Override
    public Set<MineralRecommendation> getAllMineralRecommendations() {
        final Iterable<MineralRecommendation> allMinerals = mineralRecommendationRepository.findAll();
        final Set<MineralRecommendation> minerals = new HashSet<>(10);
        for (final MineralRecommendation mineral : allMinerals) {
            minerals.add(mineral);
        }
        return minerals;
    }

    @Override
    public Optional<MineralRecommendation> getMineralRecommendation(final @NonNull Long id) {
        return mineralRecommendationRepository.findById(id);
    }

    @Override
    public MineralRecommendation saveMineralRecommendation(final @NonNull MineralRecommendation mineralRecommendation) {
        log.debug("Save or update mineral recommendation command: {}", mineralRecommendation);
        return mineralRecommendationRepository.save(mineralRecommendation);
    }


    @Override
    public void deleteMineralRecommendation(final @NonNull Long id) {
        log.debug("Delete mineral recommendation: {}", id);
        mineralRecommendationRepository.deleteById(id);
    }

    @Override
    public Set<Mineral> getMineralsAlreadyUsed() {
        final Iterable<MineralRecommendation> allMinerals = mineralRecommendationRepository.findAll();
        final Set<Mineral> minerals = new HashSet<>(10);
        for (final MineralRecommendation recommendation : allMinerals) {
            minerals.add(recommendation.getMineral());
        }
        return minerals;

    }

    @Override
    public Optional<MineralRecommendation> findRecommendationForMineral(final @NonNull String mineral) {
        final Iterable<MineralRecommendation> allMinerals = mineralRecommendationRepository.findAll();
        for (final MineralRecommendation recommendation : allMinerals) {
            if (recommendation.isForMineral(mineral)) {
                return Optional.of(recommendation);
            }
        }
        return Optional.empty();
    }
}
