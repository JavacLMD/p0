import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PetManagement {

    public static String connectionName = "jdbc:sqlite:PetCustomers.db";



    public static void main(String[] args) {
        //establish data base connection
        try (Connection connection = DriverManager.getConnection(connectionName)) {

            connection.prepareStatement("create table customers (id INT PRIMARY");

        } catch (SQLException e){

        }
    }


}