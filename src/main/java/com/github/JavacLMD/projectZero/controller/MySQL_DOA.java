package com.github.JavacLMD.projectZero.controller;

import com.github.JavacLMD.projectZero.model.Customer;
import com.github.JavacLMD.projectZero.model.Gender;
import com.github.JavacLMD.projectZero.model.Pet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQL_DOA implements DOA {
    private static Logger log = LogManager.getLogger(MySQL_DOA.class);

    private static final String CREATE_CUSTOMERS_TABLE = "CREATE TABLE IF NOT EXISTS customers (" +
            "CustomerID INTEGER PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
            "FirstName VARCHAR(50) NOT NULL, " +
            "LastName VARCHAR(50) NOT NULL, " +
            "EmailAddress VARCHAR(100) NOT NULL UNIQUE," +
            "PhoneNumber VARCHAR(15), " +
            "Gender ENUM('Unspecified', 'Male', 'Female', 'Other') DEFAULT 'Unspecified', " +
            "Address VARCHAR(255), " +
            "City VARCHAR(50), " +
            "State VARCHAR(50), " +
            "PostalCode VARCHAR(15), " +
            "JoinedDate DATE DEFAULT now() " +
            ")";

    private static final String CREATE_PETS_TABLE = "CREATE TABLE IF NOT EXISTS pets (" +
            "PetID INTEGER PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
            "CustomerID INTEGER NOT NULL, " +
            "PetName VARCHAR(50) NOT NULL, " +
            "Gender ENUM('Unspecified', 'Male', 'Female', 'Other') DEFAULT 'Unspecified', " +
            "Breed VARCHAR(75), " +
            "BehaviourDescription VARCHAR(100)," +
            "AddedDate Date DEFAULT now(), " +
            "FOREIGN KEY (CustomerID) references customers(CustomerID)" +
            ")";

    private static final String SELECT_CUSTOMERS = "SELECT * FROM customers";
    private static final String SELECT_CUSTOMERS_BY_NAME = SELECT_CUSTOMERS + " WHERE (FirstName = ? OR LastName = ?) OR (FirstName = ? AND LastName = ?)";
    private static final String SELECT_CUSTOMER_BY_EMAIL = SELECT_CUSTOMERS + " WHERE EmailAddress = ?";
    private static final String SELECT_CUSTOMER_BY_ID = SELECT_CUSTOMERS + " WHERE CustomerID = ?";

    private static final String ADD_CUSTOMER = "INSERT INTO customers (FirstName, LastName, EmailAddress, PhoneNumber, Address, City, State, PostalCode, Gender, JoinDate) " +
            "VALUES (?, ?, LOWER(?), ?, ?, ?, ?, ?, ?, now())";
    private static final String REMOVE_CUSTOMER = "DELETE FROM customers WHERE CustomerID = ?";
    private static final String UPDATE_CUSTOMER = "UPDATE customers SET " +
            "FirstName = ?, LastName = ?, EmailAddress = ?, PhoneNumber = ?, Address = ?, City = ?, State = ?, PostalCode = ?, Gender = ? " +
            "WHERE CustomerID = ?";


    private static final String SELECT_PETS = "SELECT * FROM pets";
    private static final String SELECT_PETS_BY_CUSTOMER = SELECT_PETS + " WHERE CustomerID = ?";
    private static final String SELECT_PET_BY_ID = SELECT_PETS + " WHERE PetID = ?";

    private static final String ADD_PET = "INSERT INTO pets (CustomerID, PetName, Gender, Breed, BehaviourDescription) " +
            "VALUES (?, ?, ?, ?, ?)";
    private static final String REMOVE_PET = "DELETE FROM pets WHERE PetID = ?";
    private static final String REMOVE_PETS_BY_CUSTOMER = "DELETE FROM pets WHERE CustomerID = ?";
    private static final String UPDATE_PET = "UPDATE pets SET PetName = ?, Gender = ?, Breed = ?, BehaviourDescription = ? " +
            "WHERE PetID = ? AND CustomerID = ?";


    private final Connection connection;

    public MySQL_DOA() {
        Connection c = null;
        try {
            c = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "");
        } catch (SQLException e ) {

        }
        connection = c;

        try (Statement statement = connection.createStatement()){
            statement.addBatch("CREATE SCHEMA IF NOT EXISTS petmanagement");
            statement.addBatch("USE petmanagement");

            statement.addBatch(CREATE_CUSTOMERS_TABLE);
            statement.addBatch(CREATE_PETS_TABLE);

            statement.executeBatch();

        } catch (SQLException e) {

        }

    }


    private static Customer getCustomerFromSet(ResultSet set) {
        Customer c = null;
        try {
            c = new Customer(set.getInt(1),
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
            e.printStackTrace();
        }
        return c;
    }

    private static Pet getPetFromSet(ResultSet set) {
        Pet pet = null;
        try {
            pet = new Pet(set.getInt(1),
                    set.getInt(2),
                    set.getString(3),
                    Gender.valueOf(set.getString(4)),
                    set.getString(5),
                    set.getString(6),
                    set.getDate(7)
            );
        } catch (SQLException e) {
            log.error(e);
        }
        return pet;
    }


    @Override
    public ArrayList<Customer> getCustomers() {
        ArrayList<Customer> customers = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(SELECT_CUSTOMERS) )
        {
            ResultSet set = statement.executeQuery();
            while(set.next())
                customers.add(getCustomerFromSet(set));

        } catch (SQLException e) {
            log.error(e);
        }
        return customers;
    }

    @Override
    public ArrayList<Customer> getCustomersByName(String ...name) {
        ArrayList<Customer> customers = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(SELECT_CUSTOMERS_BY_NAME) )
        {
            statement.setString(1, name[0]);
            statement.setString(2, name[0]);
            statement.setString(3, name[0]);
            statement.setString(4, name.length > 1 ? name[1] : name[0]);

            ResultSet set = statement.executeQuery();
            while(set.next())
                customers.add(getCustomerFromSet(set));

        } catch (SQLException e) {
            log.error(e);
        }
        return customers;
    }

    @Override
    public Customer getCustomerByEmail(String email) {
        Customer customer = null;
        try (PreparedStatement statement = connection.prepareStatement(SELECT_CUSTOMER_BY_EMAIL) )
        {
            statement.setString(1, email);
            ResultSet set = statement.executeQuery();
            if(set.next())
                customer = (getCustomerFromSet(set));

        } catch (SQLException e) {
            log.error(e);
        }
        return customer;
    }

    @Override
    public Customer getCustomerByID(int customerID) {
        Customer customer = null;
        try (PreparedStatement statement = connection.prepareStatement(SELECT_CUSTOMER_BY_ID) )
        {
            statement.setInt(1, customerID);
            ResultSet set = statement.executeQuery();
            if(set.next())
                customer = (getCustomerFromSet(set));

        } catch (SQLException e) {
            log.error(e);
        }
        return customer;
    }

    @Override
    public boolean addCustomer(Customer customer) {
        boolean flag = false;
        try (PreparedStatement statement = connection.prepareStatement(ADD_CUSTOMER) )
        {
            statement.setString(1, customer.getFirstName());
            statement.setString(2, customer.getLastName());
            statement.setString(3, customer.getEmailAddress());
            statement.setString(4, customer.getPhoneNumber());
            statement.setString(5, customer.getAddress());
            statement.setString(6, customer.getCity());
            statement.setString(7, customer.getState());
            statement.setString(8, customer.getPostalCode());
            statement.setString(9, customer.getGender().toString());
            //statement.setDate(10, customer.);
            statement.execute();
            flag = true;

        } catch (SQLException e) {
            log.error(e);
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean removeCustomer(int id) {
        boolean flag = false;
        try (PreparedStatement statement = connection.prepareStatement(REMOVE_CUSTOMER) )
        {
            statement.setInt(1, id);
            statement.execute();
            flag = true;

        } catch (SQLException e) {
            log.error(e);
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean updateCustomer(Customer customer) {
        boolean flag = false;
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_CUSTOMER) )
        {
            statement.setString(1, customer.getFirstName());
            statement.setString(2, customer.getLastName());
            statement.setString(3, customer.getEmailAddress());
            statement.setString(4, customer.getPhoneNumber());
            statement.setString(5, customer.getAddress());
            statement.setString(6, customer.getCity());
            statement.setString(7, customer.getState());
            statement.setString(8, customer.getPostalCode());
            statement.setString(9, customer.getGender().toString());
            statement.setInt(10, customer.getCustomerID());

            statement.execute();
            flag = true;

        } catch (SQLException e) {
            log.error(e);
            flag = false;
        }
        return flag;
    }

    @Override
    public ArrayList<Pet> getPets() {
        ArrayList<Pet> pets = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(SELECT_PETS)) {

            ResultSet set = statement.executeQuery();
            while (set.next())
                pets.add(getPetFromSet(set));

        } catch (SQLException e) {
            log.error(e);
        }
        return pets;
    }

    @Override
    public ArrayList<Pet> getPetsFromCustomer(Customer customer) {
        ArrayList<Pet> pets = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(SELECT_PETS_BY_CUSTOMER)) {
            statement.setInt(1, customer.getCustomerID());
            ResultSet set = statement.executeQuery();
            while (set.next())
                pets.add(getPetFromSet(set));

        } catch (SQLException e) {
            log.error(e);
        }
        return pets;
    }

    @Override
    public Pet getPetByID(int petID) {
        Pet pet = null;
        try (PreparedStatement statement = connection.prepareStatement(SELECT_PET_BY_ID)) {
            statement.setInt(1, petID);
            ResultSet set = statement.executeQuery();
            if (set.next())
                pet = getPetFromSet(set);

        } catch (SQLException e) {
            log.error(e);
        }
        return pet;
    }

    @Override
    public boolean addPet(Customer customer, Pet pet) {
        boolean flag = false;
        try (PreparedStatement statement = connection.prepareStatement(ADD_PET)) {
        //CustomerID, PetName, Gender, Breed, BehaviorDescription
            statement.setInt(1, customer.getCustomerID());
            statement.setString(2, pet.getName());
            statement.setString(3, pet.getGender().toString());
            statement.setString(4, pet.getBreed());
            statement.setString(5, pet.getBehaviourDescription());
            statement.execute();
            flag = true;
        } catch (SQLException e) {
            log.error(e);
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean removePet(int id) {
        boolean flag = false;
        try (PreparedStatement statement = connection.prepareStatement(REMOVE_PET)) {
            statement.setInt(1, id);
            statement.execute();
            flag = true;
        } catch (SQLException e) {
            log.error(e);
            flag = false;
        }
        return flag;
    }

    public boolean removePetsFromCustomer(Customer customer) {
        boolean flag = false;
        try (PreparedStatement statement = connection.prepareStatement(REMOVE_PETS_BY_CUSTOMER)) {
            statement.setInt(1, customer.getCustomerID());
            statement.execute();
            flag = true;
        } catch (SQLException e) {
            log.error(e);
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean updatePet(Pet pet) {
        boolean flag = false;
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_PET)) {
            //CustomerID, PetName, Gender, Breed, BehaviorDescription
            statement.setString(1, pet.getName());
            statement.setString(2, pet.getGender().toString());
            statement.setString(3, pet.getBreed());
            statement.setString(4, pet.getBehaviourDescription());
            statement.setInt(5, pet.getPetID());
            statement.setInt(6, pet.getCustomerID());
            statement.execute();
            flag = true;
        } catch (SQLException e) {
            log.error(e);
            flag = false;
        }
        return flag;
    }
}
