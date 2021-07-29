package domain;

import java.sql.Date;
import java.util.Objects;

public class Pet {

    private int id = -1;
    private String name;
    private String breed;
    private Date birthDate;
    private Gender gender = Gender.Unspecified;
    private String behaviourDescription;

    public Pet(int id, String name, String breed, Date birthDate, Gender gender, String behaviourDescription) {
        this.id = id;
        this.name = name;
        this.breed = breed;
        this.birthDate = birthDate;
        this.gender = gender;
        this.behaviourDescription = behaviourDescription;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBreed() {
        return breed;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public Gender getGender() {
        return gender;
    }

    public String getBehaviourDescription() {
        return behaviourDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pet pet = (Pet) o;
        return id == pet.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Pet{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", breed='" + breed + '\'' +
                ", birthDate=" + birthDate +
                ", gender=" + gender +
                ", behaviourDescription='" + behaviourDescription + '\'' +
                '}';
    }
}
