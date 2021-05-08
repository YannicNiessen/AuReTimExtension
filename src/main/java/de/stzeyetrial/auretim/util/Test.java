package de.stzeyetrial.auretim.util;

import de.stzeyetrial.auretim.config.Config;
import de.stzeyetrial.auretim.screens.Screens;

/**
 * @author niessen
 */
public class Test {

    private final TestType _type;
    private final Config _config;

    public Test(final Config config, final TestType type){
        _type = type;
        _config = config;
    }



    public enum TestType {
        PVT_AUDITORY,
        PVT_VISUAL,
        N_BACK_VISUAL_STIMULUS_IDENTITY,
        N_BACK_VISUAL_LOCATION_IDENTITY,
        N_BACK_AUDITORY_STIMULUS_IDENTITY,
        N_BACK_DUAL_AUDITORY_VISUAL_LOCATION_IDENTITY,
        N_BACK_DUAL_AUDITORY_VISUAL_STIMULUS_IDENTITY,
        N_BACK_DUAL_VISUAL_VISUAL_STIMULUS_IDENTITY_LOCATION_IDENTITY,
        MACKWORTH_CLOCK,
        SPATIAL_WORKING_MEMORY
    }
}
