package com.ratsoft.mineraltracker.controllers;

import com.ratsoft.mineraltracker.commands.MineralCommand;
import com.ratsoft.mineraltracker.converters.MineralMapper;
import com.ratsoft.mineraltracker.model.Mineral;
import com.ratsoft.mineraltracker.services.MineralService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
public class MineralControllerTest {

    private MineralController mineralController;

    @Mock
    private MineralService mineralService;

    @Mock
    private Model model;

    private MockMvc mockMvc;


    @BeforeEach
    void setUp() {
        mineralController = new MineralController(mineralService, Mappers.getMapper(MineralMapper.class));
        mockMvc = MockMvcBuilders.standaloneSetup(mineralController)
                                 .build();
    }

    @Test
    void listMinerals() throws Exception {
        final Mineral mineral1 = new Mineral(1L, "IRON1");
        final Mineral mineral2 = new Mineral(2L, "SELEN1");
        final Mineral mineral3 = new Mineral(3L, "FLOUR1");

        final Set<Mineral> mineralList = Set.of(mineral1, mineral2, mineral3);

        when(mineralService.getAllMinerals()).thenReturn(mineralList);

        final String result = mineralController.listMinerals(model);

        final ArgumentCaptor<Set<Mineral>> argumentCaptor = ArgumentCaptor.forClass(Set.class);

        verify(model, times(1))
               .addAttribute(eq("minerals"), argumentCaptor.capture());

        final Set<Mineral> resultMinerals = argumentCaptor.getValue();
        assertThat(resultMinerals).containsExactlyInAnyOrder(mineral1, mineral2, mineral3);

        mockMvc.perform(get("/minerals"))
               .andExpect(status().isOk())
               .andExpect(view().name("minerals/list"))
               .andExpect(MockMvcResultMatchers.model()
                                               .attribute("minerals", Matchers.equalToObject(mineralList)));
    }

    @Test
    void showMineral() throws Exception {
        final Mineral mineral2 = new Mineral(2L, "SELEN2");

        when(mineralService.getMineral(2L)).thenReturn(Optional.of(mineral2));

        final String result = mineralController.showMineral("2", model);

        final ArgumentCaptor<Mineral> argumentCaptor = ArgumentCaptor.forClass(Mineral.class);

        verify(model, times(1))
               .addAttribute(eq("mineral"), argumentCaptor.capture());

        final Mineral resultMinerals = argumentCaptor.getValue();
        assertThat(resultMinerals).isEqualTo(mineral2);

        mockMvc.perform(get("/minerals/2"))
               .andExpect(status().isOk())
               .andExpect(view().name("minerals/show"))
               .andExpect(MockMvcResultMatchers.model()
                                               .attribute("mineral", mineral2));
    }

    @Test
    void getEditFormForMineral() throws Exception {
        final Mineral mineral2 = new Mineral(2L, "SELEN2");
        final MineralCommand mineralCommand2 = new MineralCommand(2L, "SELEN2");

        when(mineralService.getMineral(2L)).thenReturn(Optional.of(mineral2));

        mockMvc.perform(get("/minerals/2/editform"))
               .andExpect(status().isOk())
               .andExpect(view().name("minerals/editform"))
               .andExpect(MockMvcResultMatchers.model()
                                               .attribute("mineral", mineralCommand2));
    }

    @Test
    void getNewFormForMineral() throws Exception {
        final MineralCommand mineralCommandNew = new MineralCommand();

        mockMvc.perform(get("/minerals/newform"))
               .andExpect(status().isOk())
               .andExpect(view().name("minerals/editform"))
               .andExpect(MockMvcResultMatchers.model()
                                               .attribute("mineral", mineralCommandNew));
    }

    @Test
    void newMineral() throws Exception {
        final MineralCommand mineralCommandNew = new MineralCommand();
        mineralCommandNew.setName("NewMaterial2");

        final MineralCommand mineralCommandNewSaved = new MineralCommand(3L, "NewMaterial");

        when(mineralService.saveMineralCommand(any())).thenReturn(mineralCommandNewSaved);

        mockMvc.perform(post(URI.create("/minerals")).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                                     .param("name", "NewMineral2"))
               .andExpect(status().isMovedTemporarily())
               .andExpect(view().name("redirect:/minerals/3"));
    }

    @Test
    void deleteMineral() throws Exception {
        mockMvc.perform(get("/minerals/1/delete"))
               .andExpect(status().isMovedTemporarily())
               .andExpect(view().name("redirect:/minerals"));

        verify(mineralService, times(1) ).deleteMineral(1L);
    }
}