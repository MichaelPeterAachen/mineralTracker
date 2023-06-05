package com.ratsoft.mineraltracker.services;

import com.ratsoft.mineraltracker.commands.MineralCommand;
import com.ratsoft.mineraltracker.commands.MineralRecommendationCommand;
import com.ratsoft.mineraltracker.converters.MineralRecommendationMapper;
import com.ratsoft.mineraltracker.model.Mineral;
import com.ratsoft.mineraltracker.model.MineralRecommendation;
import com.ratsoft.mineraltracker.model.RecommendationPeriodType;
import com.ratsoft.mineraltracker.model.Unit;
import com.ratsoft.mineraltracker.repositories.MineralRecommendationRepository;
import com.ratsoft.mineraltracker.services.impl.MineralRecommendationServiceImpl;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
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
@SuppressWarnings({"MissingJavadoc", "NestedMethodCall"})
@NoArgsConstructor
class MineralRecommendationServiceTest {
    @Mock
    private @NonNull MineralRecommendationRepository mineralRecommendationRepository;

    private @NonNull MineralRecommendationService mineralRecommendationService;

    @BeforeEach
    void setUp() {
        //noinspection deprecation
        MockitoAnnotations.initMocks(this);
        final MineralRecommendationMapper mapper = Mappers.getMapper(MineralRecommendationMapper.class);

        mineralRecommendationService = new MineralRecommendationServiceImpl(mineralRecommendationRepository, mapper);
    }

    @Test
    void getAllMineralRecommendations() {
        final Mineral mineralCommand1 = new Mineral(1L, "Eisen", null);
        final Mineral mineralCommand2 = new Mineral(2L, "Selen", null);
        final Mineral mineralCommand3 = new Mineral(3L, "Mangan", null);

        final MineralRecommendation mineralRecommendation1 = buildMineralRecommendation(mineralCommand1, 1, Unit.mg, RecommendationPeriodType.HOURS);
        final MineralRecommendation mineralRecommendation2 = buildMineralRecommendation(mineralCommand2, 2, Unit.g, RecommendationPeriodType.DAYS);
        //noinspection NonAsciiCharacters
        final MineralRecommendation mineralRecommendation3 = buildMineralRecommendation(mineralCommand3, 3, Unit.µg, RecommendationPeriodType.WEEKS);

        final List<MineralRecommendation> mineralRecommendations = List.of(mineralRecommendation1, mineralRecommendation2, mineralRecommendation3);

        when(mineralRecommendationRepository.findAll()).thenReturn(mineralRecommendations);

        final Set<MineralRecommendation> allMineralRecommendations = mineralRecommendationService.getAllMineralRecommendations();

        assertThat(allMineralRecommendations).containsExactlyInAnyOrder(mineralRecommendation1, mineralRecommendation2, mineralRecommendation3);
    }

    @Test
    void getMineralRecommendationPresent() {
        final Mineral mineralCommand = new Mineral(2L, "Selen", null);
        final MineralRecommendation mineralRecommendationExpected = buildMineralRecommendation(mineralCommand, 2, Unit.g, RecommendationPeriodType.DAYS);

        when(mineralRecommendationRepository.findById(2L)).thenReturn(Optional.of(mineralRecommendationExpected));

        final Optional<MineralRecommendation> mineralRecommendation = mineralRecommendationService.getMineralRecommendation(2L);

        assertThat(mineralRecommendation).isPresent();
        assertThat(mineralRecommendation.get()).isEqualTo(mineralRecommendationExpected);
    }

    @Test
    void getMineralNotPresent() {
        when(mineralRecommendationRepository.findById(4L)).thenReturn(Optional.empty());

        final Optional<MineralRecommendation> mineralResult = mineralRecommendationService.getMineralRecommendation(4L);

        assertThat(mineralResult).isEmpty();
    }

    @Test
    void saveMineral() {
        // Given
        final Mineral mineral = new Mineral(2L, "Selen", null);

        final MineralRecommendation mineralRecommendation = buildMineralRecommendation(mineral, 1L, Unit.g, RecommendationPeriodType.DAYS);
        mineralRecommendation.setId(null);

        final MineralRecommendation mineralRecommendationSaved = buildMineralRecommendation(mineral, 1L, Unit.g, RecommendationPeriodType.DAYS);

        when(mineralRecommendationRepository.save(mineralRecommendation)).thenReturn(mineralRecommendationSaved);

        // When
        final MineralRecommendation mineralRecommendationInput = buildMineralRecommendation(mineral, 1L, Unit.g, RecommendationPeriodType.DAYS);
        mineralRecommendationInput.setId(null);

        final MineralRecommendation mineralRecommendationReturned = mineralRecommendationService.saveMineralRecommendation(mineralRecommendationInput);

        // Then
        final MineralRecommendation mineralCommandNewExpected = buildMineralRecommendation(mineral, 1L, Unit.g, RecommendationPeriodType.DAYS);

        assertThat(mineralRecommendationReturned.getId()).isEqualTo(2L);
        assertThat(mineralRecommendationReturned).isEqualTo(mineralCommandNewExpected);
    }

    @Test
    void deleteMineral() {
        mineralRecommendationService.deleteMineralRecommendation(2L);
        verify(mineralRecommendationRepository, times(1)).deleteById(2L);
    }

    @Test
    void getUsedMinerals() {
        final Mineral mineral1 = new Mineral(1L, "Eisen", null);
        final Mineral mineral2 = new Mineral(2L, "Selen", null);
        final Mineral mineral3 = new Mineral(3L, "Mangan", null);

        final MineralRecommendation mineralRecommendation1 = buildMineralRecommendation(mineral1, 1, Unit.mg, RecommendationPeriodType.HOURS);
        final MineralRecommendation mineralRecommendation2 = buildMineralRecommendation(mineral2, 2, Unit.g, RecommendationPeriodType.DAYS);
        //noinspection NonAsciiCharacters
        final MineralRecommendation mineralRecommendation3 = buildMineralRecommendation(mineral3, 3, Unit.µg, RecommendationPeriodType.WEEKS);

        final List<MineralRecommendation> mineralRecommendations = List.of(mineralRecommendation1, mineralRecommendation2, mineralRecommendation3);

        when(mineralRecommendationRepository.findAll()).thenReturn(mineralRecommendations);

        final Set<Mineral> mineralsAlreadyUsed = mineralRecommendationService.getMineralsAlreadyUsed();

        assertThat(mineralsAlreadyUsed).containsExactlyInAnyOrder(mineral1, mineral2, mineral3);
    }

    @Test
    void getRecommendationForMinerals() {
        final Mineral mineral1 = new Mineral(1L, "Eisen", null);
        final Mineral mineral2 = new Mineral(2L, "Selen", null);
        final Mineral mineral3 = new Mineral(3L, "Mangan", null);

        final MineralRecommendation mineralRecommendation1 = buildMineralRecommendation(mineral1, 1, Unit.mg, RecommendationPeriodType.HOURS);
        final MineralRecommendation mineralRecommendation2 = buildMineralRecommendation(mineral2, 2, Unit.g, RecommendationPeriodType.DAYS);
        //noinspection NonAsciiCharacters
        final MineralRecommendation mineralRecommendation3 = buildMineralRecommendation(mineral3, 3, Unit.µg, RecommendationPeriodType.WEEKS);

        final List<MineralRecommendation> mineralRecommendations = List.of(mineralRecommendation1, mineralRecommendation2, mineralRecommendation3);

        when(mineralRecommendationRepository.findAll()).thenReturn(mineralRecommendations);

        final Optional<MineralRecommendation> mineralRecommendationOptional = mineralRecommendationService.findRecommendationForMineral("Selen");

        assertThat(mineralRecommendationOptional).isPresent();

        final MineralRecommendation mineralRecommendation = mineralRecommendationOptional.get();
        assertThat(mineralRecommendation.getMineral()
                                        .getName()).isEqualTo("Selen");
    }

    @Test
    void getRecommendationForMineralsMissing() {
        final Mineral mineral1 = new Mineral(1L, "Eisen", null);

        final MineralRecommendation mineralRecommendation1 = buildMineralRecommendation(mineral1, 1, Unit.mg, RecommendationPeriodType.HOURS);

        final List<MineralRecommendation> mineralRecommendations = List.of(mineralRecommendation1);

        when(mineralRecommendationRepository.findAll()).thenReturn(mineralRecommendations);

        final Optional<MineralRecommendation> mineralRecommendationOptional = mineralRecommendationService.findRecommendationForMineral("Unknown");

        assertThat(mineralRecommendationOptional).isEmpty();
    }

    @Test
    void getRecommendationForMineralsEmptyList() {
        final List<MineralRecommendation> mineralRecommendations = Collections.emptyList();

        when(mineralRecommendationRepository.findAll()).thenReturn(mineralRecommendations);

        final Optional<MineralRecommendation> mineralRecommendationOptional = mineralRecommendationService.findRecommendationForMineral("Unknown");

        assertThat(mineralRecommendationOptional).isEmpty();
    }


    private static MineralRecommendation buildMineralRecommendation(final @NonNull Mineral mineral, final long no, final @NonNull Unit unit, final @NonNull RecommendationPeriodType periodType) {
        final MineralRecommendation mineralRecommendation1 = new MineralRecommendation();
        mineralRecommendation1.setId(1L + no);
        mineralRecommendation1.setMineral(mineral);
        mineralRecommendation1.setMinAmount(0.0f + no / 10.0f);
        mineralRecommendation1.setMaxAmount(9.0f + no / 10.0f);
        mineralRecommendation1.setUnit(unit);
        mineralRecommendation1.setTimePeriodDimension(periodType);
        mineralRecommendation1.setTimePeriodLength(no);
        return mineralRecommendation1;
    }

    private static MineralRecommendationCommand buildMineralRecommendationCommand(final @NonNull MineralCommand mineralCommand, final long no, final @NonNull Unit unit, final @NonNull RecommendationPeriodType periodType) {
        final MineralRecommendationCommand mineralRecommendation1 = new MineralRecommendationCommand();
        mineralRecommendation1.setId(1L + no);
        mineralRecommendation1.setMineral(mineralCommand);
        mineralRecommendation1.setMinAmount(0.0f + no / 10.0f);
        mineralRecommendation1.setMaxAmount(9.0f + no / 10.0f);
        mineralRecommendation1.setUnit(unit);
        mineralRecommendation1.setTimePeriodDimension(periodType);
        mineralRecommendation1.setTimePeriodLength(no);
        return mineralRecommendation1;
    }
}