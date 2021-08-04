package com.github.JavacLMD.projectZero.view;

import com.github.JavacLMD.projectZero.StringUtils;
import com.github.JavacLMD.projectZero.controller.DOA;
import com.github.JavacLMD.projectZero.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class CommandInterface implements Interface{
    private static final Scanner scanner = new Scanner(System.in);
    private static final Logger log = LogManager.getLogger(CommandInterface.class);

    private DOA dataAccessor;
    private Customer selectedCustomer;
    private Pet selectedPet;

    public CommandInterface(DOA dataAccessor) {
        this.dataAccessor = dataAccessor;
    }

    public void run() {

        boolean flag = true;
        do {
            String[] commands = {"Search", "Select", "Add", "Remove","Quit"};
            String input = promptMenu("Please type a command:", commands).toLowerCase();

            switch (input) {
                case "search": //search
                    doSearch();
                    break;
                case "select": //select
                    doSelect();
                    break;
                case "add": //add customer
                    addCustomer();
                    break;
                case "remove": //remove customer
                    removeCustomer();
                    break;
                case "quit": //quit
                    flag = false;
                    break;
            }
        } while (flag);

        scanner.close();
    }

    private void removeCustomer() {
        selectedCustomer = null;
        selectCustomer();

        //warn the user of deletion
        System.out.println("This will delete all associated entries!");
        //get confirmation
        if (inputConfirmation("Are you sure you want to delete the selected customer?")) {
            //attempt to remove the customer
            boolean success = dataAccessor.removeCustomer(this.selectedCustomer.getCustomerID());
            //if successful, "deselect" the customer and pet
            if (success) {
                System.out.println("Customer " + " " + this.selectedCustomer.getEmailAddress() + " deleted.");
                this.selectedCustomer = null;
                selectedPet = null;
            } else {
                System.out.println("Customer " + this.selectedCustomer.getEmailAddress() + " could not be deleted.");
                log.error("Could not delete customer " + this.selectedCustomer.getEmailAddress() + "!");
            }
        }
    }

    private void addCustomer() {
        String[] profileOptions = {"Name", "Email", "Phone Number","Gender", "Address", "Finish"};
        boolean flag = true;
        String firstName = null,
                lastName= null,
                email= null,
                phone= null,
                address= null,
                city= null,
                state= null,
                zip= null,
                gender= Gender.Unspecified.toString();

        do {
            String input = promptMenu("What do you want to add?", profileOptions).toLowerCase();
            switch (input) {
                case "name":
                    do {
                        System.out.print("Please enter first and last name: >>  ");
                        input = scanner.nextLine();
                        //separate first and last name to different strings
                        String[] name = input.split(" ");
                        boolean caughtException = false;
                        try {
                            firstName = name[0];
                            lastName = name[1];
                        } catch (ArrayIndexOutOfBoundsException e) {
                            caughtException = true;
                            System.out.println("Did not get first and last name! Try again.\n");
                        }

                        //only confirm input if we got both first and last name
                        if (caughtException == false) {
                            flag = false;
                        }
                    } while(flag);
                    flag = true;
                    break;
                case "email":
                    do {
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
                                if (check == null) {
                                    email = input;
                                    flag = false;
                                    log.debug("Email " + email + " accepted!");
                                }
                            } else {
                                System.out.println("Input is not valid for email addresses! Try again.");
                                flag = true;
                            }
                        } else {
                            System.out.println("Emails do not match! Try again.");
                            flag = true;
                        }

                    } while(flag);
                    flag = true;
                    break;
                case "phone number":
                    do {
                        System.out.print("\nPlease enter phone number: >>  ");
                        phone = scanner.nextLine();

                        if (phone.toCharArray().length < 15) {
                            flag = false;
                        } else {
                            flag = true;
                            System.out.println("Phone number too long! Try again.");
                        }
                    } while (flag);
                    flag = true;
                    break;
                case "address":
                    do {
                        System.out.print("Please enter address: >>  ");
                        address = scanner.nextLine();
                    } while (inputConfirmation("Is this the right address? " + address) == false);
                    do {
                        System.out.print("Please enter city: >>  ");
                        city = scanner.nextLine();
                    } while (inputConfirmation("Is this the right city? " + city) == false);
                    do {
                        System.out.print("Please enter state: >>  ");
                        state = scanner.nextLine();
                    } while (inputConfirmation("Is this the right state? " + state) == false);
                    do {
                        System.out.print("Please enter postal code: >>  ");
                        zip = scanner.nextLine();
                    } while (inputConfirmation("Is this the right postal code? " + zip) == false);
                    break;
                case "gender":
                    gender = promptMenu("Please select a gender:", Gender.Male.toString(),
                            Gender.Female.toString(), Gender.Unspecified.toString(),
                            Gender.Other.toString());
                    break;
                case "finish":
                    flag = false;
                    break;
            }

            System.out.println("\nPlease confirm your profile: " +
                    "\n First Name: " + firstName +
                    "\n Last Name: " + lastName +
                    "\n Email Address: " + email +
                    "\n Phone Number: " + phone +
                    "\n Address: " + address +
                    "\n City: " + city +
                    "\n State: " + state +
                    "\n Postal Code: " + zip
            );

            if (flag) {
                System.out.println("Type 'Finish' to add customer.");
            }

        } while(flag);

        Customer customer = new Customer(firstName, lastName, email, phone,
                Gender.valueOf(gender), address,city,state,zip);

        if (dataAccessor.addCustomer(customer)) {
            System.out.println(firstName + " " + lastName + " successfully added!");
            selectedCustomer = dataAccessor.getCustomerByEmail(customer.getEmailAddress());
        }
    }

    private void doSearch() {
        String[] searchArgs = {"Customers", "Pets", "Exit"};
        boolean flag = true;

        do {
            String input = promptMenu("What do you want to search?", searchArgs).toLowerCase();
            switch (input) {

                case "customers":
                    searchCustomers();
                    break;
                case "pets":
                    searchPets();
                    break;
                case "exit":
                    log.info("Exited Search Function!");
                    flag = false;
                    break;
            }
        } while (flag);
    }

    private void doSelect() {
        String[] searchArgs = {"Customers", "Pets", "Exit"};
        boolean flag = true;

        do {
            String input = promptMenu("What do you want to select?", searchArgs).toLowerCase();
            switch (input) {
                case "customers":
                    doCustomerSelect();
                    break;
                case "pets":
                    doPetSelect();
                    break;
                case "exit":
                    log.info("Exited Select Function!");
                    flag = false;
                    break;
            }
        } while (flag);
    }

    private void doPetSelect() {
        selectPet();
        if (selectedPet == null) {
            System.out.println("Pet not selected!");
            log.error("Pet not selected!");
            return;
        }

        String[] args = {"Update Pet", "Delete Pet", "Print", "Exit" };

        boolean selectionFlag = true;
        do {
            String command = promptMenu("Please type a command:", args).toLowerCase();
            switch (command) {
                case "update pet":
                    updatePet();
                    break;
                case "delete pet":
                    if (deletePet()) return;
                    break;
                case "print":
                    ArrayList<Pet> pet = new ArrayList<>();
                    pet.add(selectedPet);
                    printPetList(pet);
                    break;
                case "exit":
                    selectionFlag = false;
                    break;
            }
        } while(selectionFlag);
    }

    private boolean deletePet() {
        //warn the user of deletion
        System.out.println("This will delete the pet!");
        //get confirmation
        if (inputConfirmation("Are you sure you want to delete the selected pet?")) {
            //attempt to remove the customer
            boolean success = dataAccessor.removePet(this.selectedPet.getPetID());
            //if successful, "deselect" the customer and pet
            if (success) {
                System.out.println("Pet " + " " + this.selectedPet.getName() + " deleted.");
                selectedPet = null;
                return true;
            } else {
                System.out.println("Pet " + " " + this.selectedPet.getName() + " could not be deleted.");
                log.error("Could not delete Pet " + this.selectedPet.getName() + "!");
                return false;
            }
        }
        return false;
    }

    private Pet selectPet() {
        if (selectedPet != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Pet: ");
            sb.append(selectedPet.getName());
            sb.append(" is selected!");

            System.out.println("Pet " + sb.toString());
            if (inputConfirmation("Do you wish to select a new pet?") == false) {
                log.debug("User kept current selection");
                return selectedPet;
            }
        }

        boolean flag = true;
        do {
            System.out.print("Please type in id or [exit] to exit >> ");
            String input = scanner.nextLine().toLowerCase();

            if (StringUtils.isInteger(input)) {
                selectedPet = dataAccessor.getPetByID(Integer.parseInt(input));
            } else if ("exit".equalsIgnoreCase(input) || "quit".equalsIgnoreCase(input)) {
                return selectedPet;
            }

            if (selectedPet != null) flag = false;
            else System.out.println("Invalid entry! Try again!");
        } while (flag);

        return selectedPet;
    }

    private void updatePet() {
        String[] profileOptions = {"Name", "Breed", "Gender","Behaviour", "Behavior", "Finish"};
        boolean flag = true;

        String name = selectedPet.getName(),
                breed= selectedPet.getBreed(),
                gender= selectedPet.getGender().toString(),
                behaviourDescription = selectedPet.getBehaviourDescription();

        do {
            String input = promptMenu("What do you want to update?", profileOptions).toLowerCase();

            switch (input) {
                case "name":
                    do {
                        System.out.print("Please enter the name: >>  ");
                        name = scanner.nextLine();
                        flag = name.isEmpty();
                        if (flag) {
                            System.out.println("Name can't be blank!");
                        }
                    } while(flag);
                    flag = true; //reset flag
                    break;
                case "breed":
                    System.out.print("\nPlease enter the breed: >>  ");
                    breed = scanner.nextLine();
                    break;
                case "gender":
                    gender = promptMenu("Please select a gender:", Gender.Male.toString(),
                            Gender.Female.toString(), Gender.Unspecified.toString(),
                            Gender.Other.toString());
                    break;
                case "behaviour":
                case "behavior":
                    System.out.print("Please enter the name: >>  ");
                    behaviourDescription = scanner.nextLine();
                case "finish":
                    flag = false;
                    break;
            }

            System.out.println("Please confirm the pet's profile: " +
                    "\n Name: " + name +
                    "\n Breed: " + breed +
                    "\n Gender: " + gender +
                    "\n Behaviour Description: " + behaviourDescription
            );

            if (flag)
                System.out.println("Type 'Finish' to update profile\n");

        } while(flag);
        flag = true;

        selectedPet.setName(name);
        selectedPet.setBreed(breed);
        selectedPet.setGender(Gender.valueOf(gender));
        selectedPet.setBehaviourDescription(behaviourDescription);

        dataAccessor.updatePet(selectedPet);
    }

    //region Selection Stuff
    private void doCustomerSelect() {
        selectCustomer();
        if (selectedCustomer == null) {
            System.out.println("Customer not selected!");
            log.error("Customer not selected!");
            return;
        }

        boolean selectionFlag = true;
        do {
            String[] args = {"Update Profile", "Delete Profile", "Print Profile", "Print Pets", "Add Pet", "Remove Pet", "Update Pet", "Exit" };
            String command = promptMenu("Please type a command:", args).toLowerCase();

            switch (command) {
                case "update profile":
                    updateProfile();
                    break;
                case "delete profile":
                    if (deleteProfile()) return;
                    break;
                case "print profile":
                    ArrayList<Customer> customers = new ArrayList<>();
                    customers.add(selectedCustomer);
                    printCustomerList(customers);
                    break;
                case "print pets":
                    printPetsFromCustomer();
                    break;
                case "add pet":
                    addPetToCustomer();
                    break;
                case "remove pet":
                    removePetFromCustomer();
                    break;
                case "update pet":

                    ArrayList<Pet> customerPets = dataAccessor.getPetsFromCustomer(this.selectedCustomer);
                    ArrayList<Integer> associatedIDs = new ArrayList<>();
                    String fullname = this.selectedCustomer.getFirstName() + " " + this.selectedCustomer.getLastName();

                    if (customerPets.size() == 0 || customerPets.size() == 1 && customerPets.get(0) == null) {
                        System.out.println(fullname + " does not have any pets!");
                        return;
                    }

                    boolean flag = true;
                    do {
                        System.out.println("\nAvailable Pets for " + fullname + ":");
                        associatedIDs.clear();
                        for (Pet x : customerPets)
                        {
                            System.out.println("PetID: " + x.getPetID() + " | Name: " + x.getName());
                            associatedIDs.add(x.getPetID());
                        }
                        System.out.print("\nWhat pet do you want to update? >>  ");
                        String input = scanner.nextLine();
                        int id = -1;
                        if (StringUtils.isInteger(input)) {
                            id = Integer.parseInt(input);

                            boolean isAcceptable = false;
                            for (int s : associatedIDs) {
                                if (s == id) isAcceptable = true;
                            }

                            if (isAcceptable) {
                                selectedPet = dataAccessor.getPetByID(id);
                                flag = false;
                            } else {
                                System.out.println("Id is out of range for " + fullname + "'s pets");
                            }
                        } else {
                            flag = true;
                        }
                    } while (flag);
                    updatePet();
                    break;
                case "exit":
                    selectionFlag = false;
                    break;
            }
        } while (selectionFlag);

    }

    private Customer selectCustomer() {
        if (selectedCustomer != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Customer: ");
            sb.append(selectedCustomer.getFirstName() + " " + selectedCustomer.getLastName());
            sb.append(" is selected!");

            System.out.println("Customer " + sb.toString());
            if (inputConfirmation("Do you wish to select a new customer?") == false) {
                log.debug("User kept current selection");
                return selectedCustomer;
            }
        }

        boolean flag = true;
        do {
            System.out.print("Please type in id or email >> ");
            String input = scanner.nextLine().toLowerCase();

            if (StringUtils.isEmail(input)) {
                selectedCustomer = dataAccessor.getCustomerByEmail(input);
            } else if (StringUtils.isInteger(input)) {
                selectedCustomer = dataAccessor.getCustomerByID(Integer.parseInt(input));
            } else if ("exit".equalsIgnoreCase(input) || "quit".equalsIgnoreCase(input)) {
                return selectedCustomer;
            }

            if (selectedCustomer != null) flag = false;
            else System.out.println("Invalid entry! Try again!");
        } while (flag);

        return selectedCustomer;
    }

    private void removePetFromCustomer() {
        ArrayList<Pet> customerPets = dataAccessor.getPetsFromCustomer(this.selectedCustomer);
        ArrayList<Integer> associatedIDs = new ArrayList<>();
        String fullname = this.selectedCustomer.getFirstName() + " " + this.selectedCustomer.getLastName();
        if (customerPets.size() == 0 || customerPets.size() == 1 && customerPets.get(0) == null) {
            System.out.println(fullname + " does not have any pets!");
            return;
        }

        boolean flag = true;
        do {
            System.out.println("\nAvailable Pets for " + fullname + ":");
            associatedIDs.clear();
            for (Pet x : customerPets)
            {
                System.out.println("PetID: " + x.getPetID() + " | Name: " + x.getName());
                associatedIDs.add(x.getPetID());
            }
            System.out.print("\nWhat pet do you want to remove? >>  ");
            String input = scanner.nextLine();
            int id = -1;
            if (StringUtils.isInteger(input)) {
                id = Integer.parseInt(input);

                boolean isAcceptable = false;
                for (int s : associatedIDs) {
                    if (s == id) isAcceptable = true;
                }

                if (isAcceptable) {
                    selectedPet = dataAccessor.getPetByID(id);
                    boolean success = dataAccessor.removePet(id);
                    if (success) {

                        System.out.println("Removed " + selectedPet.getName() + " from " + fullname);
                        selectedPet = null;
                        flag = false;
                    } else {
                        System.out.println("Failed to remove " + selectedPet.getName() + " from " + fullname);
                        if(inputConfirmation("Want to retry deletion?") == false)
                            flag = false;

                    }
                } else {
                    System.out.println("Id is out of range for " + fullname + "'s pets");
                }
            } else {
                flag = true;
            }
        } while (flag);
    }

    private void updateProfile() {
        String[] profileOptions = {"Name", "Email", "Phone Number","Gender", "Address", "Finish"};
        boolean flag = true;

        String firstName = selectedCustomer.getFirstName(),
                lastName= selectedCustomer.getLastName(),
                email= selectedCustomer.getEmailAddress(),
                phone= selectedCustomer.getPhoneNumber(),
                address= selectedCustomer.getAddress(),
                city= selectedCustomer.getCity(),
                state= selectedCustomer.getState(),
                zip= selectedCustomer.getPostalCode(),
                gender= selectedCustomer.getGender().toString();

        do {
            String input = promptMenu("What do you want to update?", profileOptions).toLowerCase();

            switch (input) {
                case "name":
                    do {
                        System.out.print("Please enter first and last name: >>  ");
                        input = scanner.nextLine();
                        //separate first and last name to different strings
                        String[] name = input.split(" ");
                        boolean caughtException = false;
                        try {
                            firstName = name[0];
                            lastName = name[1];
                        } catch (ArrayIndexOutOfBoundsException e) {
                            caughtException = true;
                            System.out.println("Did not get first and last name! Try again.\n");
                        }

                        //only confirm input if we got both first and last name
                        if (caughtException == false) {
                            flag = false;
                        }
                    } while(flag);
                    break;
                case "email":
                    flag = true;
                    do {

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
                                    email = input;
                                    flag = false;
                                    log.debug("Email " + email + " accepted!");
                                }
                            } else {
                                System.out.println("Input is not valid for email addresses! Try again.");
                                flag = true;
                            }
                        } else {
                            System.out.println("Emails do not match! Try again.");
                            flag = true;
                        }

                    } while(flag);
                    break;
                case "phone number":
                    flag = true;
                    do {
                        System.out.print("\nPlease enter phone number: >>  ");
                        phone = scanner.nextLine();

                        if (phone.toCharArray().length < 15) {
                            flag = false;
                        } else {
                            flag = true;
                            System.out.println("Phone number too long! Try again.");
                        }
                    } while (flag);
                    break;
                case "address":
                       do {
                           System.out.print("Please enter address: >>  ");
                           address = scanner.nextLine();
                       } while (inputConfirmation("Is this the right address? " + address) == false);
                       do {
                           System.out.print("Please enter city: >>  ");
                           city = scanner.nextLine();
                       } while (inputConfirmation("Is this the right city? " + city) == false);
                       do {
                           System.out.print("Please enter state: >>  ");
                           state = scanner.nextLine();
                       } while (inputConfirmation("Is this the right state? " + state) == false);
                       do {
                           System.out.print("Please enter postal code: >>  ");
                           zip = scanner.nextLine();
                       } while (inputConfirmation("Is this the right postal code? " + zip) == false);
                    break;
                case "gender":
                    do {
                        gender = promptMenu("Please select a gender:", Gender.Male.toString(),
                                Gender.Female.toString(), Gender.Unspecified.toString(),
                                Gender.Other.toString());

                        flag = !inputConfirmation("Are you sure thats correct? Gender selected: " + gender);
                    } while (flag);
                    break;
                case "finish":
                    flag = false;
                    break;
            }

            System.out.println("Please confirm your profile: " +
                    "\n First Name: " + firstName +
                    "\n Last Name: " + lastName +
                    "\n Email Address: " + email +
                    "\n Phone Number: " + phone +
                    "\n Address: " + address +
                    "\n City: " + city +
                    "\n State: " + state +
                    "\n Postal Code: " + zip
                    );

        } while(flag);
        flag = true;

        selectedCustomer.setFirstName(firstName);
        selectedCustomer.setLastName(lastName);
        selectedCustomer.setEmailAddress(email);
        selectedCustomer.setPhoneNumber(phone);
        selectedCustomer.setGender(Gender.valueOf(gender));
        selectedCustomer.setAddress(address);
        selectedCustomer.setCity(city);
        selectedCustomer.setState(state);
        selectedCustomer.setPostalCode(zip);

        dataAccessor.updateCustomer(selectedCustomer);
    }

    private boolean deleteProfile() {
        //warn the user of deletion
        System.out.println("This will delete all associated entries!");
        //get confirmation
        if (inputConfirmation("Are you sure you want to delete the selected customer?")) {
            //attempt to remove the customer
            boolean success = dataAccessor.removeCustomer(this.selectedCustomer.getCustomerID());
            //if successful, "deselect" the customer and pet
            if (success) {
                System.out.println("Customer " + " " + this.selectedCustomer.getEmailAddress() + " deleted.");
                this.selectedCustomer = null;
                selectedPet = null;
                return true;
            } else {
                System.out.println("Customer " + " " + this.selectedCustomer.getEmailAddress() + " could not be deleted.");
                log.error("Could not delete customer " + this.selectedCustomer.getEmailAddress() + "!");
                return false;
            }
        }
        return false;
    }

    private void printPetsFromCustomer() {
        ArrayList<Pet> pets = dataAccessor.getPetsFromCustomer(selectedCustomer);
        printPetList(pets);
    }

    private void addPetToCustomer() {
        boolean flag;
        String petName;
        String breed;
        String gender = Gender.Unspecified.toString();
        String behaviourDescription;

        do {
            //get the pets name
            flag = true;
            do {
                System.out.print("Pet's name: ");
                petName = scanner.nextLine();
                if (inputConfirmation("Are you sure this is correct? Entered Name: " + petName)) {
                    flag = false;
                } else {
                    System.out.print("Try entering the name again! ");
                }

            } while (flag);

            //get the pet's breed
            flag = true;
            do {
                System.out.print("Pet's breed: ");
                breed = scanner.nextLine();
                if (inputConfirmation("Are you sure this is correct? Entered breed: " + breed)) {
                    flag = false;
                } else {
                    System.out.print("Try entering the breed again! ");
                }
            } while (flag);

            //get the pets gender
            flag = true;
            do {
                gender = promptMenu("Select the pet's gender", "Male", "Female", "Other", "Unspecified");
                if (inputConfirmation("Are you sure this is correct? Selected gender: " + gender)) {
                    flag = false;
                } else {
                    System.out.print("Try selecting the gender again! ");
                }
            } while (flag);

            //get the pet's description
            flag = true;
            do {
                System.out.print("Pet's behaviour description: ");
                behaviourDescription = scanner.nextLine();
                if (inputConfirmation("Are you sure this is correct? Entered description: " + behaviourDescription)) {
                    flag = false;
                } else {
                    System.out.print("Try entering the behaviour description again! ");
                }
            } while (flag);

            System.out.println("Pet Profile: " +
                    "\n Name: " + petName +
                    "\n Breed: " + breed +
                    "\n Gender: " + gender +
                    "\n Behaviour Description: " + behaviourDescription);

        } while (inputConfirmation("Is this correct? ") == false);

        Pet pet = new Pet(petName, breed, Gender.valueOf(gender), behaviourDescription);
        dataAccessor.addPet(selectedCustomer, pet);
    }

    //endregion

    //region Searching Stuff
    //Handles finding and printing pets to the terminal
    private void searchPets() {
        ArrayList<Pet> pets = new ArrayList<>();
        System.out.println("Please type in search criteria (leave blank for all): ");
        System.out.print("Examples (Email, id) >> ");
        String input = scanner.nextLine().toLowerCase();

        pets.clear();
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
            log.debug(message);
            System.out.println(message);
            return;
        }

        if (pets.size() > 0) {
            printPetList(pets);
        }
    }

    //Handles searching for customers
    private void searchCustomers() {
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
        } else if (input.isEmpty() == false) {
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
            log.debug(message);
            System.out.println(message);
            return;
        }

        //print found records
        if (customers.size() > 0) {
            printCustomerList(customers);
        }
    }
    //endregion

    //region Helpful methods
    private void printCustomerList(ArrayList<Customer> list) {
        System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------------");
        list.forEach(x -> System.out.println(x));
        System.out.println("---------------------------------------------------------------------------------------------------------------------------------------");
    }
    private void printPetList(ArrayList<Pet> list) {
        System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------------");
        list.forEach(x -> System.out.println(x));
        System.out.println("---------------------------------------------------------------------------------------------------------------------------------------");
    }

    //prompts the user for a string input
    private static String promptMenu(String message, String ...commandArgs) {

        StringBuilder sb = new StringBuilder();
        sb.append("Options: ");
        for (int i =0; i < commandArgs.length; i++) {
            sb.append(String.format("%s [%d] ", commandArgs[i], i));
            if (i != commandArgs.length - 1) sb.append("| ");
        }
        String options = sb.toString();

        while (true) {
            System.out.print(message + "\n " + options + "\n >>  ");
            String prompt = scanner.nextLine().toLowerCase();
            int promptAsInt = -1;
            try {
                promptAsInt = Integer.valueOf(prompt);
            } catch (Exception e) { }

            for (int i = 0; i <= commandArgs.length; i++) {
                if (prompt.equalsIgnoreCase(commandArgs[i]) || promptAsInt == i)
                {
                    log.debug("Prompt Confirmed: " + commandArgs[i]);
                    return commandArgs[i];
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
                case "y":
                case "Y":
                case "Yes":
                case "yes":
                case "0":
                    log.debug("User selected 'Yes' to " + message);
                    return true;
                case "n":
                case "N":
                case "No":
                case "no":
                case "1":
                    log.debug("User selected 'No' to " + message);
                    return false;
            }
        }
    }
    //endregion


}
