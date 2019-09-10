package model;

// Person ID: Unique identifier for this person (non-empty string)
// Descendant: User (Username) to which this person belongs
// First Name: Person’s first name (non-empty string)
// Last Name: Person’s last name (non-empty string)
// Gender: Person’s gender (string: “f” or “m”)
// Father: ID of person’s father (possibly null)
// Mother: ID of person’s mother (possibly null)
// Spouse: ID of person’s spouse (possibly null)


/** A Person is type of data stored in the Data Base */
public class Person {

    /** Unique identifier for this person (non-empty string) */
    private String personID = new String();

    /** User (Username) to which this person belongs  */
    private String descendant = new String();

    /** Person’s first name (non-empty string) */
    private String firstName = new String();

    /** Person’s last name (non-empty string) */
    private String lastName = new String();

    /** Person’s gender (string: “f” or “m”) */
    private String gender = new String();

    /** ID of person’s father (possibly null) */
    private String father = null;

    /** ID of person’s mother (possibly null) */
    private String mother = null;

    /** ID of person’s spouse (possibly null) */
    private String spouse = null;

    public Person(String descendant, String personID, String firstName, String lastName, String gender, String father, String mother, String spouse) {
        this.personID = personID;
        this.descendant = descendant;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.father = father;
        this.mother = mother;
        this.spouse = spouse;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getDescendant() {
        //System.out.println(descendant);
        return descendant;
    }

    public void setDescendant(String descendant) {
        this.descendant = descendant;
    }

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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFather() {
        return father;
    }

    public void setFather(String father) {
        this.father = father;
    }

    public String getMother() {
        return mother;
    }

    public void setMother(String mother) {
        this.mother = mother;
    }

    public String getSpouse() {
        return spouse;
    }

    public void setSpouse(String spouse) {
        this.spouse = spouse;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(getPersonID());
        sb.append("\n"+getDescendant());
        sb.append("\n"+getFirstName());
        sb.append("\n"+getLastName());
        sb.append("\n"+getGender());
        sb.append("\n"+getFather());
        sb.append("\n"+getMother());
        sb.append("\n"+getSpouse());

        return sb.toString();
    }
}
