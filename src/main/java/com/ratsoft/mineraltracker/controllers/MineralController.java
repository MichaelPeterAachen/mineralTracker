package com.ratsoft.mineraltracker.controllers;

import com.ratsoft.mineraltracker.commands.MineralCommand;
import com.ratsoft.mineraltracker.converters.MineralMapper;
import com.ratsoft.mineraltracker.model.Mineral;
import com.ratsoft.mineraltracker.services.MineralService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

/**
 * The controller to access the minerals.
 *
 * @author mpeter
 */
@SuppressWarnings({"HardcodedFileSeparator", "SameReturnValue"})
@Slf4j
@Controller
@RequiredArgsConstructor
public class MineralController {

    private final MineralService mineralService;

    private final MineralMapper mineralMapper;

    /**
     * Get a list of all minerals, add it to the model and return the name of the template.
     *
     * @param model the model for the template.
     * @return the template name.
     */
    @GetMapping({"/minerals", "/minerals/"})
    public String listMinerals(final Model model) {
        log.debug("Getting mineral list");

        final Set<Mineral> allMinerals = mineralService.getAllMinerals();

        model.addAttribute("minerals", allMinerals);

        return "minerals/list";
    }

    /**
     * Get a mineral by it's id, add it to the model and return the name of the template.
     *
     * @param id    the id of the mineral.
     * @param model the model for the template.
     * @return the template name.
     */
    @SuppressWarnings("NestedMethodCall")
    @GetMapping("/minerals/{id}")
    public String showMineral(@PathVariable final String id, final Model model) {
        log.debug("Getting mineral with id: {}", id);

        final Optional<Mineral> mineral = mineralService.getMineral(Long.valueOf(id));
        mineral.ifPresent(value -> model.addAttribute("mineral", value));

        return "minerals/show";
    }

    /**
     * Get the edit form for a mineral.
     *
     * @param id    the id of the mineral.
     * @param model the model for the template.
     * @return the template name or editing.
     */
    @SuppressWarnings("NestedMethodCall")
    @GetMapping("/minerals/{id}/editform")
    public String getEditMineralForm(@PathVariable final String id, final Model model) {
        log.debug("Getting edit form for a mineral with id: {}", id);

        final Optional<Mineral> mineral = mineralService.getMineral(Long.valueOf(id));
        mineral.ifPresent(value -> model.addAttribute("mineral", mineralMapper.mineralToCommand(value)));

        return "minerals/editform";
    }

    /**
     * Get the form for a new mineral.
     *
     * @param model the model for the template.
     * @return the template name or editing.
     */
    @SuppressWarnings("NestedMethodCall")
    @GetMapping("/minerals/newform")
    public String getEditMineralForm(final Model model) {
        log.debug("Getting new mineral form.");

        final Mineral mineral = new Mineral();
        model.addAttribute("mineral", mineralMapper.mineralToCommand(mineral));

        return "minerals/editform";
    }

    /**
     * Save or update a specific mineral command.
     * @param command the command to save or update.
     * @return New page being shown afterwars.
     */
    @PostMapping({"/minerals", "/minerals/"})
    public String saveOrUpdateMineral(@ModelAttribute final MineralCommand command) {
        log.info("Request to save or update material: {}",command);
        final MineralCommand savedCommand = mineralService.saveMineralCommand(command);
        log.info("Saved or updated successfully: {}",savedCommand);
        return "redirect:/minerals/"+savedCommand.getId();
    }

    /**
     * Delete a specific mineral.
     * @param id the id of the mineral to delete.
     * @param model the model for the templates.
     * @return New page being shown afterwars.
     */
    @GetMapping({"/minerals/{id}/delete", "/minerals/{id}/delete/"})
    public String deleteMineral(@PathVariable final String id, final Model model) {
        log.info("Request to delete a material: {}",id);
        try {
            mineralService.deleteMineral(Long.valueOf(id));
        } catch (final Exception e) {
            model.addAttribute("error", "Mineral could not be removed, it is still in use.");
            return "error/error";
        }
        return "redirect:/minerals";
    }
}
