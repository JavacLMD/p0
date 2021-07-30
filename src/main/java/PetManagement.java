import com.github.JavacLMD.projectZero.Context;
import com.github.JavacLMD.projectZero.Customer;
import com.github.JavacLMD.projectZero.Database;

import java.util.Scanner;

public class PetManagement {
    private static Database database;

    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306";
        String username = "root";
        String password = "";

        Context context = new Context("-d", url,"--user", username);


        try (Scanner scanner = new Scanner(System.in)) {

            String input;
            boolean flag = false;
            while (flag == false) {

                System.out.print("Command >> ");
                input = scanner.nextLine();


            }


        } catch (Exception e) {

        }




    }



}
