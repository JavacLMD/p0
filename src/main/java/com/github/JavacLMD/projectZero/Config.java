package com.github.JavacLMD.projectZero;

import java.util.Properties;

public class Config {

    private String[] args;
    private Properties props;

    private String url;
    private String username, password;


    public Config(String[] args) {
        this.args = args;
        this.props = new Properties();
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--user":
                case "-u":
                    props.setProperty("username", args[++i]);
                    break;
                case "--pass":
                case "-p":
                    props.setProperty("password", args[++i]);
                    break;
                case "--dburl":
                case "-d":
                    props.setProperty("url", args[++i]);
                    break;
                default:
                    System.err.println("Unknown argument " + args[i]);
                    System.exit(1);
                    break;
            }
        }

        this.url = props.getProperty("url", "jdbc:mysql://localhost:3306/");
        this.username = props.getProperty("username", "root");
        this.password = props.getProperty("password", "");
    }


    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }


}
