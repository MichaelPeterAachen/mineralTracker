package com.ratsoft.mineraltracker.controllers;

import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SuppressWarnings("ProhibitedExceptionDeclared")
@ExtendWith(MockitoExtension.class)
@NoArgsConstructor
public class LandingPageControllerTest {
    @InjectMocks
    private @NonNull LandingPageController landingPageController;

    @Mock
    private @NonNull Model model;

    private @NonNull MockMvc mockMvc;


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(landingPageController)
                                 .build();
    }

    @Test
    void getLandingPage() throws Exception {
        mockMvc.perform(get("/"))
               .andExpect(status().isOk())
               .andExpect(view().name("landingpage"));
    }
}