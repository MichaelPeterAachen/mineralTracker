package com.ratsoft.mineraltracker.services.it;


import com.ratsoft.mineraltracker.commands.MineralCommand;
import com.ratsoft.mineraltracker.controllers.MineralController;
import com.ratsoft.mineraltracker.model.Mineral;
import com.ratsoft.mineraltracker.repositories.MineralRepository;
import com.ratsoft.mineraltracker.services.MineralService;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
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
public class MineralServiceIT {
    @Autowired
    @NonNull
    MineralService mineralService;

    @Autowired
    @NonNull
    MineralController mineralController;

    @Autowired
    @NonNull
    MineralRepository mineralRepository1;

    @Autowired
    private @NonNull WebApplicationContext webApplicationContext;

    private @NonNull MockMvc mockMvc;

    @BeforeEach
    public void setup() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                                      .build();
    }

    @Transactional
    @Test
    public void testSaveofDescriptionViaService() {
        final MineralCommand mineralCommand = new MineralCommand();
        mineralCommand.setName("Eisen");

        final MineralCommand saveMineralCommand = mineralService.saveMineralCommand(mineralCommand);

        assertThat(saveMineralCommand.getName()).isEqualTo("Eisen");
        assertThat(saveMineralCommand.getId()).isGreaterThan(1L);
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Transactional
    @Test
    public void testSaveofDescriptionViaController() {
        final MineralCommand mineralCommand = new MineralCommand();
        mineralCommand.setName("NewMineral1");

        final Optional<Mineral> mineralOptionalBefore = mineralRepository1.findByName("NewMineral1");
        assertThat(mineralOptionalBefore).withFailMessage("Precondition for test not correct. Mineral already in database.")
                                         .isEmpty();

        final String result = mineralController.saveOrUpdateMineral(mineralCommand);

        final Optional<Mineral> mineralOptional = mineralRepository1.findByName("NewMineral1");

        assertThat(mineralOptional).isPresent();

        final Mineral mineral = mineralOptional.get();
        assertThat(mineral.getName()).isEqualTo("NewMineral1");
        assertThat(mineral.getId()).isGreaterThan(1L);
    }

    @Transactional
    @Test
    public void testUpdateofDescriptionViaRest() throws Exception {
        final Optional<Mineral> mineralOptionalBefore = mineralRepository1.findByName("Eisen");

        assertThat(mineralOptionalBefore).withFailMessage("Precondition for test not correct. Mineral missing in database.")
                                         .isPresent();

        final Long currId = mineralOptionalBefore.get()
                                                 .getId();

        mockMvc.perform(post(URI.create("/minerals")).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                                     .param("name", "Eisen2")
                                                     .param("id", String.valueOf(currId)))
               .andExpect(status().isMovedTemporarily());


        final Optional<Mineral> mineralOptional = mineralRepository1.findByName("Eisen2");

        assertThat(mineralOptional).isPresent();

        final Mineral mineral = mineralOptional.get();
        assertThat(mineral.getName()).isEqualTo("Eisen2");
        assertThat(mineral.getId()).isEqualTo(currId);
    }


}
