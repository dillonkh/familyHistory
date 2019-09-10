package model;

// Username: Unique user name (non-empty string)
// Password: User’s password (non-empty string)
// Email: User’s email address (non-empty string)
// First Name: User’s first name (non-empty string)
// Last Name: User’s last name (non-empty string)
// Gender: User’s gender (string: “f” or “m”)
// Person ID: Unique Person ID assigned to this user’s generated Person object

/** A User is a Person that also has a username, password, and email. They are also given an AuthToken in the DataBase */
public class User {
//    /** Unique id for each User */
//    private String userID = new String();

    /** Unique user name (non-empty string) */
    private String userName = new String();

    /** User’s password (non-empty string) */
    private String password = new String();

    /** User’s email address (non-empty string) */
    private String email = new String();

    /** User’s first name (non-empty string) */
    private String firstName = new String();

    /** User’s last name (non-empty string) */
    private String lastName = new String();

    /** User’s gender (string: “f” or “m”) */
    private String gender = new String();

    /** Unique Person ID assigned to this user’s generated Person object  */
    private String personID = new String();

    public User(String userName, String password, String email,
                String firstName, String lastName, String gender, String personID) {

//        this.userID = userID;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.personID = personID;
    }

//    public String getUserID() {
//        return userID;
//    }
//
//    public void setUserID(String userID) {
//        this.userID = userID;
//    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }
    @Override
    public String toString() {
//        this.username = username;
//        this.password = password;
//        this.email = email;
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.gender = gender;
//        this.personID = personID;
        StringBuilder toPrint = new StringBuilder();

        toPrint.append("UserName: "+getUserName());
        toPrint.append("\nPassword: "+getPassword());
        toPrint.append("\nEmail: "+getEmail());
        toPrint.append("\nFirstName: "+getFirstName());
        toPrint.append("\nLastName: "+getLastName());
        toPrint.append("\nGender: "+getGender());
        toPrint.append("\nPersonID: "+getPersonID());

        return toPrint.toString();
    }
}
