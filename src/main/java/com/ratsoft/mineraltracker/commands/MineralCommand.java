package com.ratsoft.mineraltracker.commands;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.lang.Nullable;

/**
 * Command for editing a mineral.
 *
 * @author mpeter
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MineralCommand {
    @Nullable
    private Long id;

    @Nullable
    private String name;

    @Nullable
    private byte[] image;
}
