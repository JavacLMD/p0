package com.github.JavacLMD.projectZero;

public class Pet {

    private String name;
    private Gender gender;
    private String breed;
    private String behaviourDescription;

    public Pet(String name, Gender gender, String breed, String behaviourDescription) {
        this.name = name;
        this.gender = gender;
        this.breed = breed;
        this.behaviourDescription = behaviourDescription;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getBehaviourDescription() {
        return behaviourDescription;
    }

    public void setBehaviourDescription(String behaviourDescription) {
        this.behaviourDescription = behaviourDescription;
    }

}
