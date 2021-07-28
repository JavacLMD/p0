package domain;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {


    private static Logger log = LoggerFactory.getLogger(Database.class);
    private static final String localURL = "jdcb:sqlite:PetDatabase.db";





    private Connection connection;




    public Connection connect() {
        try {
            connection = DriverManager.getConnection(localURL);
        } catch (SQLException sqlException) {

        }


    }





}
