package com.ratsoft.mineraltracker.controllers;

import com.ratsoft.mineraltracker.commands.MineralCommand;
import com.ratsoft.mineraltracker.converters.MineralMapper;
import com.ratsoft.mineraltracker.model.Mineral;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SuppressWarnings("ProhibitedExceptionDeclared")
@ExtendWith(MockitoExtension.class)
@NoArgsConstructor
public class MineralControllerTest {

    private @NonNull MineralController mineralController;

    @Mock
    private @NonNull MineralService mineralService;

    @Mock
    private @NonNull Model model;

    private @NonNull MockMvc mockMvc;


    @BeforeEach
    void setUp() {
        mineralController = new MineralController(mineralService, Mappers.getMapper(MineralMapper.class));
        mockMvc = MockMvcBuilders.standaloneSetup(mineralController)
                                 .build();
    }

    @SuppressWarnings("unchecked")
    @Test
    void listMinerals() throws Exception {
        final Mineral mineral1 = new Mineral(1L, "IRON1", null);
        final Mineral mineral2 = new Mineral(2L, "SELEN1", null);
        final Mineral mineral3 = new Mineral(3L, "FLOUR1", null);

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
               .andExpect(model()
                       .attribute("minerals", Matchers.equalToObject(mineralList)));
    }

    @Test
    void showMineral() throws Exception {
        final Mineral mineral2 = new Mineral(2L, "SELEN2", null);

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
               .andExpect(model()
                       .attribute("mineral", mineral2));
    }

    @Test
    void getEditFormForMineral() throws Exception {
        final Mineral mineral2 = new Mineral(2L, "SELEN2", null);
        final MineralCommand mineralCommand2 = new MineralCommand(2L, "SELEN2", null);

        when(mineralService.getMineral(2L)).thenReturn(Optional.of(mineral2));

        mockMvc.perform(get("/minerals/2/editform"))
               .andExpect(status().isOk())
               .andExpect(view().name("minerals/editform"))
               .andExpect(model()
                       .attribute("mineral", mineralCommand2));
    }

    @Test
    void getNewFormForMineral() throws Exception {
        final MineralCommand mineralCommandNew = new MineralCommand();

        mockMvc.perform(get("/minerals/newform"))
               .andExpect(status().isOk())
               .andExpect(view().name("minerals/editform"))
               .andExpect(model()
                       .attribute("mineral", mineralCommandNew));
    }

    @Test
    void newMineral() throws Exception {
        final MineralCommand mineralCommandNew = new MineralCommand();
        mineralCommandNew.setName("NewMineral2");

        final MineralCommand mineralCommandNewSaved = new MineralCommand(3L, "NewMineral", null);

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

        verify(mineralService, times(1)).deleteMineral(1L);
    }

    @Test
    void uploadImage() throws Exception {
        final MockMultipartFile multiplartFile = new MockMultipartFile("imagefile", "testing.txt", "text/plain", "Spring Framework Gutu".getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(multipart("/minerals/1/image").file(multiplartFile))
               .andExpect(status().isFound())
               .andExpect(header().string("Location", "/minerals/1/editform"));

        verify(mineralService, times(1)).saveImageFile(anyLong(), any());
    }
}