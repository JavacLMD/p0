package com.github.JavacLMD.projectZero.model;

import java.sql.Date;

public class Pet {

    private int petID;
    private int customerID;



    private String name;
    private Gender gender;
    private String breed;
    private String behaviourDescription;
    private Date addedDate;



    @Override
    public String toString() {
        return "Pet{" +
                "petID=" + petID +
                ", customerID=" + customerID +
                ", name='" + name + '\'' +
                ", gender=" + gender +
                ", breed='" + breed + '\'' +
                ", behaviourDescription='" + behaviourDescription + '\'' +
                ", addedDate=" + addedDate +
                '}';
    }




    public Pet(int petID, int customerID, String name, Gender gender, String breed, String behaviourDescription, Date addedDate) {
        this.petID = petID;
        this.customerID = customerID;
        this.name = name;
        this.gender = gender;
        this.breed = breed;
        this.behaviourDescription = behaviourDescription;
        this.addedDate = addedDate;
    }

    public Pet(String name, String breed, Gender gender, String behaviourDescription) {
        this.name = name;
        this.gender = gender;
        this.breed = breed;
        this.behaviourDescription = behaviourDescription;

    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public void setBehaviourDescription(String behaviourDescription) {
        this.behaviourDescription = behaviourDescription;
    }

    public int getPetID() {
        return petID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public String getName() {
        return name;
    }

    public Gender getGender() {
        return gender;
    }

    public String getBreed() {
        return breed;
    }

    public String getBehaviourDescription() {
        return behaviourDescription;
    }

    public Date getAddedDate() {
        return addedDate;
    }
}
