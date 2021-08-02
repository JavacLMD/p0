import com.github.JavacLMD.projectZero.Config;
import com.github.JavacLMD.projectZero.Context;
import com.github.JavacLMD.projectZero.controller.IAccessor;
import com.github.JavacLMD.projectZero.model.Customer;
import com.github.JavacLMD.projectZero.model.Pet;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PetManagement {

    public static void main(String[] args) {

        Context context = new Context(args);
        Config config = context.getAppConfig();
        IAccessor data = context.getData();

        System.out.println("-------------------------------------------------------------");
        if (config.doCustomerSearch()) {
            List<Customer> customers = new ArrayList<>();

            String customerName = config.getCustomerName();
            String email = config.getCustomerEmail();

            if (customerName != null && customerName.isEmpty() == false) {
                customers = data.getCustomersByName(customerName.split(" "));
            } else if (email != null && email.isEmpty() == false) {
                customers.add(data.getCustomerByEmail(email));
            } else {
                customers = data.getAllCustomers();
            }

            for (Customer c : customers) {
                System.out.println(c);
            }
            System.out.println("-------------------------------------------------------------");
        }

        if (config.doPetSearch()) {
            List<Pet> pets = new ArrayList<>();
            String petName = config.getPetName();
            String email = config.getCustomerEmail();
            int petID = config.getPetID();

            if (petName != null && petName.isEmpty() == false) {
                pets = data.getPetsByName(petName);
            } else if (email != null && email.isEmpty() == false) {
                pets = data.getPetsByCustomer(data.getCustomerByEmail(email));
            } else if (petID > -1) {
                pets.add(data.getPetByID(petID));
            } else {
                pets = data.getAllPets();
            }

            for (Pet p : pets) {
                System.out.println(p);
            }
            System.out.println("-------------------------------------------------------------");
        }


    }

    private static void printCustomers(List<Customer> list) {

    }


}
