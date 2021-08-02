package com.github.JavacLMD.projectZero.controller;

import com.github.JavacLMD.projectZero.model.Appointment;
import com.github.JavacLMD.projectZero.model.Customer;
import com.github.JavacLMD.projectZero.model.Pet;

import java.util.List;

public interface IAccessor {

    boolean insertCustomer(Customer customer);
    boolean removeCustomer(int customerID);
    boolean removeCustomer(String email);
    boolean updateCustomer(Customer customer);

    List<Customer> getAllCustomers();
    List<Customer> getCustomersByName(String ...args);
    Customer getCustomerByID(int customerID);
    Customer getCustomerByEmail(String emailAddress);

    boolean insertPet(Pet pet);
    boolean removePet(int petID);
    boolean removePetsFromCustomer(Customer customer);
    boolean updatePet(Pet pet);

    Pet getPetByID(int petID);
    List<Pet> getAllPets(); //list all pets
    List<Pet> getPetsByName(String name); //list all pets by name
    List<Pet> getPetsByCustomer(Customer customer); //list all pets associated with customer's email

 /*
    boolean insertAppointment(Appointment appointment) ;
    boolean removeAppointment(int appointmentID) ;
    boolean updateAppointment(Appointment appointment);
    List<Appointment> getAllAppointments();
  */

}
