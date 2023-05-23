package com.ratsoft.mineraltracker.controllers;

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
    @RequestMapping(value = {"", "/", "/index"}, method = RequestMethod.GET)
    public String getLandingPage(final Model model) {
        log.debug("Getting landing page");
        return "landingpage";
    }
}
