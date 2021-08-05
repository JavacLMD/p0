package com.github.JavacLMD.projectZero.view;

import com.github.JavacLMD.StringUtils;
import com.github.JavacLMD.projectZero.controller.DOA;
import com.github.JavacLMD.projectZero.model.Customer;
import com.github.JavacLMD.projectZero.model.Gender;
import com.github.JavacLMD.projectZero.model.Pet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Scanner;

public class CommandInterface implements Interface{
    private static final Scanner scanner = new Scanner(System.in);
    private static final Logger log = LogManager.getLogger(CommandInterface.class.getName());

    private final DOA dataAccessor;
    private Customer selectedCustomer;
    private Pet selectedPet;

    public CommandInterface(DOA dataAccessor) {
        this.dataAccessor = dataAccessor;
    }

    public void run() {

        boolean flag = true;
        do {
            String[] commands = {"Search", "Select", "Add", "Remove","Quit"};
            int command = promptMenu("\nPlease type a command:", commands);

            switch (command) {
                case 0: //"search": //search
                    doSearch();
                    break;
                case 1: //"select": //select
                    promptSelectionMenu();
                    break;
                case 2: //"add": //add customer
                    selectedCustomer = promptAddCustomer();
                    break;
                case 3: //"remove": //remove customer
                    if (selectedCustomer == null)
                        selectCustomer();
                    selectedCustomer = promptRemoveCustomer(selectedCustomer);
                    break;
                default: //quit
                    flag = false;
                    break;
            }
        } while (flag);

        scanner.close();
    }

    private Customer promptRemoveCustomer(Customer selectedCustomer) {
        //warn the user of deletion
        System.out.println("This will delete all associated entries!");
        //get confirmation
        if (inputConfirmation("Are you sure you want to delete the selected customer?")) {
            //attempt to remove the customer
            boolean success = dataAccessor.removeCustomer(selectedCustomer.getCustomerID());
            //if successful, "deselect" the customer and pet
            if (success) {
                System.out.println("Customer " + " " + selectedCustomer.getEmailAddress() + " deleted.");
                selectedCustomer = null;
            } else {
                System.out.println("Customer " + selectedCustomer.getEmailAddress() + " could not be deleted.");
                log.error("Could not delete customer " + selectedCustomer.getEmailAddress() + "!");
            }
        }

        return selectedCustomer;
    }

    private Customer promptAddCustomer() {
        Customer customer = promptCustomerInfo("What do you want to add?", null);
        String name = customer.getFirstName() + " " + customer.getLastName();

        if (dataAccessor.addCustomer(customer)) {
            log.info(name + " successfully added!");
            customer = dataAccessor.getCustomerByEmail(customer.getEmailAddress());
        } else
            log.error("Customer wasn't added!");

        return customer;
    }

    private void doSearch() {
        String[] searchArgs = {"Customers", "Pets", "Exit"};
        boolean flag = true;
        while (flag) {
            int command = promptMenu("What do you want to search?", searchArgs);
            switch (command) {

                case 0: //"customers":
                    promptSearchCustomers();
                    break;
                case 1: //"pets":
                    searchPets();
                    break;
                default: //"exit":
                    log.info("Exited Search Function!");
                    flag = false;
                    break;
            }
        }
    }

    private void promptSelectionMenu() {
        String[] searchArgs = {"Customers", "Pets", "Exit"};
        boolean flag = true;

        while (flag) {
            int command = promptMenu("What do you want to select?", searchArgs);
            switch (command) {
                case 0: //"customers":
                    handleCustomerSelection();
                    break;
                case 1: //"pets":
                    handlePetSelection();
                    break;
                default: //"exit":
                    log.info("Exited Select Function!");
                    flag = false;
                    break;
            }
        }
    }

    private void handlePetSelection() {
        selectedPet = promptPetSelection();
        if (selectedPet == null) {
            System.out.println("Pet not selected!");
            log.error("Pet not selected!");
            return;
        }

        String[] args = {"Update Pet", "Delete Pet", "Print", "Exit" };

        boolean flag = true;
        while(flag) {
            int command = promptMenu("Please type a command:", args);
            switch (command) {
                case 0: //"update pet":
                    selectedPet = promptUpdatePet(selectedPet);
                    break;
                case 1: //"delete pet":
                    selectedPet = promptPetDeletion(selectedPet);
                    break;
                case 2: //"print":
                    if (selectedPet != null)
                        printPets(selectedPet);
                    break;
                default: //exit
                    flag = false;
                    break;
            }
        }
    }

    private Pet promptPetDeletion(Pet selectedPet) {
        String petName = selectedPet.getName();
        int petID = selectedPet.getPetID();

        //warn the user of deletion
        System.out.println("This will delete the pet!");
        //get confirmation
        if (inputConfirmation("Are you sure you want to delete the selected pet?")) {
            //attempt to remove the customer
            boolean success = dataAccessor.removePet(petID);

            if (success) {
                log.info("Pet: " + petID + ": " + petName + " deleted.");
                selectedPet = null; //return null since pet no longer exists
            } else {
                log.error("Pet " + petID + ": " + petName + " could not be deleted.");
            }
        }
        return selectedPet;
    }

    //select any pet with id
    private Pet promptPetSelection() {
        if (selectedPet != null) {
            String sb = "Pet: " +
                    selectedPet.getName() +
                    " is selected!";

            System.out.println("Pet " + sb);
            if (!inputConfirmation("Do you wish to select a new pet?")) {
                log.debug("User kept current selection");
                return selectedPet;
            }
        }

        boolean flag = true;
        do {
            System.out.print("Please type in id or 'exit' to cancel >> ");
            String input = scanner.nextLine().toLowerCase();

            if (StringUtils.isInteger(input)) {
                selectedPet = dataAccessor.getPetByID(Integer.parseInt(input));
            } else if ("exit".equalsIgnoreCase(input) || "quit".equalsIgnoreCase(input)) {
                return selectedPet;
            }

            if (selectedPet != null) flag = false;
            else System.out.println("Invalid entry! Try again!");
        } while (flag);

        printPets(selectedPet);

        return selectedPet;
    }

    private Pet promptUpdatePet(Pet selectedPet) {


        Pet pet = promptPetInfo("What do you want to update?", selectedPet);
        if (selectedPet != null) {
            selectedPet.setName(pet.getName());
            selectedPet.setBreed(pet.getBreed());
            selectedPet.setGender(pet.getGender());
            selectedPet.setBehaviourDescription(selectedPet.getBehaviourDescription());

            if (dataAccessor.updatePet(selectedPet)) {
                log.info("Pet profile: " + selectedPet.getName() + " updated successfully!");
            }
        }
        return selectedPet;
    }

    private Pet promptSelectCustomerPet(Customer selectedCustomer) {
        ArrayList<Pet> customerPets = dataAccessor.getPetsFromCustomer(selectedCustomer);
        ArrayList<Integer> associatedIDs = new ArrayList<>();
        String fullName = selectedCustomer.getFirstName() + " " + selectedCustomer.getLastName();

        if (customerPets.size() == 0 || customerPets.size() == 1 && customerPets.get(0) == null) {
            System.out.println(fullName + " does not have any pets!");
            return null;
        }

        boolean flag = true;
        do {
            System.out.println("\nAvailable Pets for " + fullName + ":");
            associatedIDs.clear();
            for (Pet x : customerPets)
            {
                System.out.println("PetID: " + x.getPetID() + " | Name: " + x.getName());
                associatedIDs.add(x.getPetID());
            }
            System.out.print("\nWhat pet do you want to select? >>  ");
            String input = scanner.nextLine();
            int id;
            if (StringUtils.isInteger(input)) {
                id = Integer.parseInt(input);

                boolean isAcceptable = false;
                for (int s : associatedIDs) {
                    if (s == id) {
                        isAcceptable = true;
                        break;
                    }
                }

                if (isAcceptable) {
                    selectedPet = dataAccessor.getPetByID(id);
                    log.info("Pet " + selectedPet.getName() + " selected from " + fullName + "!");
                    flag = false;
                } else {
                    System.out.println("Id is out of range for " + fullName + "'s pets");
                }
            } else if ("exit".equalsIgnoreCase(input)) {
                log.warn("User exited pet selection for " + fullName);
                selectedPet = null;
                flag = false;
            }
        } while (flag);

        printPets(selectedPet);

        return selectedPet;
    }

    //region Selection Stuff
    private void handleCustomerSelection() {
        selectedCustomer = selectCustomer();
        if (selectedCustomer == null) {
            System.out.println("Customer not selected!");
            log.error("Customer not selected!");
            return;
        }

        boolean selectionFlag = true;
        do {
            String[] args = {"Update Profile", "Delete Profile", "Print Profile", "Print Pets", "Add Pet", "Remove Pet", "Update Pet", "Exit" };
            int command = promptMenu("Please type a command:", args);

            switch (command) {
                case 0: //"update profile":
                    updateCustomerProfile(selectedCustomer);
                    break;
                case 1: //"delete profile":
                    selectedCustomer = deleteCustomerProfile(selectedCustomer);
                    if (selectedCustomer == null) selectionFlag = false;
                    break;
                case 2: //"print profile":
                    printCustomers(selectedCustomer);
                    break;
                case 3: //"print pets":
                    printPetsFromCustomer(selectedCustomer);
                    break;
                case 4: //"add pet":
                    addPetToCustomer(selectedCustomer);
                    break;
                case 5: //"remove pet":
                    selectedPet = removePetFromCustomer(selectedCustomer);
                    break;
                case 6: //"update pet":
                    Pet customerPet = promptSelectCustomerPet(selectedCustomer);
                    selectedPet = promptUpdatePet(customerPet);
                    break;
                default: //exit
                    selectionFlag = false;
                    break;
            }
        } while (selectionFlag);

    }

    private Customer selectCustomer() {
        if (selectedCustomer != null) {
            String sb = "Customer: " +
                    selectedCustomer.getFirstName() + " " + selectedCustomer.getLastName() +
                    " is selected!";

            System.out.println("Customer " + sb);
            if (!inputConfirmation("Do you wish to select a new customer?")) {
                log.debug("User kept current selection");
                return selectedCustomer;
            }
        }

        boolean flag = true;
        do {
            System.out.print("Please type in id, email, or 'exit' to cancel >> ");
            String input = scanner.nextLine().toLowerCase();

            if (StringUtils.isEmail(input)) {
                selectedCustomer = dataAccessor.getCustomerByEmail(input);
            } else if (StringUtils.isInteger(input)) {
                selectedCustomer = dataAccessor.getCustomerByID(Integer.parseInt(input));
            } else if ("exit".equalsIgnoreCase(input) || "quit".equalsIgnoreCase(input) || input.isEmpty()) {
                return selectedCustomer;
            }

            if (selectedCustomer != null) flag = false;
            else System.out.println("Invalid entry! Try again!");
        } while (flag);

        printCustomers(selectedCustomer);

        return selectedCustomer;
    }

    private Pet removePetFromCustomer(Customer selectedCustomer) {
        selectedPet = promptSelectCustomerPet(selectedCustomer);
        String fullName = selectedCustomer.getFirstName() + " " + selectedCustomer.getLastName();
        System.out.println("Selected pet:");
        printPets(selectedPet);

        System.out.println();
        if (inputConfirmation("Are you sure you want to remove the pet?")) {

            if (dataAccessor.removePet(selectedPet.getPetID())) {
                log.info(selectedPet.getName() + " has been removed from " + fullName + "!");
                selectedPet = null;
            } else {
                log.error(selectedPet.getName() + " could not be deleted from " + fullName + "!");
            }
        } else {
            log.warn(selectedPet.getName() + " is safe from deletion");
        }

        return selectedPet;
    }

    //updates the selected profile
    private void updateCustomerProfile(Customer selectedCustomer) {
        //prevent updating without a selected customer
        if (selectedCustomer == null) {
            log.error("Customer not selected!");
            return; //customer not selected
        }

        //create an empty placeholder for the customer's updated info, passing in the original values
        Customer updatedCustomer = promptCustomerInfo("What do you want to update?", selectedCustomer);

        //update the customer's values
        selectedCustomer.setFirstName(updatedCustomer.getFirstName());
        selectedCustomer.setLastName(updatedCustomer.getLastName());
        selectedCustomer.setEmailAddress(updatedCustomer.getEmailAddress());
        selectedCustomer.setPhoneNumber(updatedCustomer.getPhoneNumber());
        selectedCustomer.setGender(updatedCustomer.getGender());
        selectedCustomer.setAddress(updatedCustomer.getAddress());
        selectedCustomer.setCity(updatedCustomer.getCity());
        selectedCustomer.setState(updatedCustomer.getState());
        selectedCustomer.setPostalCode(updatedCustomer.getPostalCode());

        dataAccessor.updateCustomer(selectedCustomer); //update the table
    }

    private Customer deleteCustomerProfile(Customer selectedCustomer) {
        //prevent updating without a selected customer
        if (selectedCustomer == null) {
            log.error("Customer not selected!");
            return null; //customer not selected
        }

        String email = selectedCustomer.getEmailAddress();

        //warn the user of deletion
        System.out.println("This will delete all associated entries!");
        //get confirmation
        if (inputConfirmation("Are you sure you want to delete the selected customer?")) {
            //attempt to remove the customer
            boolean success = dataAccessor.removeCustomer(selectedCustomer.getCustomerID());
            //if successful, "deselect" the customer and pet
            if (success) {
                System.out.println("Customer " + " " + email + " deleted.");
                selectedPet = null;
                return null;
            } else {
                System.out.println("Customer " + " " + email + " could not be deleted.");
                log.error("Could not delete customer " + email + "!");
                return selectedCustomer;
            }
        }
        return selectedCustomer;
    }

    private void printPetsFromCustomer(Customer selectedCustomer) {
        ArrayList<Pet> pets = dataAccessor.getPetsFromCustomer(selectedCustomer);
        int size = pets.size();
        printPets(pets.toArray(new Pet[size]));
    }

    private void addPetToCustomer(Customer customer) {
        Pet pet = promptPetInfo("Please fill out the pet's profile:", null);

        if (pet != null) {
            if (dataAccessor.addPet(customer, pet)) {
                log.info("Pet " + pet.getName() + " added successfully");
            }
        }
    }

    //endregion

    //region Searching Stuff
    //Handles finding and printing pets to the terminal
    private void searchPets() {
        ArrayList<Pet> pets = new ArrayList<>();
        System.out.println("Please type in search criteria (leave blank for all): ");
        System.out.print("Examples (Email, id) >> ");
        String input = scanner.nextLine().toLowerCase();

        if (StringUtils.isEmail(input)) {
            //detected an email address in the string, so get all pets from the associated email
            log.debug("Getting pets associated with email: " + input);
            pets = dataAccessor.getPetsFromCustomer(dataAccessor.getCustomerByEmail(input));
        } else if (StringUtils.isInteger(input)) {
            //detected a number from the string, so get the pet associated with the id
            log.debug("Getting pet by id: " + input);
            pets.add(dataAccessor.getPetByID(Integer.parseInt(input)));
        } else {
            //no specific request has been detected, so print all pets that have been registered
            log.info("Getting all pets!");
            pets = dataAccessor.getPets();
        }

        //pet array will always have at least one entry, even if it's null
        if (pets.size() == 1 && pets.get(0) == null || pets.size() == 0) {
            String message = "No pet with matching results: " + input;
            log.error(message);
            return;
        }

        int size = pets.size();
        Pet[] asArray = pets.toArray(new Pet[size]);
        printPets(asArray);

    }

    private void promptSearchCustomers() {
        ArrayList<Customer> customers = new ArrayList<>();
        System.out.println("Please type in search criteria (leave blank for all): ");
        System.out.print("Examples (Email, id, first and/or last name) >> ");
        String input = scanner.nextLine().toLowerCase();

        if (StringUtils.isEmail(input)) {
            //detected the input is an email, add associated record
            customers.add(dataAccessor.getCustomerByEmail(input));
        } else if (StringUtils.isInteger(input)) {
            //detected the input was an integer id, add associated record
            customers.add(dataAccessor.getCustomerByID(Integer.parseInt(input)));
        } else if (!input.isEmpty()) {
            //assuming input string is a name, try splitting it for first and last name entries;
            String[] name = input.split(" ");
            customers = dataAccessor.getCustomersByName(name); //get list of customers with the matching names
        } else {
            //no search criteria passed, get all customer records
            customers = dataAccessor.getCustomers();
        }

        //prevent listing a single null value
        if (customers.size() == 1 && customers.get(0) == null) {
            String message = "No customer with matching results: " + input;
            log.warn(message);
            return;
        }

        int size = customers.size();
        printCustomers(customers.toArray(new Customer[size]));
    }

    //endregion

    //region Profile Prompts
    //asks the user for pet info
    public Pet promptPetInfo(String promptMessage, Pet pet) {
        String name = null, breed = null, behaviourDescription = null;
        Gender gender = Gender.Unspecified;

        if (pet != null) {
            name = pet.getName();
            breed = pet.getBreed();
            gender = pet.getGender();
            behaviourDescription = pet.getBehaviourDescription();
        }

        String[] profileOptions = {"Name", "Breed", "Gender", "Behaviour", "Finish"};
        boolean flag = true;
        while (flag) {
            int command = promptMenu(promptMessage, profileOptions);
            switch (command) {
                case 0:
                    name = promptPetName();
                    break;
                case 1:
                    breed = promptString("Please enter the breed: >>  ");
                    break;
                case 2:
                    gender = promptGender();
                    break;
                case 3:
                    behaviourDescription = promptString("Please enter the behaviour description: >>  ");
                    break;
                default:
                    if (name == null || name.isEmpty())
                        log.warn("Name cannot be blank!");

                    if (inputConfirmation("Are you sure you're finished?\n" +
                            "Changes will be discarded if the following are not completed: Name")) {
                        flag = false; //exit loop
                    }
                    break;
            }

            System.out.println("\nPet Profile: " +
                    "\n Name: " + name +
                    "\n Breed: " + breed +
                    "\n Gender: " + gender +
                    "\n Behaviour Description: " + behaviourDescription);

            if (flag)
                System.out.println("Type 'Finish' to finish populating pet.");
        }

        return new Pet(name, breed, gender, behaviourDescription);
    }
    //asks the user for customer info
    public Customer promptCustomerInfo(String promptMessage, Customer customer) {

        String firstName = null, lastName = null, email = null, phone = null,
                address = null, city = null, state = null, zip = null;
        Gender gender = Gender.Unspecified;
        if (customer != null) {
            firstName = customer.getFirstName();
            lastName = customer.getState();
            email = customer.getEmailAddress();
            phone = customer.getPhoneNumber();
            address = customer.getAddress();
            city = customer.getCity();
            state = customer.getState();
            zip = customer.getPostalCode();
            gender = customer.getGender();
        }

        String[] profileOptions = {"Name", "Email", "Phone Number","Gender", "Address", "Finish"};
        boolean flag = true;
        while (flag) {
            int input = promptMenu(promptMessage, profileOptions);
            switch (input) {
                case 0: //name
                    String[] name = promptCustomerName();
                    firstName = name[0];
                    lastName = name[1];
                    break;
                case 1: //email
                    email = promptCustomerEmail();
                    break;
                case 2: //phone number
                    phone = promptCustomerPhone();
                    break;
                case 3: //gender
                    gender = promptGender();
                    break;
                case 4: //address
                    do {
                        System.out.print("Please enter address: >>  ");
                        address = scanner.nextLine();
                    } while (!inputConfirmation("Is this the right address? " + address));
                    do {
                        System.out.print("Please enter city: >>  ");
                        city = scanner.nextLine();
                    } while (!inputConfirmation("Is this the right city? " + city));
                    do {
                        System.out.print("Please enter state: >>  ");
                        state = scanner.nextLine();
                    } while (!inputConfirmation("Is this the right state? " + state));
                    do {
                        System.out.print("Please enter postal code: >>  ");
                        zip = scanner.nextLine();
                    } while (!inputConfirmation("Is this the right postal code? " + zip));
                    break;
                default: //finish
                    if ((firstName == null || firstName.isEmpty()) ||
                            (lastName == null || lastName.isEmpty()) ||
                            (email == null || email.isEmpty())) {
                        log.error("First name, last name, and email cannot be empty!");
                    }
                    if (inputConfirmation("Are you sure you're finished?\n" +
                            "Changes will be discarded if the following are not completed: Name & Email")) {
                        flag = false; //exit loop
                    }
                    break;
            }

            System.out.println("\nCustomer Information: " +
                    "\n First Name: " + firstName +
                    "\n Last Name: " + lastName +
                    "\n Email Address: " + email +
                    "\n Phone Number: " + phone +
                    "\n Gender: " + gender.toString() +
                    "\n Address: " + address +
                    "\n City: " + city +
                    "\n State: " + state +
                    "\n Postal Code: " + zip
            );

            if (flag)
                System.out.println("Type 'Finish' to finish populating customer.");
        }
        log.info("Returning customer information");
        return new Customer(firstName, lastName, email, phone, gender, address, city, state, zip);
    }


    //prompts the user for a pet name and returns only if it's not blank
    private String promptPetName() {
        boolean flag = true;
        String name = null;
        while(flag) {
            System.out.print("Please enter the name: >>  ");
            name = scanner.nextLine();
            flag = name.isEmpty();
            if (flag) {
                log.warn("Name can't be blank!");
            }
        }
        return name;
    }
    //prompts the user for input and simply returns it
    private String promptString(String message) {
        System.out.print("\n" + message);
        return scanner.nextLine();
    }

    //prompts user for a first and last name and returns it as an array with 2 indexes
    private String[] promptCustomerName() {
        String[] name;
        do {
            System.out.print("\nPlease enter first and last name: >>  ");
            String input = scanner.nextLine();
            //separate first and last name to different strings
            name = input.split(" ");
            if (name.length != 2) {
                System.out.println("Did not get first and last name! Try again.");
            }
        } while(name.length != 2);
        return name;
    }

    //prompts user for an email address and returns accepted input
    private String promptCustomerEmail() {
        boolean flag = true;
        String input = "", email;
        while (flag) {
            System.out.print("\nPlease enter in new email: >>  ");
            input = scanner.nextLine();
            System.out.print("Please confirm the new email: >>  ");
            email = scanner.nextLine();

            //do the email entries match
            if (input.equalsIgnoreCase(email)) {
                //are both entries emails
                if (StringUtils.isEmail(input) && StringUtils.isEmail(email)) {
                    //check if email already exists in registry
                    Customer check = dataAccessor.getCustomerByEmail(input);
                    if (check == null || check.equals(selectedCustomer)) {
                        flag = false;
                        log.info("Email " + email + " accepted!");
                    } else
                        log.error("Email is already in use!");
                } else
                    log.error("Not a valid email address! " + input);
            } else
                log.error("Emails do not match! " + input + " vs " + email);
        }
        return input;
    }
    //prompts user for a phone number and returns accepted input
    private String promptCustomerPhone() {
        boolean flag = true;
        String input = "";
        while (flag) {
            System.out.print("\nPlease enter phone number: >>  ");
            input = scanner.nextLine();

            if (input.toCharArray().length < 15) {
                flag = false;
                log.info("Phone number accepted: " + input + "!");
            } else
                log.error("Phone number too long! Try again.");
        }
        return input;
    }
    //prompts user for a specified gender and returns it
    private Gender promptGender() {
        int selection;
        String[] genderOptions = {
                Gender.Unspecified.toString(),
                Gender.Male.toString(),
                Gender.Female.toString(),
                Gender.Other.toString()
        };

        Gender gender;
        selection = promptMenu("Please select a gender:", genderOptions);

        switch (selection) {
            case 1: //Male
                gender = Gender.Male;
                break;
            case 2: //Female
                gender = Gender.Female;
                break;
            case 3: //Other
                gender = Gender.Other;
                break;
            default: //Unspecified
                gender = Gender.Unspecified;
                break;
        }
        return gender;
    }
    //endregion

    //Handles printing customers
    public static void printCustomers(Customer ...customers) {
        //print found records
        if (customers.length > 0) {
            System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------------");
            for (Customer customer : customers)
                System.out.println(customer);
            System.out.println("---------------------------------------------------------------------------------------------------------------------------------------\n");
        }
    }

    //Handles printing pets
    public static void printPets(Pet ...pets) {
        //print found records
        if (pets.length > 0) {
            System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------------");
            for (Pet pet : pets)
                System.out.println(pet);
            System.out.println("---------------------------------------------------------------------------------------------------------------------------------------\n");
        }
    }

    //prompts the user for a string input
    private static int promptMenu(String message, String ...commandArgs) {
        StringBuilder sb = new StringBuilder();
        sb.append("Options: ");
        for (int i =0; i < commandArgs.length; i++) {
            if (i%3 == 0 && i > 0)
                sb.append("\n          ");
            sb.append(String.format("%s [%d] ", commandArgs[i], i));

            if (i != commandArgs.length - 1) sb.append("| ");
        }
        String options = sb.toString();

        while (true) {
            System.out.print("\n" + message + "\n " + options + "\n >>  ");
            String prompt = scanner.nextLine().toLowerCase();
            int command = StringUtils.isInteger(prompt) ? Integer.parseInt(prompt) : -1;

            for (int i = 0; i < commandArgs.length; i++) {
                if (prompt.equalsIgnoreCase(commandArgs[i]) || command == i)
                {
                    log.debug("Prompt Confirmed: " + commandArgs[i]);
                    //return commandArgs[i]; return the argument
                    return i; //return the corresponding number for the option
                }
            }
            System.out.println("Unrecognized Command > " + prompt + ". Try again!");
        }
    }

    private static boolean inputConfirmation(String message) {

        while (true) {
            System.out.print(message + "\n  [Y]yes or [N]no >>  ");
            String input = scanner.nextLine().toLowerCase();

            switch (input) {
                case "y": case "yes": case "0":
                    log.debug("User selected 'Yes' to " + message);
                    return true;
                case "n": case "no": case "1":
                    log.debug("User selected 'No' to " + message);
                    return false;
            }
        }
    }


}
