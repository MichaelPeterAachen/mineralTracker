package com.ratsoft.mineraltracker.controllers;

import com.ratsoft.mineraltracker.commands.MineralCommand;
import com.ratsoft.mineraltracker.converters.MineralMapper;
import com.ratsoft.mineraltracker.model.Mineral;
import com.ratsoft.mineraltracker.services.MineralService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    private final @NonNull MineralService mineralService;

    private final @NonNull MineralMapper mineralMapper;

    /**
     * Get a list of all minerals, add it to the model and return the name of the template.
     *
     * @param model the model for the template.
     * @return the template name.
     */
    @GetMapping({"/minerals", "/minerals/"})
    public @NonNull String listMinerals(final Model model) {
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
    public @NonNull String showMineral(@PathVariable final @NonNull String id, final @NonNull Model model) {
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
    public @NonNull String getEditMineralForm(@PathVariable final @NonNull String id, final @NonNull Model model) {
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
    public @NonNull String getEditMineralForm(final Model model) {
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
    public @NonNull String saveOrUpdateMineral(@ModelAttribute final @NonNull MineralCommand command) {
        log.info("Request to save or update mineral: {}",command);
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
    @SuppressWarnings("OverlyBroadCatchBlock")
    @GetMapping({"/minerals/{id}/delete", "/minerals/{id}/delete/"})
    public @NonNull String deleteMineral(@PathVariable final @NonNull String id, final @NonNull Model model) {
        log.info("Request to delete a mineral: {}",id);
        try {
            mineralService.deleteMineral(Long.valueOf(id));
        } catch (final Exception e) {
            model.addAttribute("error", "Mineral could not be removed, it is still in use.");
            return "error/error";
        }
        return "redirect:/minerals";
    }

    @PostMapping("/minerals/{id}/image")
    public @NonNull String uploadImageForMineral(@PathVariable final @NonNull String id, @RequestParam("imagefile") final @NonNull MultipartFile file) {
        mineralService.saveImageFile(Long.valueOf(id), file);
        return "redirect:/minerals/" + id + "/editform";
    }
}
