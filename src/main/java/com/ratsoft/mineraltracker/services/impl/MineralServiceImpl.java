package com.ratsoft.mineraltracker.services.impl;

import com.ratsoft.mineraltracker.commands.MineralCommand;
import com.ratsoft.mineraltracker.converters.MineralMapper;
import com.ratsoft.mineraltracker.model.Mineral;
import com.ratsoft.mineraltracker.repositories.MineralRepository;
import com.ratsoft.mineraltracker.services.MineralService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    private final @NonNull MineralRepository mineralRepository;

    private final @NonNull MineralMapper mapper;

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
    public Optional<Mineral> getMineral(@NonNull final Long id) {
        return mineralRepository.findById(id);
    }

    @Override
    public MineralCommand saveMineralCommand(@NonNull final MineralCommand mineralCommand) {
        log.debug("Save or update mineral: {}",mineralCommand);
        final Mineral mineral = mapper.commandToMineral(mineralCommand);
        final Mineral mineralSaved = mineralRepository.save(mineral);
        return mapper.mineralToCommand(mineralSaved);
    }

    @Override
    public void deleteMineral(@NonNull final Long id) {
        log.debug("Delete mineral: {}",id);
        mineralRepository.deleteById(id);
    }

    @Override
    public void saveImageFile(@NonNull final Long id, @NonNull final MultipartFile file) {
        log.info("Save image for mineral {}",id);

    }
}
