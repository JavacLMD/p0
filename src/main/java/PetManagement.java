import com.github.JavacLMD.projectZero.Context;
import com.github.JavacLMD.projectZero.Customer;
import com.github.JavacLMD.projectZero.Database;

public class PetManagement {
    private static Database database;

    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306";
        String username = "root";
        String password = "";

        Context context = new Context("-d", url);

        for (Customer c : context.customers.values()) {
            System.out.println(c);
        }


    }



}
