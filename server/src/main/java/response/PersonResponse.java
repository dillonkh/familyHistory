package response;

/** Response for the single Person Service */
public class PersonResponse {
    /**  Name of user account this person belongs to */
    private String descendent = null;

    /** Person’s unique ID  */
    private String personID = null;

    /**  Person’s first name */
    private String firstName = null;

    /**  Person’s last name */
    private String lastName = null;

    /** Person’s gender (“m” or “f”)  */
    private String gender = null;

    /** ID of person’s father ​[OPTIONAL, can be missing]  */
    private String father = null;

    /** ID of person’s mother ​[OPTIONAL, can be missing]  */
    private String mother = null;

    /**  ID of person’s spouse ​[OPTIONAL, can be missing] */
    private String spouse = null;

    /**  String used to contain the DefaultResponse Message that will be displayed in case of error */
    private String message = null;

    public PersonResponse(String descendent, String personID, String firstName, String lastName,
                          String gender, String father, String mother, String spouse) {
        this.descendent = descendent;
        this.personID = personID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.father = father;
        this.mother = mother;
        this.spouse = spouse;
    }

    public PersonResponse(String message) {
        this.message = message;
    }

    public String getDescendent() {
        return descendent;
    }

    public void setDescendent(String descendent) {
        this.descendent = descendent;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}