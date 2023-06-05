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
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SuppressWarnings({"ProhibitedExceptionDeclared", "MissingJavadoc", "HardcodedFileSeparator", "NestedMethodCall"})
@ExtendWith(MockitoExtension.class)
@NoArgsConstructor
class MineralControllerTest {

    private @NonNull MineralController mineralController;

    @Mock
    private @NonNull MineralService mineralService;

    @Mock
    private @NonNull Model model;

    private @NonNull MockMvc mockMvc;


    @BeforeEach
    void setUp() {
        mineralController = new MineralController(mineralService, Mappers.getMapper(MineralMapper.class));
        final GlobalControllerExceptionHandler globalController = new GlobalControllerExceptionHandler();
        mockMvc = MockMvcBuilders.standaloneSetup(mineralController, globalController)
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

        verify(model, times(1)).addAttribute(eq("minerals"), argumentCaptor.capture());

        final Set<Mineral> resultMinerals = argumentCaptor.getValue();
        assertThat(resultMinerals).containsExactlyInAnyOrder(mineral1, mineral2, mineral3);

        mockMvc.perform(get("/minerals"))
               .andExpect(status().isOk())
               .andExpect(view().name("minerals/list"))
               .andExpect(model().attribute("minerals", Matchers.equalToObject(mineralList)));
    }

    @Test
    void showMineral() throws Exception {
        final Mineral mineral2 = new Mineral(2L, "SELEN2", null);

        when(mineralService.getMineral(2L)).thenReturn(Optional.of(mineral2));

        final String result = mineralController.showMineral("2", model);

        final ArgumentCaptor<Mineral> argumentCaptor = ArgumentCaptor.forClass(Mineral.class);

        verify(model, times(1)).addAttribute(eq("mineral"), argumentCaptor.capture());

        final Mineral resultMinerals = argumentCaptor.getValue();
        assertThat(resultMinerals).isEqualTo(mineral2);

        mockMvc.perform(get("/minerals/2"))
               .andExpect(status().isOk())
               .andExpect(view().name("minerals/show"))
               .andExpect(model().attribute("mineral", mineral2));
    }

    @Test
    void getMineralWithImage() throws Exception {
        final @NonNull Byte[] testImage = transformImageForDomain("This is a test text".getBytes(StandardCharsets.UTF_8));

        final Mineral mineral = new Mineral(1L, "TestMineral", testImage);
        final Optional<Mineral> mineralOptional = Optional.of(mineral);

        when(mineralService.getMineral(anyLong())).thenReturn(mineralOptional);

        mockMvc.perform(get("/minerals/1"))
               .andExpect(status().isOk())
               .andExpect(view().name("minerals/show"))
               .andExpect(model().attribute("mineral", mineral));
    }

    @Test
    void getImageForMineral() throws Exception {
        final byte[] imageRaw = "This is a test text".getBytes(StandardCharsets.UTF_8);
        final @NonNull Byte[] rawImageAsByte = transformImageForDomain(imageRaw);

        final Mineral mineral = new Mineral(1L, "TestMineral", rawImageAsByte);
        final Optional<Mineral> mineralOptional = Optional.of(mineral);

        when(mineralService.getMineral(anyLong())).thenReturn(mineralOptional);

        final MockHttpServletResponse response = mockMvc.perform(get("/minerals/1/image"))
                                                        .andExpect(status().isOk())
                                                        .andReturn()
                                                        .getResponse();

        final byte[] responseBytes = response.getContentAsByteArray();

        assertThat(responseBytes).isEqualTo(imageRaw);
    }

    @Test
    void getEditFormForMineral() throws Exception {
        final Mineral mineral2 = new Mineral(2L, "SELEN2", null);
        final MineralCommand mineralCommand2 = new MineralCommand(2L, "SELEN2", null);

        when(mineralService.getMineral(2L)).thenReturn(Optional.of(mineral2));

        mockMvc.perform(get("/minerals/2/editform"))
               .andExpect(status().isOk())
               .andExpect(view().name("minerals/editform"))
               .andExpect(model().attribute("mineral", mineralCommand2));
    }

    @Test
    void getNewFormForMineral() throws Exception {
        final MineralCommand mineralCommandNew = new MineralCommand();

        mockMvc.perform(get("/minerals/newform"))
               .andExpect(status().isOk())
               .andExpect(view().name("minerals/editform"))
               .andExpect(model().attribute("mineral", mineralCommandNew));
    }

    @Test
    void newMineral() throws Exception {
        final Mineral mineralNewSaved = new Mineral(3L, "NewMineral", null);

        when(mineralService.saveMineral(any())).thenReturn(mineralNewSaved);

        mockMvc.perform(post(URI.create("/minerals")).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                                     .param("name", "NewMineral2"))
               .andExpect(status().is3xxRedirection())
               .andExpect(view().name("redirect:/minerals/3"));
    }

    @Test
    void deleteMineral() throws Exception {
        mockMvc.perform(get("/minerals/1/delete"))
               .andExpect(status().is3xxRedirection())
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

    @Test
    void uploadImageError() throws Exception {
        final MockMultipartFile multiplartFile = new MockMultipartFile("imagefile", "testing.txt", "text/plain", "Spring Framework Gutu".getBytes(StandardCharsets.UTF_8));
        final MockMultipartFile newMock = mock(MockMultipartFile.class);
        when(newMock.getName()).thenReturn("imagefile");
        when(newMock.getBytes()).thenThrow(new IOException("Exception accessing bytes"));

        mockMvc.perform(multipart("/minerals/1/image").file(newMock))
               .andExpect(status().isOk())
               .andExpect(model().attributeExists("error"))
               .andExpect(view().name("error/error"));
    }

    private static @NonNull Byte @NonNull [] transformImageForDomain(final @NonNull byte @NonNull [] imageBytes) {
        final Byte[] imageByteObject = new Byte[imageBytes.length];
        for (int i = 0; i < imageBytes.length; i++) {
            imageByteObject[i] = imageBytes[i];
        }
        return imageByteObject;
    }

    private static @NonNull byte @NonNull [] transformImageForController(final @NonNull Byte @NonNull [] imageBytes) {
        final byte[] imageByteObject = new byte[imageBytes.length];
        for (int i = 0; i < imageBytes.length; i++) {
            imageByteObject[i] = imageBytes[i];
        }
        return imageByteObject;
    }


}