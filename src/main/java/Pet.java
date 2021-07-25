public class Pet {

    int petID;
    int customerID;
    String name;
    String behaviourDescription;

    public Pet(){}
    public Pet(int petID, int customerID, String name, String behaviourDescription) {
        setPetID(petID);
        setCustomerID(customerID);
        setName(name);
        setBehaviourDescription(behaviourDescription);
    }

    public int getPetID() {
        return petID;
    }

    public void setPetID(int petID) {
        this.petID = petID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBehaviourDescription() {
        return behaviourDescription;
    }

    public void setBehaviourDescription(String behaviourDescription) {
        this.behaviourDescription = behaviourDescription;
    }
}
