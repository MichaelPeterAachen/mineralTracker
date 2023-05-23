package com.ratsoft.mineraltracker.controllers;

import com.ratsoft.mineraltracker.commands.MineralCommand;
import com.ratsoft.mineraltracker.commands.MineralRecommendationCommand;
import com.ratsoft.mineraltracker.converters.MineralRecommendationMapper;
import com.ratsoft.mineraltracker.model.Mineral;
import com.ratsoft.mineraltracker.model.MineralRecommendation;
import com.ratsoft.mineraltracker.model.RecommendationPeriodType;
import com.ratsoft.mineraltracker.model.Unit;
import com.ratsoft.mineraltracker.services.MineralRecommendationService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.net.URI;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
public class MineralRecommendationControllerTest {

    private MineralRecommendationController mineralRecommendationController;

    @Mock
    private MineralRecommendationService mineralRecommendationService;

    @Mock
    private Model model;

    private MockMvc mockMvc;


    @BeforeEach
    void setUp() {
        mineralRecommendationController = new MineralRecommendationController(mineralRecommendationService, Mappers.getMapper(MineralRecommendationMapper.class));
        mockMvc = MockMvcBuilders.standaloneSetup(mineralRecommendationController)
                                 .build();
    }

    @Test
    void listMineralRecommendations() throws Exception {
        final Mineral mineralCommand1 = new Mineral(1L, "Eisen");
        final Mineral mineralCommand2 = new Mineral(2L, "Selen");
        final Mineral mineralCommand3 = new Mineral(3L, "Mangan");

        final MineralRecommendation mineralRecommendation1 = buildMineralRecommendation(mineralCommand1, 1, Unit.mg, RecommendationPeriodType.HOURS);
        final MineralRecommendation mineralRecommendation2 = buildMineralRecommendation(mineralCommand2, 2, Unit.g, RecommendationPeriodType.DAYS);
        final MineralRecommendation mineralRecommendation3 = buildMineralRecommendation(mineralCommand3, 3, Unit.µg, RecommendationPeriodType.WEEKS);

        final Set<MineralRecommendation> mineralRecommendations = Set.of(mineralRecommendation1, mineralRecommendation2, mineralRecommendation3);

        when(mineralRecommendationService.getAllMineralRecommendations()).thenReturn(mineralRecommendations);

        final String result = mineralRecommendationController.listMineralRecommendations(model);

        final ArgumentCaptor<Set<MineralRecommendation>> argumentCaptor = ArgumentCaptor.forClass(Set.class);

        verify(model, times(1))
                .addAttribute(eq("mineralrecommendations"), argumentCaptor.capture());

        final Set<MineralRecommendation> resultMinerals = argumentCaptor.getValue();
        assertThat(resultMinerals).containsExactlyInAnyOrder(mineralRecommendation1, mineralRecommendation2, mineralRecommendation3);

        mockMvc.perform(get("/mineralrecommendations"))
               .andExpect(status().isOk())
               .andExpect(view().name("mineralrecommendations/list"))
               .andExpect(MockMvcResultMatchers.model()
                                               .attribute("mineralrecommendations", Matchers.equalToObject(mineralRecommendations)));
    }

    @Test
    void showMineral() throws Exception {
        final Mineral mineralCommand2 = new Mineral(2L, "Selen");
        final MineralRecommendation mineralRecommendation2 = buildMineralRecommendation(mineralCommand2, 2, Unit.g, RecommendationPeriodType.DAYS);

        when(mineralRecommendationService.getMineralRecommendation(2L)).thenReturn(Optional.of(mineralRecommendation2));

        final String result = mineralRecommendationController.showMineralRecommendation("2", model);

        final ArgumentCaptor<MineralRecommendation> argumentCaptor = ArgumentCaptor.forClass(MineralRecommendation.class);

        verify(model, times(1))
                .addAttribute(eq("mineralrecommendation"), argumentCaptor.capture());

        final MineralRecommendation resultMinerals = argumentCaptor.getValue();
        assertThat(resultMinerals).isEqualTo(mineralRecommendation2);

        mockMvc.perform(get("/mineralrecommendations/2"))
               .andExpect(status().isOk())
               .andExpect(view().name("mineralrecommendations/show"))
               .andExpect(MockMvcResultMatchers.model()
                                               .attribute("mineralrecommendation", mineralRecommendation2));
    }

    @Test
    void getEditFormForMineral() throws Exception {
        final Mineral mineral = new Mineral(2L, "Selen");
        final MineralRecommendation mineralRecommendation2 = buildMineralRecommendation(mineral, 2, Unit.g, RecommendationPeriodType.DAYS);

        final MineralCommand mineralCommand = new MineralCommand(2L, "Selen");
        final MineralRecommendationCommand mineralRecommendationCommand = buildMineralRecommendationCommand(mineralCommand, 2, Unit.g, RecommendationPeriodType.DAYS);

        when(mineralRecommendationService.getMineralRecommendation(2L)).thenReturn(Optional.of(mineralRecommendation2));

        mockMvc.perform(get("/mineralrecommendations/2/editform"))
               .andExpect(status().isOk())
               .andExpect(view().name("mineralrecommendations/editform"))
               .andExpect(MockMvcResultMatchers.model()
                                               .attribute("mineralrecommendation", mineralRecommendationCommand));
    }

    @Test
    void getNewFormForMineral() throws Exception {
        final MineralRecommendationCommand mineralRecommendationCommandNew = new MineralRecommendationCommand();

        mockMvc.perform(get("/mineralrecommendations/newform"))
               .andExpect(status().isOk())
               .andExpect(view().name("mineralrecommendations/editform"))
               .andExpect(MockMvcResultMatchers.model()
                                               .attribute("mineralrecommendation", mineralRecommendationCommandNew));
    }

    @Test
    void newMineral() throws Exception {
        final MineralCommand mineralCommand2 = new MineralCommand(2L, "Selen");
        final MineralRecommendationCommand mineralRecommendationSaved = buildMineralRecommendationCommand(mineralCommand2, 2, Unit.g, RecommendationPeriodType.DAYS);

        when(mineralRecommendationService.saveMineralRecommendationCommand(any())).thenReturn(mineralRecommendationSaved);

        mockMvc.perform(post(URI.create("/mineralrecommendations")).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                                                   .param("name", "NewMineral2"))
               .andExpect(status().isMovedTemporarily())
               .andExpect(view().name("redirect:/mineralrecommendations/3"));
    }

    @Test
    void deleteMineral() throws Exception {
        mockMvc.perform(get("/mineralrecommendations/1/delete"))
               .andExpect(status().isMovedTemporarily())
               .andExpect(view().name("redirect:/mineralrecommendations"));

        verify(mineralRecommendationService, times(1)).deleteMineralRecommendation(1L);
    }


    private static MineralRecommendation buildMineralRecommendation(Mineral mineral, long no, Unit unit, RecommendationPeriodType periodType) {
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

    private static MineralRecommendationCommand buildMineralRecommendationCommand(MineralCommand mineralCommand, long no, Unit unit, RecommendationPeriodType periodType) {
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