import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CustomerDatabase {

    public static final String localConnectionURL = "jdbc:sqlite:PetDatabase.db";


    private static Connection connection = null;



    public static Connection connect() {

        try {
            //return existing connection
            if (connection != null) return connection;

            connection = DriverManager.getConnection(localConnectionURL);
        } catch (SQLException e) {

            //Log error
            System.err.println("Failed to establish the database connection!");
        }

        return connection;
    }

    public static void disconnect() {

        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println("Unable to close connection!\n" + e.getMessage());
        }
    }











}
