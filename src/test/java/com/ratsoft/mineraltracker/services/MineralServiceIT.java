package com.ratsoft.mineraltracker.services;


import com.ratsoft.mineraltracker.commands.MineralCommand;
import com.ratsoft.mineraltracker.controllers.MineralController;
import com.ratsoft.mineraltracker.model.Mineral;
import com.ratsoft.mineraltracker.repositories.MineralRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class MineralServiceIT {
    @Autowired
    MineralService mineralService;

    @Autowired
    MineralController mineralController;

    @Autowired
    MineralRepository mineralRepository1;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
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

    @Transactional
    @Test
    public void testSaveofDescriptionViaController() {
        final MineralCommand mineralCommand = new MineralCommand();
        mineralCommand.setName("NewMaterial1");

        final Optional<Mineral> mineralOptionalBefore = mineralRepository1.findByName("NewMaterial1");
        assertThat(mineralOptionalBefore).withFailMessage("Precondition for test not correct. Mineral already in database.")
                                         .isEmpty();

        final String result = mineralController.saveOrUpdateMineral(mineralCommand);

        final Optional<Mineral> mineralOptional = mineralRepository1.findByName("NewMaterial1");

        assertThat(mineralOptional).isPresent();

        final Mineral mineral = mineralOptional.get();
        assertThat(mineral.getName()).isEqualTo("NewMaterial1");
        assertThat(mineral.getId()).isGreaterThan(1L);
    }

    @Transactional
    @Test
    public void testUpdateofDescriptionViaRest() throws Exception {
        final Optional<Mineral> mineralOptionalBefore = mineralRepository1.findByName("Eisen");

        assertThat(mineralOptionalBefore).withFailMessage("Precondition for test not correct. Mineral missing in database.")
                                         .isPresent();

        Long currId = mineralOptionalBefore.get().getId();

        mockMvc.perform(post(URI.create("/minerals")).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                                     .param("name", "Eisen2").param("id", ""+currId))
               .andExpect(status().isMovedTemporarily());


        final Optional<Mineral> mineralOptional = mineralRepository1.findByName("Eisen2");

        assertThat(mineralOptional).isPresent();

        final Mineral mineral = mineralOptional.get();
        assertThat(mineral.getName()).isEqualTo("Eisen2");
        assertThat(mineral.getId()).isEqualTo(currId);
    }


}
