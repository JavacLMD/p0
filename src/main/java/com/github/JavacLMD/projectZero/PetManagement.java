package com.github.JavacLMD.projectZero;

import com.github.JavacLMD.projectZero.view.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PetManagement {
    private static final Logger log = LogManager.getLogger(PetManagement.class.getName());

    public static void main(String[] args) {

        log.trace("Entering application");

        Config config = new Config(args);
        config.getAppInterface().run();

        log.trace("Exiting application");

    }
}
