package com.ratsoft.mineraltracker.controllers;

import com.ratsoft.mineraltracker.commands.MineralCommand;
import com.ratsoft.mineraltracker.commands.MineralRecommendationCommand;
import com.ratsoft.mineraltracker.converters.MineralMapper;
import com.ratsoft.mineraltracker.converters.MineralRecommendationMapper;
import com.ratsoft.mineraltracker.model.Mineral;
import com.ratsoft.mineraltracker.model.MineralRecommendation;
import com.ratsoft.mineraltracker.model.RecommendationPeriodType;
import com.ratsoft.mineraltracker.model.Unit;
import com.ratsoft.mineraltracker.services.MineralRecommendationService;
import com.ratsoft.mineraltracker.services.MineralService;
import lombok.NoArgsConstructor;
import lombok.NonNull;
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

@SuppressWarnings("ProhibitedExceptionDeclared")
@ExtendWith(MockitoExtension.class)
@NoArgsConstructor
public class MineralRecommendationControllerTest {

    private @NonNull MineralRecommendationController mineralRecommendationController;

    @Mock
    private @NonNull MineralRecommendationService mineralRecommendationService;

    @Mock
    private @NonNull MineralService mineralService;

    @Mock
    private @NonNull Model model;

    private @NonNull MockMvc mockMvc;


    @BeforeEach
    void setUp() {
        mineralRecommendationController = new MineralRecommendationController(mineralRecommendationService, mineralService, Mappers.getMapper(MineralRecommendationMapper.class), Mappers.getMapper(MineralMapper.class));
        mockMvc = MockMvcBuilders.standaloneSetup(mineralRecommendationController)
                                 .build();
    }

    @SuppressWarnings("unchecked")
    @Test
    void listMineralRecommendations() throws Exception {
        final Mineral mineralCommand1 = new Mineral(1L, "Eisen", null);
        final Mineral mineralCommand2 = new Mineral(2L, "Selen", null);
        final Mineral mineralCommand3 = new Mineral(3L, "Mangan", null);

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
        final Mineral mineralCommand2 = new Mineral(2L, "Selen", null);
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
        final Mineral mineral1 = new Mineral(1L, "Eisen", null);
        final Mineral mineral2 = new Mineral(2L, "Selen", null);
        final Mineral mineral3 = new Mineral(3L, "Fluor", null);
        final Mineral mineral4 = new Mineral(4L, "Mangan", null);

        final MineralRecommendation mineralRecommendation2 = buildMineralRecommendation(mineral2, 2, Unit.g, RecommendationPeriodType.DAYS);

        final MineralCommand mineralCommand = new MineralCommand(2L, "Selen", null);
        final MineralRecommendationCommand mineralRecommendationCommand = buildMineralRecommendationCommand(mineralCommand, 2, Unit.g, RecommendationPeriodType.DAYS);

        when(mineralRecommendationService.getMineralRecommendation(2L)).thenReturn(Optional.of(mineralRecommendation2));
        when(mineralRecommendationService.getMineralsAlreadyUsed()).thenReturn(Set.of(mineral2, mineral3));

        when(mineralService.getAllMinerals()).thenReturn(Set.of(mineral1, mineral2, mineral3, mineral4));

        mockMvc.perform(get("/mineralrecommendations/2/editform"))
               .andExpect(status().isOk())
               .andExpect(view().name("mineralrecommendations/editform"))
               .andExpect(MockMvcResultMatchers.model()
                                               .attribute("mineralrecommendation", mineralRecommendationCommand))
               .andExpect(MockMvcResultMatchers.model()
                                               .attribute("minerals", Set.of(mineral1, mineral2, mineral4)));
    }

    @Test
    void getNewFormForMineral() throws Exception {
        final MineralRecommendationCommand mineralRecommendationCommandNew = new MineralRecommendationCommand();

        final Mineral mineral1 = new Mineral(1L, "Eisen", null);
        final Mineral mineral2 = new Mineral(2L, "Selen", null);
        final Mineral mineral3 = new Mineral(3L, "Fluor", null);
        final Mineral mineral4 = new Mineral(4L, "Mangan", null);

        when(mineralRecommendationService.getMineralsAlreadyUsed()).thenReturn(Set.of(mineral1, mineral2));

        when(mineralService.getAllMinerals()).thenReturn(Set.of(mineral1, mineral2, mineral3, mineral4));

        mockMvc.perform(get("/mineralrecommendations/newform"))
               .andExpect(status().isOk())
               .andExpect(view().name("mineralrecommendations/editform"))
               .andExpect(MockMvcResultMatchers.model()
                                               .attribute("mineralrecommendation", mineralRecommendationCommandNew))
               .andExpect(MockMvcResultMatchers.model()
                                               .attribute("minerals", Set.of(mineral3, mineral4)));
    }

    @Test
    void newMineral() throws Exception {
        final MineralCommand mineralCommand2 = new MineralCommand(2L, "Selen", null);
        final MineralRecommendationCommand mineralRecommendationSaved = buildMineralRecommendationCommand(mineralCommand2, 2, Unit.g, RecommendationPeriodType.DAYS);
        when(mineralRecommendationService.saveMineralRecommendationCommand(any())).thenReturn(mineralRecommendationSaved);

        mockMvc.perform(post(URI.create("/mineralrecommendations")).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                                                   .param("name", "NewMineral2"))
               .andExpect(status().isMovedTemporarily())
               .andExpect(view().name("redirect:/mineralrecommendations/3"));
    }

    @Test
    void newMineralException() throws Exception {
        doThrow(IllegalArgumentException.class).when(mineralRecommendationService)
                                               .saveMineralRecommendationCommand(any());

        mockMvc.perform(post(URI.create("/mineralrecommendations")).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                                                   .param("name", "NewMineral2"))
               .andExpect(status().isOk())
               .andExpect(view().name("error/error"));

        verify(mineralRecommendationService, times(1)).saveMineralRecommendationCommand(any());
    }


    @Test
    void deleteMineral() throws Exception {
        mockMvc.perform(get("/mineralrecommendations/1/delete"))
               .andExpect(status().isMovedTemporarily())
               .andExpect(view().name("redirect:/mineralrecommendations"));

        verify(mineralRecommendationService, times(1)).deleteMineralRecommendation(1L);
    }

    @Test
    void deleteMineralException() throws Exception {
        doThrow(IllegalArgumentException.class).when(mineralRecommendationService)
                                               .deleteMineralRecommendation(any());

        mockMvc.perform(get("/mineralrecommendations/1/delete"))
               .andExpect(status().isOk())
               .andExpect(view().name("error/error"));

        verify(mineralRecommendationService, times(1)).deleteMineralRecommendation(1L);
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