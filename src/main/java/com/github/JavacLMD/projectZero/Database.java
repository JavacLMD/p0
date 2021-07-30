package com.github.JavacLMD.projectZero;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private static final Logger log = LogManager.getLogger(Database.class);
    private static final String createCustomerTableSQL = "CREATE TABLE IF NOT EXISTS customers (" +
            "CustomerID INTEGER PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
            "FirstName VARCHAR(50) NOT NULL, " +
            "LastName VARCHAR(50) NOT NULL, " +
            "EmailAddress VARCHAR(100) NOT NULL, " +
            "PhoneNumber VARCHAR(15), " +
            "JoinedDate DATE DEFAULT now(), " +
            "Gender ENUM('Unspecified', 'Male', 'Female', 'Other') DEFAULT 'Unspecified', " +
            "Address VARCHAR(255), " +
            "City VARCHAR(50), " +
            "State VARCHAR(50), " +
            "PostalCode VARCHAR(15), " +
            "UNIQUE (EmailAddress)" +
            ")";

    private static final String createPetsTableSQL = "CREATE TABLE IF NOT EXISTS pets (" +
            "PetID INTEGER PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
            "CustomerID INTEGER NOT NULL, " +
            "Name VARCHAR(50) NOT NULL, " +
            "Gender ENUM('Unspecified', 'Male', 'Female', 'Other') DEFAULT 'Unspecified', " +
            "Breed VARCHAR(75), " +
            "AddedDate Date DEFAULT now(), " +
            "BehaviourDescription VARCHAR(100), " +
            "FOREIGN KEY (CustomerID) references customers(CustomerID)" +
            ")";

    private final String url, username, password;

    private Connection connection;

    public static Database create(String url, String username, String password) {
        return new Database(url, username, password);
    }

    private Database(String url) {
        this(url, "root", "");
    }

    private Database(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;

        try {
            this.connection = DriverManager.getConnection(this.url, this.username, this.password);
            //log.info("Connection established to " + url);
        } catch (SQLException e) {
            log.error("Connection failed from "  + url + "\n" + e.getMessage());
            System.exit(1);
        }
        createSchema();
    }

    public Connection getConnection() {
        return this.connection;
    }

    public Statement createStatement() throws SQLException {
        return getConnection().createStatement();
    }

    //Closes the existing connection if it's active
    public void disconnect() {
        try {
            this.connection.close();
        } catch (SQLException e) {
            //e.printStackTrace();
        }
    }

    //Creates the required tables the connection needs; if they don't exist
    private void createSchema() {
        try (Statement statement = this.getConnection().createStatement()) {

            statement.execute("CREATE SCHEMA IF NOT EXISTS petmanagement");
            statement.execute("USE petmanagement");

            //statement.execute("DROP TABLE IF EXISTS pets");
            //statement.execute("DROP TABLE IF EXISTS customers");

            statement.execute(createCustomerTableSQL);
            //log.info("Established customer table from connection: " + statement.getConnection());
            statement.execute(createPetsTableSQL);
            //log.info("Established pet table from connection: " + statement.getConnection());
        } catch (SQLException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }

    }



}
