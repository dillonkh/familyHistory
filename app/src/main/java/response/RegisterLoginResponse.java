package response;


/** Response for the Login and Register Services */
public class RegisterLoginResponse {

    /** Non-empty auth token string */
    private String authToken = null;

    /** User name passed in with request */
    private String username = null;

    /** Non-empty string containing the Person ID of the user’s generated Person object */
    private String personID = null;

    /**  String used to contain the DefaultResponse Message that will be displayed in case of error */
    private String message = null;

    public RegisterLoginResponse() {}

    public RegisterLoginResponse(String message) {
        this.message = message;
    }

    public RegisterLoginResponse(String authToken, String username, String personID) {
        this.authToken = authToken;
        this.username = username;
        this.personID = personID;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(authToken + ", " + username + ", " + personID);

        return sb.toString();
    }


}
