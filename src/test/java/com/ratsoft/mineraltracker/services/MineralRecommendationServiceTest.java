package com.ratsoft.mineraltracker.services;

import com.ratsoft.mineraltracker.commands.MineralCommand;
import com.ratsoft.mineraltracker.commands.MineralRecommendationCommand;
import com.ratsoft.mineraltracker.converters.MineralMapper;
import com.ratsoft.mineraltracker.converters.MineralRecommendationMapper;
import com.ratsoft.mineraltracker.model.Mineral;
import com.ratsoft.mineraltracker.model.MineralRecommendation;
import com.ratsoft.mineraltracker.model.RecommendationPeriodType;
import com.ratsoft.mineraltracker.model.Unit;
import com.ratsoft.mineraltracker.repositories.MineralRecommendationRepository;
import com.ratsoft.mineraltracker.services.impl.MineralRecommendationServiceImpl;
import com.ratsoft.mineraltracker.services.impl.MineralServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Tests for the MineralRecommendationService.
 *
 * @author mpeter
 */
class MineralRecommendationServiceTest {
    @Mock
    private MineralRecommendationRepository mineralRecommendationRepository;

    private MineralRecommendationService mineralRecommendationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        final MineralRecommendationMapper mapper = Mappers.getMapper(MineralRecommendationMapper.class);

        mineralRecommendationService = new MineralRecommendationServiceImpl(mineralRecommendationRepository, mapper);
    }

    @Test
    public void getAllMineralRecommendations() {
        final Mineral mineralCommand1 = new Mineral(1L, "Eisen");
        final Mineral mineralCommand2 = new Mineral(2L, "Selen");
        final Mineral mineralCommand3 = new Mineral(3L, "Mangan");

        final MineralRecommendation mineralRecommendation1 = buildMineralRecommendation(mineralCommand1, 1, Unit.mg, RecommendationPeriodType.HOURS);
        final MineralRecommendation mineralRecommendation2 = buildMineralRecommendation(mineralCommand2, 2, Unit.g, RecommendationPeriodType.DAYS);
        final MineralRecommendation mineralRecommendation3 = buildMineralRecommendation(mineralCommand3, 3, Unit.µg, RecommendationPeriodType.WEEKS);

        final List<MineralRecommendation> mineralRecommendations = List.of(mineralRecommendation1, mineralRecommendation2, mineralRecommendation3);

        when(mineralRecommendationRepository.findAll()).thenReturn(mineralRecommendations);

        final Set<MineralRecommendation> allMineralRecommendations = mineralRecommendationService.getAllMineralRecommendations();

        assertThat(allMineralRecommendations).containsExactlyInAnyOrder(mineralRecommendation1, mineralRecommendation2, mineralRecommendation3);
    }

    @Test
    public void getMineralRecommendationPresent() {
        final Mineral mineralCommand = new Mineral(2L, "Selen");
        final MineralRecommendation mineralRecommendationExpected = buildMineralRecommendation(mineralCommand, 2, Unit.g, RecommendationPeriodType.DAYS);

        when(mineralRecommendationRepository.findById(2L)).thenReturn(Optional.of(mineralRecommendationExpected));

        final Optional<MineralRecommendation> mineralRecommendation = mineralRecommendationService.getMineralRecommendation(2L);

        assertThat(mineralRecommendation).isPresent();
        assertThat(mineralRecommendation.get()).isEqualTo(mineralRecommendationExpected);
    }

    @Test
    public void getMineralNotPresent() {
        when(mineralRecommendationRepository.findById(4L)).thenReturn(Optional.empty());

        final Optional<MineralRecommendation> mineralResult = mineralRecommendationService.getMineralRecommendation(4L);

        assertThat(mineralResult).isEmpty();
    }

    @Test
    public void saveMineral() {
        // Given
        final Mineral mineral = new Mineral(2L, "Selen");

        final MineralRecommendation mineralRecommendation = buildMineralRecommendation(mineral, 1L, Unit.g, RecommendationPeriodType.DAYS);
        mineralRecommendation.setId(null);

        final MineralRecommendation mineralRecommendationSaved = buildMineralRecommendation(mineral, 1L, Unit.g, RecommendationPeriodType.DAYS);

        when(mineralRecommendationRepository.save(mineralRecommendation)).thenReturn(mineralRecommendationSaved);

        // When
        MineralCommand mineralCommand = new MineralCommand(2L, "Selen");

        final MineralRecommendationCommand mineralRecommendationCommandInput = buildMineralRecommendationCommand(mineralCommand, 1L, Unit.g, RecommendationPeriodType.DAYS);
        mineralRecommendationCommandInput.setId(null);

        final MineralRecommendationCommand mineralRecommendationReturned = mineralRecommendationService.saveMineralRecommendationCommand(mineralRecommendationCommandInput);

        // Then
        final MineralRecommendationCommand mineralCommandNewExpected = buildMineralRecommendationCommand(mineralCommand, 1L, Unit.g, RecommendationPeriodType.DAYS);

        assertThat(mineralRecommendationReturned.getId()).isEqualTo(2L);
        assertThat(mineralRecommendationReturned).isEqualTo(mineralCommandNewExpected);
    }

    @Test
    public void deleteMineral() {
        mineralRecommendationService.deleteMineralRecommendation(2L);
        verify(mineralRecommendationRepository, times(1) ).deleteById(2L);
    }

    private static MineralRecommendation buildMineralRecommendation(Mineral mineral, long no, Unit unit, RecommendationPeriodType periodType) {
        final MineralRecommendation mineralRecommendation1 = new MineralRecommendation();
        mineralRecommendation1.setId(1L+ no);
        mineralRecommendation1.setMineral(mineral);
        mineralRecommendation1.setMinAmount(0.0f+ no /10);
        mineralRecommendation1.setMaxAmount(9.0f+ no /10);
        mineralRecommendation1.setUnit(unit);
        mineralRecommendation1.setTimePeriodDimension(periodType);
        mineralRecommendation1.setTimePeriodLength(no);
        return mineralRecommendation1;
    }

    private static MineralRecommendationCommand buildMineralRecommendationCommand(MineralCommand mineralCommand, long no, Unit unit, RecommendationPeriodType periodType) {
        final MineralRecommendationCommand mineralRecommendation1 = new MineralRecommendationCommand();
        mineralRecommendation1.setId(1L+ no);
        mineralRecommendation1.setMineral(mineralCommand);
        mineralRecommendation1.setMinAmount(0.0f+ no /10);
        mineralRecommendation1.setMaxAmount(9.0f+ no /10);
        mineralRecommendation1.setUnit(unit);
        mineralRecommendation1.setTimePeriodDimension(periodType);
        mineralRecommendation1.setTimePeriodLength(no);
        return mineralRecommendation1;
    }
}