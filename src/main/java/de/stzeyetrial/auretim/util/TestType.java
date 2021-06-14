package de.stzeyetrial.auretim.util;

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
    SPATIAL_WORKING_MEMORY;

    public static boolean contains(String test) {

        for (TestType t : TestType.values()) {
            if (t.name().equals(test)) {
                return true;
            }
        }

        return false;
    }
}
