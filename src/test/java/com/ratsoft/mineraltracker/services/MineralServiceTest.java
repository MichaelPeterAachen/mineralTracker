package com.ratsoft.mineraltracker.services;

import com.ratsoft.mineraltracker.commands.MineralCommand;
import com.ratsoft.mineraltracker.converters.MineralMapper;
import com.ratsoft.mineraltracker.model.Mineral;
import com.ratsoft.mineraltracker.repositories.MineralRepository;
import com.ratsoft.mineraltracker.services.impl.MineralServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class MineralServiceTest {
    @Mock
    private MineralRepository mineralRepository;

    private MineralService mineralService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        MineralMapper mapper = Mappers.getMapper(MineralMapper.class);

        mineralService = new MineralServiceImpl(mineralRepository, mapper);
    }

    @Test
    public void getAllMinerals() {
        final Mineral mineral1 = new Mineral (1L, "IRON1");
        final Mineral mineral2 = new Mineral (2L, "SELEN1");

        final List<Mineral> mineralList = List.of(mineral1, mineral2);

        when(mineralRepository.findAll()).thenReturn(mineralList);

        final Set<Mineral> mineralResult = mineralService.getAllMinerals();

        assertThat(mineralResult).containsExactlyInAnyOrder(mineral1, mineral2);
    }

    @Test
    public void getMineralPresent() {
        final Mineral mineral2 = new Mineral (2L, "SELEN2");

        when(mineralRepository.findById(2L)).thenReturn(Optional.of(mineral2));

        final Optional<Mineral> mineralResult = mineralService.getMineral(2L);

        assertThat(mineralResult).isPresent().contains(mineral2);
    }

    @Test
    public void getMineralNotPresent() {
        when(mineralRepository.findById(4L)).thenReturn(Optional.empty());

        final Optional<Mineral> mineralResult = mineralService.getMineral(4L);

        assertThat(mineralResult).isEmpty();
    }

    @Test
    public void saveMineral() {
        final Mineral mineralNew = new Mineral ();
        mineralNew.setName("NEWMINERAL");

        final Mineral mineralNewSaved = new Mineral (3L, "NEWMINERAL");

        when(mineralRepository.save(mineralNew)).thenReturn(mineralNewSaved);

        final MineralCommand mineralCommandNew = new MineralCommand ();
        mineralCommandNew.setName("NEWMINERAL");

        MineralCommand mineralCommandNewSaved = mineralService.saveMineralCommand(mineralCommandNew);

        assertThat(mineralCommandNewSaved.getId()).isEqualTo(3L);
        assertThat(mineralCommandNewSaved.getName()).isEqualTo("NEWMINERAL");
    }

    @Test
    public void deleteMineral() {
        mineralService.deleteMineral(2L);
        verify(mineralRepository, times(1) ).deleteById(2L);
    }
}