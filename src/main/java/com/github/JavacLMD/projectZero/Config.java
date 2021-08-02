package com.github.JavacLMD.projectZero;

import com.github.JavacLMD.projectZero.controller.StringHelper;

import java.util.Properties;

public class Config {

    private String[] args;
    private Properties props;

    private boolean searchPets = false;
    private boolean searchCustomers = false;
    private String petName = null;


    private int petID = -1;
    private String customerName = null;
    private String customerEmail = null;


    public Config(String[] args) {
        this.args = args;
        this.props = new Properties();


        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--customers":
                case "-c":
                    props.setProperty("Search Customers", "true");
                    if (args[++i].toCharArray()[0] != '-') {
                        try {
                            String value = args[++i];

                             //check to see if the value is an email
                            if (StringHelper.isEmail(value)) {
                                props.setProperty("Email", value);
                            } else {
                                //set the value as a name
                                props.setProperty("Customer Name", value);
                            }
                            ++i;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "--pets":
                case "-p":
                    props.setProperty("Search Pets", "true");
                    if (args[++i].toCharArray()[0] != '-') {
                        try {
                            String value = args[++i];

                            if (StringHelper.isEmail(value)) {
                                props.setProperty("Email", value);
                            } else if (StringHelper.isInteger(value)) {
                                props.setProperty("Pet ID", value);
                            } else {
                                props.setProperty("Pet Name", value);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                default:
                    break;
            }

        }

        { //set pet information
            if ("true".equalsIgnoreCase(props.getProperty("Search Pets", "false")))
                this.searchPets = true;
            petName = props.getProperty("Pet Name", null);

            String id = props.getProperty("Pet ID", "-1");
            if (StringHelper.isInteger(id)) petID = Integer.parseInt(id);
        }

        { //set customer information
            if ("true".equalsIgnoreCase(props.getProperty("Search Customers", "false")))
                searchCustomers = true;

            customerName = props.getProperty("Customer Name", null);
            customerEmail = props.getProperty("Email", null);
        }

    }

    public boolean doPetSearch() {
        return searchPets;
    }

    public boolean doCustomerSearch() {
        return searchCustomers;
    }

    public String getPetName() {
        return petName;
    }

    public int getPetID() {
        return petID;
    }

    public String getCustomerName() {
        return customerName;
    }
    public String getCustomerEmail() {
        return customerEmail;
    }



}
