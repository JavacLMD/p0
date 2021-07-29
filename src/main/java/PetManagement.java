import domain.Customer;
import domain.Database;
import domain.Pet;

import java.sql.Statement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.*;


public class PetManagement {
    private static Database database;

    public static void main(String[] args) {

        database = Database.create(args[0]);


        ArrayList<Customer> customers = database.getCustomers();

        for (Customer c : customers) {

            System.out.println(c);

        }


    }

}
