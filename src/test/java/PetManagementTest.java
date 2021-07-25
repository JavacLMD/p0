import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

public class PetManagementTest extends TestCase {

    @Test
    public void testConnection() {
        //PetManagement management = new PetManagement();
        String url = PetManagement.fileURL;
        Connection connection = null;

        try {
            connection = PetManagement.connect(url);
            System.out.println("Connection to database has been established.");
        } catch (SQLException e) {
            System.err.println("Connection to database has failed.");
        } finally {
            Assert.assertNotNull(connection);
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {

            }
        }

    }
}