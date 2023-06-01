package com.ratsoft.mineraltracker.services.it;


import com.ratsoft.mineraltracker.commands.MineralCommand;
import com.ratsoft.mineraltracker.commands.MineralRecommendationCommand;
import com.ratsoft.mineraltracker.controllers.MineralRecommendationController;
import com.ratsoft.mineraltracker.model.Mineral;
import com.ratsoft.mineraltracker.model.MineralRecommendation;
import com.ratsoft.mineraltracker.model.RecommendationPeriodType;
import com.ratsoft.mineraltracker.model.Unit;
import com.ratsoft.mineraltracker.repositories.MineralRecommendationRepository;
import com.ratsoft.mineraltracker.services.MineralRecommendationService;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.context.WebApplicationContext;

import java.net.URI;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings({"PackageVisibleField", "ProhibitedExceptionDeclared"})
@ExtendWith(SpringExtension.class)
@SpringBootTest
@NoArgsConstructor
public class MineralRecommendationServiceIT {
    @Autowired
    @NonNull
    MineralRecommendationService recommendationService;

    @Autowired
    @NonNull
    MineralRecommendationController recommendationController;

    @Autowired
    @NonNull
    MineralRecommendationRepository recommendationRepository;

    @Autowired
    private @NonNull WebApplicationContext webApplicationContext;

    @Mock
    private @NonNull Model controllerModel;

    private @NonNull MockMvc mockMvc;

    @BeforeEach
    public void setup() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                                 .build();
    }

    @Transactional
    @Test
    public void testSaveofDescriptionViaService() {
        final Mineral newMineral = new Mineral(3L, "Mangan", null);
        final MineralRecommendation mineralRecommendation = buildMineralRecommendation(newMineral, 1, Unit.µg, RecommendationPeriodType.DAYS);
        mineralRecommendation.setId(null);

        final MineralRecommendation saveMineralCommand = recommendationService.saveMineralRecommendation(mineralRecommendation);

        assertThat(saveMineralCommand.getMineral()).isEqualTo(newMineral);
        assertThat(saveMineralCommand.getId()).isGreaterThan(1L);
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Transactional
    @Test
    public void testSaveofDescriptionViaController() {
        final MineralCommand newMineral = new MineralCommand(3L, "Mangan", null);
        final MineralRecommendationCommand mineralRecommendationCommand = buildMineralRecommendationCommand(newMineral, 1, Unit.µg, RecommendationPeriodType.DAYS);
        mineralRecommendationCommand.setId(null);

        final Optional<MineralRecommendation> mineralOptionalBefore = recommendationService.findRecommendationForMineral("Mangan");
        assertThat(mineralOptionalBefore).withFailMessage("Precondition for test not correct. Recommendation already in database.")
                                         .isEmpty();

        final String result = recommendationController.saveOrUpdateMineralRecommendation(mineralRecommendationCommand, controllerModel);

        final Optional<MineralRecommendation> mineralOptionalAfter = recommendationService.findRecommendationForMineral("Mangan");

        assertThat(mineralOptionalAfter).isPresent();

        final MineralRecommendation mineralRecommendationAfter = mineralOptionalAfter.get();
        assertThat(mineralRecommendationAfter.getMineral()
                                             .getName()).isEqualTo("Mangan");
    }

    @Transactional
    @Test
    public void testUpdateofDescriptionViaRest() throws Exception {
        final Optional<MineralRecommendation> mineralOptionalBefore = recommendationService.findRecommendationForMineral("Mangan");
        assertThat(mineralOptionalBefore).withFailMessage("Precondition for test not correct. Recommendation alre ady in database.")
                                         .isEmpty();

        mockMvc.perform(post(URI.create("/mineralrecommendations")).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                                                   .param("mineralId", "3")
                                                                   .param("minAmount", "1.0")
                                                                   .param("maxAmount", "11.0")
                                                                   .param("unit", "mg")
                                                                   .param("timePeriodLength", "3")
                                                                   .param("timePeriodDimension", "DAYS"))
               .andExpect(status().isMovedTemporarily());


        final Optional<MineralRecommendation> mineralOptionalAfter = recommendationService.findRecommendationForMineral("Mangan");

        assertThat(mineralOptionalAfter).isPresent();

        final MineralRecommendation mineralRecommendationAfter = mineralOptionalAfter.get();
        assertThat(mineralRecommendationAfter.getMineral()
                                             .getName()).isEqualTo("Mangan");
    }

    private static MineralRecommendation buildMineralRecommendation(final @NonNull Mineral mineral, final long no, final @NonNull Unit unit, final @NonNull RecommendationPeriodType periodType) {
        final MineralRecommendation mineralRecommendation1 = new MineralRecommendation();
        mineralRecommendation1.setId(1L + no);
        mineralRecommendation1.setMineral(mineral);
        mineralRecommendation1.setMinAmount(0.0f + no / 10);
        mineralRecommendation1.setMaxAmount(9.0f + no / 10);
        mineralRecommendation1.setUnit(unit);
        mineralRecommendation1.setTimePeriodDimension(periodType);
        mineralRecommendation1.setTimePeriodLength(no);
        return mineralRecommendation1;
    }

    private static MineralRecommendationCommand buildMineralRecommendationCommand(final @NonNull MineralCommand mineralCommand, final long no, final @NonNull Unit unit, final @NonNull RecommendationPeriodType periodType) {
        final MineralRecommendationCommand mineralRecommendation1 = new MineralRecommendationCommand();
        mineralRecommendation1.setId(1L + no);
        mineralRecommendation1.setMineral(mineralCommand);
        mineralRecommendation1.setMinAmount(0.0f + no / 10);
        mineralRecommendation1.setMaxAmount(9.0f + no / 10);
        mineralRecommendation1.setUnit(unit);
        mineralRecommendation1.setTimePeriodDimension(periodType);
        mineralRecommendation1.setTimePeriodLength(no);
        return mineralRecommendation1;
    }
}
