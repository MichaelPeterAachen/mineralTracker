package com.ratsoft.mineraltracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.lang.Nullable;

/**
 * Main application class of the mineral tracker.
 *
 * @author mpeter
 */
@SuppressWarnings("ClassWithoutConstructor")
@SpringBootApplication
public class MineralTrackerApplication {

    /**
     * Main method of the mineral tracker.
     *
     * @param args command line arguments
     */
    public static void main(@Nullable final String[] args) {
        SpringApplication.run(MineralTrackerApplication.class, args);
    }

}
