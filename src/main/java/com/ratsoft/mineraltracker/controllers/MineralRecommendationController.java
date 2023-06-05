package com.ratsoft.mineraltracker.controllers;

import com.ratsoft.mineraltracker.commands.MineralRecommendationCommand;
import com.ratsoft.mineraltracker.converters.MineralMapper;
import com.ratsoft.mineraltracker.converters.MineralRecommendationMapper;
import com.ratsoft.mineraltracker.model.Mineral;
import com.ratsoft.mineraltracker.model.MineralRecommendation;
import com.ratsoft.mineraltracker.services.MineralRecommendationService;
import com.ratsoft.mineraltracker.services.MineralService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * The controller to access the mineral recommendations.
 *
 * @author mpeter
 */
@SuppressWarnings({"HardcodedFileSeparator", "SameReturnValue"})
@Slf4j
@Controller
@RequiredArgsConstructor
public class MineralRecommendationController {

    private final @NonNull MineralRecommendationService mineralRecommendationService;

    private final @NonNull MineralService mineralService;

    private final @NonNull MineralRecommendationMapper mineralRecommendationMapper;

    private final @NonNull MineralMapper mineralMapper;

    /**
     * Get a list of all mineral recommendations, add it to the model and return the name of the template.
     *
     * @param model the model for the template.
     * @return the template name.
     */
    @GetMapping({"/mineralrecommendations", "/mineralrecommendations/"})
    public @NonNull String listMineralRecommendations(final Model model) {
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
    @SuppressWarnings("NestedMethodCall")
    @GetMapping("/mineralrecommendations/{id}")
    public @NonNull String showMineralRecommendation(@SuppressWarnings("SameParameterValue") @PathVariable final @NonNull String id, final @NonNull Model model) {
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
    @SuppressWarnings("NestedMethodCall")
    @GetMapping("/mineralrecommendations/{id}/editform")
    public @NonNull String getNewMineralRecommendationForm(@PathVariable final @NonNull String id, final Model model) {
        log.debug("Getting edit form for a mineral recommendation with id: {}", id);

        final Optional<MineralRecommendation> mineralRecommendation = mineralRecommendationService.getMineralRecommendation(Long.valueOf(id));
        mineralRecommendation.ifPresent(value -> model.addAttribute("mineralrecommendation", mineralRecommendationMapper.mineralRecommendationToCommand(value)));

        final Set<Mineral> usedMinerals = mineralRecommendationService.getMineralsAlreadyUsed();
        final Set<Mineral> minerals = mineralService.getAllMinerals();

        final Set<Mineral> resultSet = new HashSet<>(minerals);
        resultSet.removeAll(usedMinerals);
        mineralRecommendation.ifPresent(value -> resultSet.add(value.getMineral()));

        model.addAttribute("minerals", resultSet);

        return "mineralrecommendations/editform";
    }

    /**
     * Get the form for a new mineral recommendation.
     *
     * @param model the model for the template.
     * @return the template name or editing.
     */
    @SuppressWarnings("NestedMethodCall")
    @GetMapping("/mineralrecommendations/newform")
    public @NonNull String getNewMineralRecommendationForm(final Model model) {
        log.debug("Getting new mineral recommendation form.");

        final MineralRecommendation mineralRecommendation = new MineralRecommendation();
        model.addAttribute("mineralrecommendation", mineralRecommendationMapper.mineralRecommendationToCommand(mineralRecommendation));

        final Set<Mineral> usedMinerals = mineralRecommendationService.getMineralsAlreadyUsed();
        final Set<Mineral> minerals = mineralService.getAllMinerals();

        final Collection<Mineral> resultSet = new HashSet<>(minerals);
        resultSet.removeAll(usedMinerals);

        model.addAttribute("minerals", resultSet);

        return "mineralrecommendations/editform";
    }

    /**
     * Save or update a specific mineral recommentation command.
     *
     * @param command the command to save or update.
     * @param model   the model for the page.
     * @return New page being shown afterwars.
     */
    @SuppressWarnings({"OverlyBroadCatchBlock", "FeatureEnvy", "NestedMethodCall"})
    @PostMapping({"/mineralrecommendations", "/mineralrecommendations/"})
    public @NonNull String saveOrUpdateMineralRecommendation(@ModelAttribute final @NonNull MineralRecommendationCommand command, final @NonNull Model model) {
        log.info("Request to save or update mineral recommendation : {}", command);
        //noinspection DataFlowIssue
        final Optional<Mineral> mineral = mineralService.getMineral(command.getMineralId());
        mineral.ifPresent(value -> command.setMineral(mineralMapper.mineralToCommand(value)));
        try {
            final MineralRecommendation mineralRecommendation = mineralRecommendationMapper.commandToMineralRecommendation(command);
            //noinspection DataFlowIssue
            final MineralRecommendation savedRecommendation = mineralRecommendationService.saveMineralRecommendation(mineralRecommendation);
            final MineralRecommendationCommand savedCommand = mineralRecommendationMapper.mineralRecommendationToCommand(savedRecommendation);
            log.info("Saved or updated successfully: {}", savedCommand);
            //noinspection DataFlowIssue
            return "redirect:/mineralrecommendations/" + savedCommand.getId();
        } catch (final Exception e) {
            model.addAttribute("error", "Mineral recommendation could not be saved or updated. " + e.getMessage());
            log.error("Saving or updating mineral recommendation failed: {}", e.getMessage());
            return "error/error";
        }
    }

    /**
     * Delete a specific mineral recommendation.
     *
     * @param id    the id of the mineral recommendation to delete.
     * @param model the model for the page.
     * @return New page being shown afterwars.
     */
    @GetMapping({"/mineralrecommendations/{id}/delete", "/mineralrecommendations/{id}/delete/"})
    public @NonNull String deleteMineralRecommendation(@PathVariable final @NonNull String id, final @NonNull Model model) {
        log.info("Request to delete a mineral recommentation: {}", id);
        mineralRecommendationService.deleteMineralRecommendation(Long.valueOf(id));
        return "redirect:/mineralrecommendations";
    }
}
