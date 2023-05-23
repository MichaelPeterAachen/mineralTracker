package com.ratsoft.mineraltracker.services.impl;

import com.ratsoft.mineraltracker.commands.MineralCommand;
import com.ratsoft.mineraltracker.converters.MineralMapper;
import com.ratsoft.mineraltracker.model.Mineral;
import com.ratsoft.mineraltracker.repositories.MineralRepository;
import com.ratsoft.mineraltracker.services.MineralService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Service to access the minerals.
 *
 * @author mpeter
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MineralServiceImpl implements MineralService {

    private final MineralRepository mineralRepository;

    private final MineralMapper mapper;

    @Override
    public Set<Mineral> getAllMinerals() {
        final Iterable<Mineral> allMinerals = mineralRepository.findAll();
        final Set<Mineral> minerals = new HashSet<>(10);
        for (final Mineral mineral : allMinerals) {
            minerals.add(mineral);
        }
        return minerals;
    }

    @Override
    public Optional<Mineral> getMineral(final Long id) {
        return mineralRepository.findById(id);
    }

    @Override
    public MineralCommand saveMineralCommand(final MineralCommand mineralCommand) {
        log.debug("Save or update mineral: {}",mineralCommand);
        final Mineral mineral = mapper.commandToMineral(mineralCommand);
        final Mineral mineralSaved = mineralRepository.save(mineral);
        return mapper.mineralToCommand(mineralSaved);
    }

    @Override
    public void deleteMineral(final Long id) {
        log.debug("Delete mineral: {}",id);
        mineralRepository.deleteById(id);
    }
}
