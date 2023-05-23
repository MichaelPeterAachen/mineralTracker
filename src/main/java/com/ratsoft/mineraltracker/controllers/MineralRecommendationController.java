package com.ratsoft.mineraltracker.controllers;

import com.ratsoft.mineraltracker.commands.MineralRecommendationCommand;
import com.ratsoft.mineraltracker.converters.MineralRecommendationMapper;
import com.ratsoft.mineraltracker.model.Mineral;
import com.ratsoft.mineraltracker.model.MineralRecommendation;
import com.ratsoft.mineraltracker.services.MineralRecommendationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;
import java.util.Set;

/**
 * The controller to access the mineral recommendations.
 *
 * @author mpeter
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class MineralRecommendationController {

    private final MineralRecommendationService mineralRecommendationService;

    private final MineralRecommendationMapper mineralRecommendationMapper;

    /**
     * Get a list of all mineral recommendations, add it to the model and return the name of the template.
     *
     * @param model the model for the template.
     * @return the template name.
     */
    @GetMapping(value = {"/mineralrecommendations", "/mineralrecommendations/"})
    public String listMineralRecommendations(final Model model) {
        log.debug("Getting all mineral recommendations");

        final Set<MineralRecommendation> allMineralRecommendations = mineralRecommendationService.getAllMineralRecommendations();

        model.addAttribute("mineralrecommendations", allMineralRecommendations);

        return "mineralrecommendations/list";
    }

    /**
     * Get a mineral recommendation by it's id, add it to the model and return the name of the template.
     *
     * @param id    the id of the mineral recommendation.
     * @param model the model for the template.
     * @return the template name.
     */
    @GetMapping(value = "/mineralrecommendations/{id}")
    public String showMineralRecommendation(@PathVariable final String id, final Model model) {
        log.debug("Getting mineral recommendation with id: {}", id);

        final Optional<MineralRecommendation> mineral = mineralRecommendationService.getMineralRecommendation(Long.valueOf(id));
        mineral.ifPresent(value -> model.addAttribute("mineralrecommendation", value));

        return "mineralrecommendations/show";
    }

    /**
     * Get the edit form for a mineral recommendation.
     *
     * @param id    the id of the mineral recommendation.
     * @param model the model for the template.
     * @return the template name or editing.
     */
    @GetMapping(value = "/mineralrecommendations/{id}/editform")
    public String getEditMineralRecommendationForm(@PathVariable final String id, final Model model) {
        log.debug("Getting edit form for a mineral recommendation with id: {}", id);

        final Optional<MineralRecommendation> mineralRecommendation = mineralRecommendationService.getMineralRecommendation(Long.valueOf(id));
        mineralRecommendation.ifPresent(value -> model.addAttribute("mineralrecommendation", mineralRecommendationMapper.mineralRecommendationToCommand(value)));

        return "mineralrecommendations/editform";
    }

    /**
     * Get the form for a new mineral recommendation.
     *
     * @param model the model for the template.
     * @return the template name or editing.
     */
    @GetMapping(value = "/mineralrecommendations/newform")
    public String getEditMineralRecommendationForm(final Model model) {
        log.debug("Getting new mineral recommendation form.");

        final MineralRecommendation mineralRecommendation = new MineralRecommendation();
        model.addAttribute("mineralrecommendation", mineralRecommendationMapper.mineralRecommendationToCommand(mineralRecommendation));

        return "mineralrecommendations/editform";
    }

    /**
     * Save or update a specific mineral recommentation command.
     *
     * @param command the command to save or update.
     * @return New page being shown afterwars.
     */
    @PostMapping(value = {"/mineralrecommendations", "/mineralrecommendations/"})
    public String saveOrUpdateMineralRecommendation(@ModelAttribute final MineralRecommendationCommand command) {
        log.info("Request to save or update mineral recommendation : {}", command);
        final MineralRecommendationCommand savedCommand = mineralRecommendationService.saveMineralRecommendationCommand(command);
        log.info("Saved or updated successfully: {}", savedCommand);
        return "redirect:/mineralrecommendations/" + savedCommand.getId();
    }

    /**
     * Delete a specific mineral recommendation.
     *
     * @param id    the id of the mineral recommendation to delete.
     * @param model the model for the page.
     * @return New page being shown afterwars.
     */
    @GetMapping(value = {"/mineralrecommendations/{id}/delete", "/mineralrecommendations/{id}/delete/"})
    public String deleteMineralRecommendation(@PathVariable final String id, final Model model) {
        log.info("Request to delete a mineral recommentation: {}", id);
        try {
            mineralRecommendationService.deleteMineralRecommendation(Long.valueOf(id));
        } catch (Exception e) {
            model.addAttribute("error", "Mineral recommendation could not be removed, it is still in use.");
            return "error/error";
        }
        return "redirect:/mineralrecommendations";
    }
}
