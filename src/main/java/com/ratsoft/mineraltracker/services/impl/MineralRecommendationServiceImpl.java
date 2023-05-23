package com.ratsoft.mineraltracker.services.impl;

import com.ratsoft.mineraltracker.commands.MineralRecommendationCommand;
import com.ratsoft.mineraltracker.converters.MineralRecommendationMapper;
import com.ratsoft.mineraltracker.model.MineralRecommendation;
import com.ratsoft.mineraltracker.repositories.MineralRecommendationRepository;
import com.ratsoft.mineraltracker.services.MineralRecommendationService;
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

    private final MineralRecommendationRepository mineralRecommendationRepository;

    private final MineralRecommendationMapper mapper;


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
    public Optional<MineralRecommendation> getMineralRecommendation(final Long id) {
        return mineralRecommendationRepository.findById(id);
    }

    @Override
    public MineralRecommendationCommand saveMineralRecommendationCommand(final MineralRecommendationCommand mineralCommand) {
        log.debug("Save or update mineral recommendation: {}", mineralCommand);
        final MineralRecommendation mineral = mapper.commandToMineralRecommendation(mineralCommand);
        final MineralRecommendation mineralSaved = mineralRecommendationRepository.save(mineral);
        return mapper.mineralRecommendationToCommand(mineralSaved);
    }


    @Override
    public void deleteMineralRecommendation(final Long id) {
        log.debug("Delete mineral recommendation: {}", id);
        mineralRecommendationRepository.deleteById(id);
    }
}
