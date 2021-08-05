package com.github.JavacLMD.projectZero.view;

import com.github.JavacLMD.projectZero.controller.DOA;
import com.github.JavacLMD.projectZero.controller.MySQL_DOA;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Config {
    private static final Logger log = LogManager.getLogger(Config.class.getSimpleName());

    private String[] args;
    private DOA dataAccessor;
    private Interface appInterface;



    public Config(String ...args) {
        this.args = args;
        dataAccessor = new MySQL_DOA();
        appInterface = new CommandInterface(dataAccessor);

    }

    public DOA getDataAccessor() {
        return dataAccessor;
    }

    public Interface getAppInterface() {
        return appInterface;
    }

}
