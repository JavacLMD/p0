package domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Objects;

public class Customer {

    private static final Logger log = LoggerFactory.getLogger(Customer.class);

    public Customer(int id, String firstName, String lastName, String emailAddress,
                    String phoneNumber, Gender gender, String address, String city,
                    String state, String postalCode) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.address = address;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
    }

    private int id;
    private final String firstName, lastName;
    private String emailAddress;
    private final String phoneNumber;
    private final Gender gender;
    private String address, city, state, postalCode;



    public Customer(int id, String firstName, String lastName, String emailAddress) {
        this(id, firstName, lastName, emailAddress, "", Gender.Unspecified, "","", "", "");
    }

    private HashSet<Pet> pets = new HashSet<>();

    @Override
    public String toString() {
        return "Customer{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", pets=" + pets +
                '}';
    }

    public boolean addPet(Pet pet) {
        if (!pets.contains(pet)) {
            try {
                boolean flag = pets.add(pet);
                if (flag)
                    log.info("Added pet: " + pet.getName() + " to " + this.firstName);
                return flag;
            } catch (Exception e) {
                log.error(e.getMessage());
                return false;
            }
        }
        log.warn("Could not add pet: " + pet.getName() + " to " + this.firstName);
        return false;
    }

    public boolean RemovePet(Pet pet) {
        if (pets.contains(pet.getId())) {
            boolean flag = pets.removeIf(x -> x.getId() == pet.getId());
            log.info("Removed pet: " + pet.getName() + " from " + this.firstName);
            return flag;
        }
        return false;
    }


    public  int getID() { return id; }
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Gender getGender() {
        return gender;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public HashSet<Pet> getPets() {
        return pets;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return emailAddress.equals(customer.emailAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(emailAddress);
    }

}
