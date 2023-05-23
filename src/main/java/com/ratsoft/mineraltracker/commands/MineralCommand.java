package com.ratsoft.mineraltracker.commands;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Command for editing a mineral.
 *
 * @author mpeter
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MineralCommand {
    private Long id;

    private String name;
}
