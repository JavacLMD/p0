import java.sql.*;
import java.util.Locale;
import java.util.Scanner;

public class PetManagement {

    public static void main(String ...args) {

        try {
            Connection connection = CustomerDatabase.connect();
            Statement statement = connection.createStatement();

            //statement.execute("DROP TABLE IF EXISTS customers");

            createTables(statement);

            try (Scanner input = new Scanner(System.in)) {

                boolean flag = false;
                while (flag == false) {
                    System.out.println("\n-------------------------------------------------|");
                    System.out.println("| <> q = quit                                    |");
                    System.out.println("| <> add = insert customer                       |");
                    System.out.println("| <> remove = remove customer                    |");
                    System.out.println("| <> searchName = search customers by name       |");
                    System.out.println("| <> printAll - list all customers               |");
                    System.out.println("--------------------------------------------------\n");

                    System.out.print("Command >> ");
                    String consoleInput = input.nextLine();

                    if (consoleInput.equalsIgnoreCase("q"))
                        flag = true;
                    else if (consoleInput.equalsIgnoreCase("add")) {
                        addCustomer(connection, input);
                    } else if (consoleInput.equalsIgnoreCase("remove")) {
                        removeCustomer(connection, input);
                    } else if (consoleInput.equalsIgnoreCase("searchName")) {
                        ResultSet set = searchCustomersByName(connection, input);
                        printResultSet(set);
                    }
                    else if (consoleInput.equalsIgnoreCase("printAll")) {

                        ResultSet set = statement.executeQuery("SELECT * FROM customers");
                        //ArrayList<Customer> customers = new ArrayList<>();
                        printResultSet(set);
                    }
                }

            } catch (Exception e) {

            }

        } catch (SQLException e) {
            //log error
            System.err.println(e.getMessage());

        } finally {
            CustomerDatabase.disconnect();
        }

    }

    private static void printResultSet(ResultSet set) throws SQLException {
        while (set.next()) {
            int id = set.getInt("CustomerID");
            String fname = set.getString("FirstName");
            String lname = set.getString("LastName");
            String email = set.getString("EmailAddress");
            String phone = set.getString("PhoneNumber");
            //customers.add(new Customer(fname, lname, email, phone));

            System.out.printf("%s - %s %s - %s - %s\n", id, fname, lname, email, phone);
        }
    }


    private static void addCustomer(Connection connection, Scanner scanner) {
        boolean isGood = false;
        String fName;
        String lname;
        String email;
        String phone;

        do {
            System.out.println("Please insert first name, last name, email, and phone number");
            System.out.print("First Name     >> ");
            fName = scanner.nextLine();
            System.out.print("Last  Name     >> ");
            lname = scanner.nextLine();
            System.out.print("Email Address  >> ");
            email = scanner.nextLine();
            System.out.print("Phone Number   >> ");
            phone = scanner.nextLine();



            try {
                isGood = insertCustomer(connection, fName, lname, email, phone);
            } catch (Exception e) {
                isGood = false;
            } finally {
                if (isGood == false) {
                    System.out.println("\nEmail Address is already in use! Try again!\n");
                }
            }



        } while (isGood == false);

    }

    private  static void removeCustomer(Connection connection, Scanner scanner) {
        boolean flag = false;
        do {
            try {
                System.out.println("ID of customer to remove:");
                System.out.print("ID >> ");
                int customerID = Integer.parseInt(scanner.nextLine());

                PreparedStatement statement = connection.prepareStatement("DELETE FROM customers WHERE CustomerID = ?");
                statement.setInt(1, customerID);

                statement.execute();
                statement.close();
                flag = true;
            } catch (NumberFormatException numberFormatException) {
                System.out.println("Try typing a number:\n");
            } catch (SQLException sqlException) {
                System.out.println(sqlException.getMessage());
            }

        } while (flag == false);

    }

    private static ResultSet searchCustomersByName(Connection connection, Scanner scanner) {

        ResultSet set = null;
        PreparedStatement statement = null;

        try {

            boolean flag = false;
            String searchInput;
            do {
                System.out.println("\n-----------------------");
                System.out.print("Enter name >> ");
                searchInput = scanner.nextLine();
                String[] s = searchInput.split(" ");

                try {
                    String sql = "SELECT * FROM customers WHERE ";

                    if (s.length == 1) {
                        statement = connection.prepareStatement(sql + "(LOWER(FirstName) = ? OR LOWER(LastName) = ?)");
                        statement.setString(1, s[0].toLowerCase());
                        statement.setString(2, s[0].toLowerCase());
                    } else if (s.length == 2) {
                        statement = connection.prepareStatement(sql + "(LOWER(FirstName) = ? AND LOWER(LastName) = ?)");
                        statement.setString(1, s[0].toLowerCase());
                        statement.setString(2, s[1].toLowerCase());
                    }

                    set = statement.executeQuery();
                    flag = true;

                } catch (SQLException e) {
                    System.err.println(e.getMessage());
                }

            } while (flag == false);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            return set;
        }
    }


    //Insert customer into table (returns true if successful, false if failed by email address)
    private static boolean insertCustomer(Connection connection, String firstName, String lastName, String email, String phoneNumber) {

        try {
            PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO customers " +
                    "(FirstName, LastName, EmailAddress, PhoneNumber)" +
                    "VALUES (?,?,?,?)");
            insertStatement.setString(1, firstName);
            insertStatement.setString(2, lastName);
            insertStatement.setString(3, email);
            insertStatement.setString(4, phoneNumber);

            insertStatement.execute();
            insertStatement.close();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }


    private static void createTables(Statement statement ) {

        try {
            statement.execute("CREATE TABLE IF NOT EXISTS customers (" +
                    "CustomerID INTEGER PRIMARY KEY NOT NULL," +
                    "FirstName VARCHAR(50) NOT NULL," +
                    "LastName VARCHAAR(50) NOT NULL," +
                    "EmailAddress VARCHAR(100) NOT NULL," +
                    "PhoneNumber VARCHAR(15)," +
                    "UNIQUE (EmailAddress)" +
                    ")");

            statement.execute("CREATE TABLE IF NOT EXISTS pets (" +
                    "PetID INTEGER PRIMARY KEY NOT NULL," +
                    "CustomerID INTEGER NOT NULL," +
                    "PetName VARCHAR(50) NOT NULL," +
                    "BehaviourDescription VARCHAR(255)," +
                    "FOREIGN KEY (CustomerID) references customers(CustomerID)" +
                    ")");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }


}
