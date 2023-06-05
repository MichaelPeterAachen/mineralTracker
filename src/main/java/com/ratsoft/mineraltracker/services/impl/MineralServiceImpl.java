package com.ratsoft.mineraltracker.services.impl;

import com.ratsoft.mineraltracker.converters.MineralMapper;
import com.ratsoft.mineraltracker.model.Mineral;
import com.ratsoft.mineraltracker.repositories.MineralRepository;
import com.ratsoft.mineraltracker.services.MineralService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.NoSuchElementException;
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
    public Optional<Mineral> getMineral(final Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return mineralRepository.findById(id);
    }

    @Override
    public Mineral saveMineral(final @NonNull Mineral mineral) {
        log.debug("Save or update mineral: {}", mineral);
        return mineralRepository.save(mineral);
    }

    @Override
    public void deleteMineral(final @NonNull Long id) {
        log.debug("Delete mineral: {}", id);
        mineralRepository.deleteById(id);
    }

    @Override
    public void saveImageFile(final @NonNull Long id, final @NonNull Byte @NonNull [] imageBytes) {
        log.info("Save image for mineral {}", id);
        final Optional<Mineral> mineralOptional = mineralRepository.findById(id);
        if (mineralOptional.isEmpty()) {
            throw new NoSuchElementException("No mineral id " + id + " found for saving image to.");
        }

        final Mineral mineral = mineralOptional.get();
        mineral.setImage(imageBytes);

        mineralRepository.save(mineral);
    }
}
