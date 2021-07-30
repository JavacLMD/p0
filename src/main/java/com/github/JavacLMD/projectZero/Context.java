package com.github.JavacLMD.projectZero;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Context {

    private Config appConfig;
    private Database database;

    public Map<Integer, Customer> customers = new HashMap<>();


    public Context(String ...args) {
        this.appConfig = new Config(args);
        this.database = Database.create(appConfig.getUrl(), appConfig.getUsername(), appConfig.getPassword());

        grabCustomersTable();


    }

    private void grabCustomersTable() {

        try (Statement statement = database.createStatement()) {

            ResultSet set = statement.executeQuery("SELECT * FROM customers");
            int customerID;
            String firstName, lastName, email, phone, address, city, state, postal;
            Gender gender;
            Date joinDate;

            Customer customer;

            while (set.next()) {
                customerID = set.getInt(1);
                firstName = set.getString(2);
                lastName = set.getString(3);
                email = set.getString(4);
                phone = set.getString(5);
                joinDate = set.getDate(6);
                gender = Gender.valueOf(set.getString(7));
                address = set.getString(8);
                city = set.getString(9);
                state = set.getString(10);
                postal = set.getString(11);

                customer = new Customer(customerID, firstName, lastName, email, phone, joinDate, gender, address, city, state, postal);
                customers.put(customerID, customer);
            }

        } catch (SQLException e) {

        }
    }





}
