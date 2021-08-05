package com.github.JavacLMD.projectZero.controller;

import com.github.JavacLMD.projectZero.model.Customer;
import com.github.JavacLMD.projectZero.model.Pet;

import java.util.ArrayList;

public interface DOA {

    ArrayList<Customer> getCustomers(); //returns list of all customers
    ArrayList<Customer> getCustomersByName(String ...name); //return list of customers with first/last name OR first AND last name
    Customer getCustomerByEmail(String email); //return customer with the associated email
    Customer getCustomerByID(int customerID); //return customer with the associated id

    boolean addCustomer(Customer customer);
    //boolean removeCustomer(Customer customer);
    boolean removeCustomer(int id);
    boolean updateCustomer(Customer customer);


    ArrayList<Pet> getPets(); //returns list of all pets registered
    ArrayList<Pet> getPetsFromCustomer(Customer customer); //returns list of pets that customer owns
    Pet getPetByID(int petID); //return pet from the associated id

    boolean addPet(Customer customer, Pet pet);
    boolean removePet(int id);
    boolean removePetsFromCustomer(Customer customer);
    boolean updatePet(Pet pet);

    void close();



}
