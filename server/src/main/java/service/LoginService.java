package service;

import java.sql.SQLException;
import java.util.UUID;

import access.AuthTokenDao;
import access.UserDao;
import model.AuthToken;
import model.User;
import request.LoginRequest;
import response.DefaultResponse;
import response.RegisterLoginResponse;

/** Logs the user in */
public class LoginService {

    /**
     * Logs in the user and returns an auth token.
     * @param req
     * @return  returns the RegisterLoginResponse Message, including the authToken
     */
    public RegisterLoginResponse login (LoginRequest req) {
       try {
           //check username and password:
//        RegisterLoginResponse response;
           RegisterLoginResponse response;
           UserDao userAccess = new UserDao();
           System.out.println(req.getUserName());
           User user = userAccess.getUserWithUsername(req.getUserName());

           //System.out.println(user.toString());

           // only log in people who already exist
           if (user == null) {
               System.out.println("user null");
               response = new RegisterLoginResponse("Username not valid");
               return response;
           }
           User user2 = userAccess.getUserWithPassword(req.getPassword());
           if (user2 == null) {
               System.out.println("password null");
               response = new RegisterLoginResponse("password not valid");
               return response;
           }

           // make auth Object
           AuthToken token = new AuthToken(UUID.randomUUID().toString(),user.getPersonID());
           AuthTokenDao tokenAccess = new AuthTokenDao();
           tokenAccess.addNewToken(token);

           // make response:
           response = new RegisterLoginResponse(token.getToken(),user.getUserName(),user.getPersonID());
            System.out.println("login response: " + response.toString());
           return response;
       }
       catch (SQLException ex) {
           ex.printStackTrace();
           DefaultResponse response = new DefaultResponse("A database error occurred");
           return  null;
       }
    }




}
