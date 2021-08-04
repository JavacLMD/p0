package com.github.JavacLMD.projectZero.view;

import com.github.JavacLMD.projectZero.controller.DOA;
import com.github.JavacLMD.projectZero.controller.MySQL_DOA;

public class Config {

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
