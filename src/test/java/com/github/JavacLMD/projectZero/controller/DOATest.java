package com.github.JavacLMD.projectZero.controller;

import com.github.JavacLMD.projectZero.model.Customer;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public abstract class DOATest {

    protected static DOA dataAccessor;

    @Before
    public abstract void testAccessor();
    @After
    public abstract void testClose();


    @Test
    public void testGetCustomers() {
        List<Customer> customers = new ArrayList<>();
        customers = dataAccessor.getCustomers();
        Assert.assertTrue(customers.size() > 0);
    }

    /*
    public void testGetCustomersByName() {
        String name = "Lane Dorscher";
    }

    public void testGetCustomerByEmail() {
    }

    public void testGetCustomerByID() {
    }

    public void testAddCustomer() {
    }

    public void testRemoveCustomer() {
    }

    public void testUpdateCustomer() {
    }

    public void testGetPets() {
    }

    public void testGetPetsFromCustomer() {
    }

    public void testGetPetByID() {
    }

    public void testAddPet() {
    }

    public void testRemovePet() {
    }

    public void testRemovePetsFromCustomer() {
    }

    public void testUpdatePet() {
    }

    */


}