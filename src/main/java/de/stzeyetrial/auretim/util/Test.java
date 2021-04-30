package de.stzeyetrial.auretim.util;

import de.stzeyetrial.auretim.config.Config;

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


    public static enum TestType{
        SIMPLE_PVT,
        GO_NOGO_PVT,
        N_BACK_TEST
    }

}
