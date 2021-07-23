import java.sql.*;

public class PetManagement {

    public static String fileURL = "jdbc:sqlite:PetCustomers.db";

    public String createCustomersTable = "create table IF NOT EXISTS customers (" +
            "CustomerID INT PRIMARY KEY," +
            "FirstName VARCHAR(45)," +
            "LastName VARCHAR(45)," +
            "PhoneNumber VARCHAR(10)" +
            ")";

    public String createPetsTable = "create table IF NOT EXISTS pets (" +
            "PetID INT," +
            "CustomerID INT," +
            "PetName VARCHAR(45)," +
            "BehaviourDescription VARCHAR(255)," +
            "PRIMARY KEY (PetID)," +
            "FOREIGN KEY (CustomerID) references customers(CustomerID)" +
            ")";

    public static void main(String[] args) {

        PetManagement management = new PetManagement();

        Connection connection = null;
        try {
            //establish connection to database
            connection = connect("PetCustomers.db");

            Statement statement = connection.createStatement();

            //create required tables
            statement.execute(management.createCustomersTable);
            statement.execute(management.createPetsTable);

            //statement.executeUpdate("insert into customers values (0, \"Lane\", \"Dorscher\", \"7122542249\")");



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