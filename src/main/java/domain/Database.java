package domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class Database {

    private static final Logger log = LoggerFactory.getLogger(Database.class);

    private static final String createCustomersTableSQL =
            "CREATE TABLE IF NOT EXISTS customers (" +
            "CustomerID INTEGER PRIMARY KEY NOT NULL," +
            "FirstName VARCHAR(50) NOT NULL," +
            "LastName VARCHAAR(50) NOT NULL," +
            "EmailAddress VARCHAR(100) NOT NULL," +
            "PhoneNumber VARCHAR(15)," +
            "Address VARCHAR(255), " +
            "City VARCHAR(75), State VARCHAR(75), PostalCode VARCHAR(10), " +
            "UNIQUE (EmailAddress)" +
            ")";

    private static final String createPetsTableSQL =
            "CREATE TABLE IF NOT EXISTS pets (" +
            "PetID INTEGER PRIMARY KEY NOT NULL," +
            "CustomerID INTEGER NOT NULL," +
            "PetName VARCHAR(50) NOT NULL," +
            "BehaviourDescription VARCHAR(255)," +
            "FOREIGN KEY (CustomerID) references customers(CustomerID)" +
            ")";

    private static  final String insertIntoCustomersSQL =
            "INSERT INTO customers " +
            "(FirstName, LastName, EmailAddress, PhoneNumber, Address, City, State, PostalCode)" +
            "VALUES " +
            "(?,?,?,?,?,?,?,?)";


    private final String url;
    private Connection connection;

    private ArrayList<Customer> customers = new ArrayList<>();

    public ArrayList<Customer> getCustomers() { return customers; }

    public static Database create(String sqlURL) {
        return new Database(sqlURL);
    }

    private Database(String url) {
        this.url = url;
        try (Statement s = getConnection().createStatement()) {
            s.execute(createCustomersTableSQL);
            log.info("Acquired customers table!");
            s.execute(createPetsTableSQL);
            log.info("Acquired pets table!");

            ResultSet set = s.executeQuery("SELECT * FROM customers");
            while (set.next()) {

                int customerID = set.getInt("CustomerID");
                String fName = set.getString("FirstName");
                String lname = set.getString("LastName");
                String phone = set.getString("PhoneNumber");
                //String g = set.getString("Gender");
                String email = ""; //set.getString("EmailAddress");
                String address = "";//set.getString("Address");
                String city = "";//set.getString("City");
                String state = "";//set.getString("State");
                String postalCode = ""; //set.getString("PostalCode");


                Gender gender = null;
                try {
                    //gender = Gender.valueOf(g);
                } finally {
                    if (gender == null) gender = Gender.Unspecified;
                }

                Customer customer = new Customer(
                        customerID,
                        fName,
                        lname,
                        email,
                        phone,
                        gender,
                        address,
                        city,
                        state,
                        postalCode
                );

                PreparedStatement petStatement = getConnection().prepareStatement("SELECT * FROM pets WHERE CustomerID = ?");
                petStatement.setInt(1, customer.getID());
                ResultSet petResults = petStatement.executeQuery();

                while (petResults.next()) {
                    int petID = petResults.getInt("PetID");
                    String petName = petResults.getString("PetName");
                    String breed = petResults.getString("Breed");
                    Date birthDate = petResults.getDate("BirthDate");
                    //String petGenderString = petResults.getString("Gender");
                    String behaviourDescription = petResults.getString("BehaviourDescription");


                    Gender petGender = null;
                    try {
                        //petGender = Gender.valueOf(petGenderString);
                    } finally {
                        if (petGender == null) petGender = Gender.Unspecified;
                    }

                    Pet pet = new Pet(petID, petName, breed, birthDate, petGender, behaviourDescription);
                    customer.addPet(pet);
                }

                customers.add(customer);
            }

        }catch (SQLException e) {
            log.error(e.getMessage());
        }
    }


    private Connection getConnection() {
        if (connection == null) {
            try {
                this.connection = DriverManager.getConnection(this.url);
                log.info("Established connection to database: " + this.url);
            } catch (SQLException e) {
                log.error(e.getMessage());
            }
        }
        return this.connection;
    }

    public void close() {
        try {
            if (connection != null)
                connection.close();
        } catch (SQLException e) {
            log.error("Unable to close!\n" + e.getMessage());
        }
    }




    private PreparedStatement prepareStatement(String sql) {
        try {
            PreparedStatement statement = getConnection().prepareStatement(sql);
            log.debug("Prepare Statement created!");
            return statement;
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    public boolean addCustomer(Customer customer) {

        try (PreparedStatement statement = getConnection().prepareStatement(insertIntoCustomersSQL)) {

            statement.setString(1, customer.getFirstName());
            statement.setString(2, customer.getLastName());
            statement.setString(3, customer.getEmailAddress());
            statement.setString(4, customer.getPhoneNumber());
            statement.setString(5, customer.getAddress());
            statement.setString(6, customer.getCity());
            statement.setString(7, customer.getState());
            statement.setString(8, customer.getPostalCode());

            return true;

        } catch (SQLException e) {
            log.warn("Could not add customer: " + customer.getFirstName() + " " + customer.getLastName());
            return false;
        }
    }

    public boolean addPet(Pet pet) {
        try {
            PreparedStatement statement = getConnection().prepareStatement("INSERT INTO pets " +
                    "(PetName, Breed, BirthDate, Gender, BehaviourDescription) " +
                    "VALUES" +
                    "(?,?,?,?,?)"
            );

            statement.setString(1, pet.getName());
            statement.setString(2, pet.getBreed());
            statement.setDate(3, pet.getBirthDate());
            statement.setString(4, pet.getGender().toString());
            statement.setString(5, pet.getBehaviourDescription());

            statement.execute();

            return true;
        } catch (SQLException e) {
            log.warn("Could not add pet " + pet.getName() + '|' + e.getMessage());
            return false;
        }
    }



}
