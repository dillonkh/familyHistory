package service;

import java.sql.SQLException;

import access.AuthTokenDao;

public class ValidateTokenService {

    String authToken = null;

    public ValidateTokenService(String authToken) {
        this.authToken = authToken;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public boolean isValidToken () {
        try {
            AuthTokenDao tokenAccess = new AuthTokenDao();
            if (tokenAccess.getUserWithToken(getAuthToken()) != null) {
                return true;
            }
            else {
                return false;
            }
        }
        catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

}
