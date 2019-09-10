package request;

/**  The Request used to register a user */
public class RegisterRequest {

    /**  Username in the Request */
    private String userName = new String();

    /**  Password in the Request */
    private String password = new String();

    /**  Email in the Request */
    private String email = new String();

    /**  First name in the Request */
    private String firstName = new String();

    /**  Last Name in the Request */
    private String lastName = new String();

    /**  Gender in the Request */
    private String gender = new String();

    public RegisterRequest(String user,String pass,String mail,String first,String last,String g) {
        userName = user;
        password = pass;
        email = mail;
        firstName = first;
        lastName = last;
        gender = g;
    }

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
}
