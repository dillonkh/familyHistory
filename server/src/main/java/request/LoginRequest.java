package request;


/**  The Request used to login a user */
public class LoginRequest {
    /**  userName in the Request */
    private String userName = new String();

    /**  password in the Request */
    private String password = new String();

    public LoginRequest(String userName, String password) {
        this.userName = userName;
        this.password = password;
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
}
