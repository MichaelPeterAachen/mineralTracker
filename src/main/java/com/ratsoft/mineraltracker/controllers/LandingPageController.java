package com.ratsoft.mineraltracker.controllers;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * The controller for the landing page.
 *
 * @author mpeter
 */
@SuppressWarnings("HardcodedFileSeparator")
@Slf4j
@Controller
@RequiredArgsConstructor
public class LandingPageController {

    /**
     * Get the landing page for the application.
     *
     * @param model the model for the template.
     * @return the template name.
     */
    @SuppressWarnings("SameReturnValue")
    @RequestMapping(value = {"", "/", "/index"}, method = RequestMethod.GET)
    public @NonNull String getLandingPage(final @NonNull Model model) {
        log.debug("Getting landing page");
        return "landingpage";
    }
}
