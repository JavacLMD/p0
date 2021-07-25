import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class PetManagement {

    public static String fileURL = "PetCustomers.db";

    public String createCustomersTable = "create table IF NOT EXISTS customers (" +
            "CustomerID INTEGER PRIMARY KEY NOT NULL, " +
            "FirstName VARCHAR(45), " +
            "LastName VARCHAR(45), " +
            "PhoneNumber VARCHAR(15)" +
            ")";

    public String createPetsTable = "create table IF NOT EXISTS pets (" +
                    "PetID INTEGER PRIMARY KEY NOT NULL, " +
                    "CustomerID int NOT NULL, " +
                    "PetName VARCHAR(45), " +
                    "BehaviourDescription VARCHAR(255), " +
                    "FOREIGN KEY (CustomerID) references customers(CustomerID)" +
                    ")";

    public static void main(String[] args) {

        PetManagement management = new PetManagement();
        ArrayList<Customer> customers = new ArrayList<>();

        Connection connection = null;
        try {
            //establish connection to database
            connection = connect(fileURL);

            Statement statement = connection.createStatement();

            //create required tables
            statement.execute(management.createCustomersTable);
            statement.execute(management.createPetsTable);

            try (Scanner input = new Scanner(System.in)) {

                boolean flag = true;
                while (flag) {
                    System.out.println("Commands: q - quit | add - Adds new customer | print - list all customers");
                    System.out.print("Enter Command: ");
                    String commandInput = input.nextLine();

                    if (commandInput.equals("q")) {
                        flag = false;
                    } else if (commandInput.equals("add")) {

                        System.out.print("\nPlease enter first name, last name, and phone number: ");

                        String entry = input.nextLine();
                        String[] s = entry.split(" "); // \\s+


                        PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO customers (FirstName, LastName, PhoneNumber) Values (?,?,?)");

                        insertStatement.setString(1, s[0]);
                        insertStatement.setString(2, s[1]);
                        insertStatement.setString(3, s[2]);

                        insertStatement.execute();

                    } else if (commandInput.equals("print")) {

                        customers.clear();
                        var resultSet = statement.executeQuery("SELECT * FROM customers");

                        while (resultSet.next()) {
                            int id = resultSet.getInt("CustomerID");
                            String fname = resultSet.getString("FirstName");
                            String lname = resultSet.getString("LastName");
                            String number = resultSet.getString("PhoneNumber");

                            customers.add(new Customer(id, fname, lname, number));
                        }
                        System.out.println("\n--------------------------------------------------");
                        for (var c : customers) {
                            System.out.println(c);
                        }
                        System.out.println("--------------------------------------------------\n");
                    }
                }


            } catch (Exception e) {
                System.err.println(e.getMessage());

            }


        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    public static Connection connect(String fileName) throws SQLException
    {
        String url = "jdbc:sqlite:" + fileName;
        Connection connection = DriverManager.getConnection(url);
        //System.out.println("Connection has been established.");
        return connection;
    }


}