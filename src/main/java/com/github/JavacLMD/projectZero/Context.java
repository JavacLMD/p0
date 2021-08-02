package com.github.JavacLMD.projectZero;

import com.github.JavacLMD.projectZero.controller.Database;
import com.github.JavacLMD.projectZero.controller.IAccessor;

public class Context {
    private Config appConfig;
    private IAccessor data;


    public Context(String ...args) {
        this.appConfig = new Config(args);
        data = Database.create();
    }


    public Config getAppConfig() {
        return appConfig;
    }
    public IAccessor getData() { return data; }

}
