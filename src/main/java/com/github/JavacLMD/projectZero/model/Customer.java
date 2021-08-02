package com.github.JavacLMD.projectZero.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Date;
import java.util.Objects;


public class Customer {
    private static final Logger log = LogManager.getLogger(Customer.class);

    //Not Null Values
    private Integer id;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String phoneNumber;
    private Date joinDate;

    //Optional Values
    private Gender gender = Gender.Unspecified;
    private String address;
    private String city;
    private String state;
    private String postalCode;

    public Customer(Integer id, String firstName, String lastName, String emailAddress, String phoneNumber, Date joinDate) {
        this(id, firstName, lastName, emailAddress, phoneNumber, joinDate,
                Gender.Unspecified, "","","","");
    }

    public Customer(String firstName, String lastName, String emailAddress, String phoneNumber, Gender gender, String address, String city, String state, String postalCode) {
        this (-1, firstName, lastName, emailAddress, phoneNumber, new Date(System.currentTimeMillis()), gender, address, city, state, postalCode);
    }

    public Customer(Integer id, String firstName, String lastName, String emailAddress, String phoneNumber, Date joinDate, Gender gender, String address, String city, String state, String postalCode) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.joinDate = joinDate;
        this.gender = gender;
        this.address = address;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
    }

    public int getCustomerID() { return this.id; }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }


    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", joinDate=" + joinDate +
                ", gender=" + gender +
                '}';
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
