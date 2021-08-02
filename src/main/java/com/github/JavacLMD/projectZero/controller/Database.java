package com.github.JavacLMD.projectZero.controller;

import com.github.JavacLMD.projectZero.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database implements IAccessor{

    private static final Logger log = LogManager.getLogger(Database.class);

    public static final String URL = "jdbc:mysql://localhost:3306/";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "";


    private static final String CREATE_SCHEMA = "CREATE SCHEMA IF NOT EXISTS petmanagement";
    private static final String USE_SCHEMA = "USE petmanagement";

    private static final String CREATE_CUSTOMERS_TABLE = "CREATE TABLE IF NOT EXISTS customers (" +
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

    private static final String CREATE_PETS_TABLE = "CREATE TABLE IF NOT EXISTS pets (" +
            "PetID INTEGER PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
            "CustomerID INTEGER NOT NULL, " +
            "Name VARCHAR(50) NOT NULL, " +
            "Gender ENUM('Unspecified', 'Male', 'Female', 'Other') DEFAULT 'Unspecified', " +
            "Breed VARCHAR(75), " +
            "BehaviourDescription VARCHAR(100), " +
            "AddedDate Date DEFAULT now(), " +
            "FOREIGN KEY (CustomerID) references customers(CustomerID)" +
            ")";


    private static final String SELECT_ALL_CUSTOMERS = "SELECT * FROM customers";
    private static final String SELECT_CUSTOMER_BY_ID = SELECT_ALL_CUSTOMERS + " WHERE CustomerID = ?";
    private static final String SELECT_CUSTOMER_BY_EMAIL = SELECT_ALL_CUSTOMERS + " WHERE EmailAddress = ?";
    private static final String SELECT_CUSTOMERS_BY_NAME = SELECT_ALL_CUSTOMERS + " WHERE (FirstName = ? OR LastName = ?) OR (FirstName = ? AND LastName = ?)";

    private static final String SELECT_ALL_PETS = "SELECT * FROM pets";
    private static final String SELECT_PETS_BY_CUSTOMER = SELECT_ALL_PETS + " WHERE CustomerID = ?";
    private static final String SELECT_PETS_BY_NAME = SELECT_ALL_PETS + " WHERE PetName = ?";
    private static final String SELECT_PET_BY_ID = SELECT_ALL_PETS + " WHERE PetID = ?";

    private static final String SELECT_ALL_APPOINTMENTS = "SELECT * FROM appointments";

    private static final String INSERT_INTO_CUSTOMERS = "INSERT INTO customers " +
            "(FirstName, LastName, EmailAddress, PhoneNumber, Gender, Address, City, State, PostalCode) " +
            "VALUE (?,?,LOWER(?),?,?,?,?,?,?)";
    private static final String REMOVE_CUSTOMER_BY_ID = "DELETE FROM customers where CustomerID = ?";
    private static final String REMOVE_CUSTOMER_BY_EMAIL = "DELETE FROM customers where EmailAddress = ?";
    private static final String UPDATE_CUSTOMER = "UPDATE customers SET " +
            "FirstName = ?, " +
            "LastName = ?, " +
            "EmailAddress = ?, " +
            "PhoneNumber = ?, " +
            "Gender = ?, " +
            "Address = ?, " +
            "City = ?," +
            "State = ?, " +
            "PostalCode = ? " +
            "WHERE CustomerID = ?";

    private static final String INSERT_INTO_PETS = "INSERT INTO pets " +
            "(CustomerID, PetName, Gender, Breed, BehaviourDescription) " +
            "VALUES (?,?,?,?,?)";
    private static final String REMOVE_FROM_PETS = "DELETE FROM pets WHERE PetID = ?";
    private static final String REMOVE_CUSTOMERS_PETS = "DELETE FROM pets WHERE CustomerID = ?";
    private static final String UPDATE_PET = "UPDATE pets SET " +
            "PetName = ?, " +
            "Gender = ?, " +
            "Breed = ?, " +
            "BehaviourDescription = ? " +
            "WHERE PetID = ? AND CustomerID = ?";

    private final String url, username, password;
    private final Connection connection;

    private List<Customer> allCustomers = new ArrayList<>();
    private List<Pet> allPets = new ArrayList<>();

    public static Database create(String url, String username, String password) {
        return new Database(url, username, password);
    }

    public static Database create() {
        return new Database(URL, USERNAME, PASSWORD);
    }

    private Database(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;

        Connection c = null;

        //attempt connection
        try {
            c = DriverManager.getConnection(this.url, this.username, this.password);
            log.debug("Connection established to " + url);
        } catch (SQLException e) {
            log.error("Connection failed from "  + url + "\n" + e.getMessage());
            System.exit(1);
        }

        this.connection = c;

        //create schema and database
        try (Statement statement = this.getConnection().createStatement()) {

            statement.execute(CREATE_SCHEMA);
            statement.execute(USE_SCHEMA);

            //statement.execute("DROP TABLE IF EXISTS pets");
            //statement.execute("DROP TABLE IF EXISTS customers");

            statement.execute(CREATE_CUSTOMERS_TABLE);
            log.debug("Established customer table from connection: " + statement.getConnection());
            statement.execute(CREATE_PETS_TABLE);
            log.debug("Established pet table from connection: " + statement.getConnection());
        } catch (SQLException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }

    private Connection getConnection() { return this.connection; }


    //id, first name, last name, email, phone, join date, gender, address, city, state, zip
    private Customer getCustomerFromSet(ResultSet set) {
        Customer customer = null;
        try {
            customer = new Customer(
                    set.getInt(1),
                    set.getString(2),
                    set.getString(3),
                    set.getString(4),
                    set.getString(5),
                    set.getDate(6),
                    Gender.valueOf(set.getString(7)),
                    set.getString(8),
                    set.getString(9),
                    set.getString(10),
                    set.getString(11)
            );
        } catch (SQLException e) {
            log.error(e);
        }

        return customer;
    }

    //id, customer id, pet name, gender, breed, behaviour, added date
    private Pet getPetFromSet(ResultSet set) {
        Pet pet = null;

        try {
            pet = new Pet(
                    set.getInt(1), //pet id
                    set.getInt(2), //customer id
                    set.getString(3), //name
                    Gender.valueOf(set.getString(4)), //gender
                    set.getString(5), //breed
                    set.getString(6), //behaviour
                    set.getDate(7)
            );
        } catch (SQLException e) {
            log.error(e.getMessage());
        } catch (IllegalArgumentException illegalArgumentException) {
            log.error(illegalArgumentException.getMessage());
        }
        return pet;
    }

    @Override //returns all customers
    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_CUSTOMERS)){
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                customers.add(getCustomerFromSet(set));
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return customers;
    }

    @Override //returns all customers by their first/last name or first AND last name
    public List<Customer> getCustomersByName(String ...args) {
        List<Customer> customers = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(SELECT_CUSTOMERS_BY_NAME)){

            statement.setString(1, args[0]);
            statement.setString(2, args[0]);
            statement.setString(3, args[0]);
            statement.setString(4, args.length > 1 ? args[1] : args[0]);

            ResultSet set = statement.executeQuery();
            while (set.next()) {
                customers.add(getCustomerFromSet(set));
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return customers;
    }


    @Override //returns the customer by specified id
    public Customer getCustomerByID(int customerID) {
        Customer customer = null;
        try (PreparedStatement statement = connection.prepareStatement(SELECT_CUSTOMER_BY_ID)){
            statement.setInt(1, customerID);
            ResultSet set = statement.executeQuery();
            if (set.next())
                customer = getCustomerFromSet(set);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return customer;
    }

    //returns the customer by specified email
    @Override
    public Customer getCustomerByEmail(String emailAddress) {
        Customer customer = null;
        try (PreparedStatement statement = connection.prepareStatement(SELECT_CUSTOMER_BY_EMAIL)) {
            statement.setString(1, emailAddress.toLowerCase());
            ResultSet set = statement.executeQuery();
            if (set.next())
                customer = getCustomerFromSet(set);
        } catch (Exception e) {

        }
        return customer;
    }

    //adds a whole new customer | will automatically assign an id and join date
    @Override
    public boolean insertCustomer(Customer customer) {
        boolean flag = false;
        try (PreparedStatement statement = connection.prepareStatement(INSERT_INTO_CUSTOMERS)){
            statement.setString(1, customer.getFirstName());
            statement.setString(2, customer.getLastName());
            statement.setString(3, customer.getEmailAddress());
            statement.setString(4, customer.getPhoneNumber());
            statement.setString(5, customer.getGender().toString());
            statement.setString(6, customer.getAddress());
            statement.setString(7, customer.getCity());
            statement.setString(8, customer.getState());
            statement.setString(9, customer.getPostalCode());
            statement.execute();
            flag = true;
        } catch (SQLException e) {
            log.error(e.getMessage());
            flag = false;
        }
        return flag;
    }

    @Override //removes the customer by associated id
    public boolean removeCustomer(int customerID) {
        boolean flag = false;
        try (PreparedStatement statement = connection.prepareStatement(REMOVE_CUSTOMER_BY_ID)){
            statement.setInt(1, customerID);
            statement.execute();
            flag = true;
        } catch (SQLException e) {
            log.error(e.getMessage());
            flag = false;
        }
        return flag;
    }

    @Override //removes the customer by associated email
    public boolean removeCustomer(String email) {
        boolean flag = false;
        try (PreparedStatement statement = connection.prepareStatement(REMOVE_CUSTOMER_BY_EMAIL)){
            statement.setString(1, email);
            statement.execute();
            flag = true;
        } catch (SQLException e) {
            log.error(e.getMessage());
            flag = false;
        }
        return flag;
    }

    @Override //updates the customers data
    public boolean updateCustomer(Customer customer) {
        boolean flag = false;
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_CUSTOMER)){
            statement.setString(1, customer.getFirstName());
            statement.setString(2, customer.getLastName());
            statement.setString(3, customer.getEmailAddress());
            statement.setString(4, customer.getPhoneNumber());
            statement.setString(5, customer.getGender().toString());
            statement.setString(6, customer.getAddress());
            statement.setString(7, customer.getCity());
            statement.setString(8, customer.getState());
            statement.setString(9, customer.getPostalCode());
            statement.execute();
            flag = true;
        } catch (SQLException e) {
            log.error(e.getMessage());
            flag = false;
        }
        return flag;
    }

    @Override //returns all pets
    public List<Pet> getAllPets() {
        List<Pet> pets = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_PETS)){
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                pets.add(getPetFromSet(set));
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return pets;
    }

    @Override
    public List<Pet> getPetsByName(String name) {
        List<Pet> pets = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(SELECT_PETS_BY_NAME)){
            statement.setString(1, name);
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                pets.add(getPetFromSet(set));
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return pets;
    }

    @Override
    public List<Pet> getPetsByCustomer(Customer customer) {
        List<Pet> pets = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(SELECT_CUSTOMER_BY_ID)){
            statement.setInt(1, customer.getCustomerID());
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                pets.add(getPetFromSet(set));
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return pets;
    }

    @Override
    public Pet getPetByID(int petID) {
        Pet pet = null;
        try (PreparedStatement statement = connection.prepareStatement(SELECT_PET_BY_ID)){
            statement.setInt(1, petID);
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                pet = getPetFromSet(set);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return pet;
    }

    @Override
    public boolean insertPet(Pet pet) {
        boolean flag = false;
        try (PreparedStatement statement = connection.prepareStatement(INSERT_INTO_PETS)){
            statement.setInt(1, pet.getCustomerID());
            statement.setString(2, pet.getName());
            statement.setString(3, pet.getGender().toString());
            statement.setString(4, pet.getBreed());
            statement.setString(5, pet.getBehaviourDescription());
            statement.execute();
            flag = true;
        } catch (SQLException e) {
            log.error(e.getMessage());
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean removePet(int petID) {
        boolean flag = false;
        try (PreparedStatement statement = connection.prepareStatement(REMOVE_FROM_PETS)){
            statement.setInt(1, petID);
            statement.execute();
            flag = true;
        } catch (SQLException e) {
            log.error(e.getMessage());
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean removePetsFromCustomer(Customer customer) {
        boolean flag = false;
        try (PreparedStatement statement = connection.prepareStatement(REMOVE_CUSTOMERS_PETS)){
            statement.setInt(1, customer.getCustomerID());
            statement.execute();
            flag = true;
        } catch (SQLException e) {
            log.error(e.getMessage());
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean updatePet(Pet pet) {
        boolean flag = false;
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_PET)){
            statement.setString(1, pet.getName());
            statement.setString(2, pet.getGender().toString());
            statement.setString(3, pet.getBreed());
            statement.setString(4, pet.getBehaviourDescription());
            statement.execute();
            flag = true;
        } catch (SQLException e) {
            log.error(e.getMessage());
            flag = false;
        }
        return flag;
    }


}
