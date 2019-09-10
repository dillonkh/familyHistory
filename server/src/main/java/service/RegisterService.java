package service;

import java.sql.SQLException;
import java.util.UUID;

import access.UserDao;
import model.User;
import request.LoginRequest;
import request.RegisterRequest;
import response.RegisterLoginResponse;

/** Creates new user account and logs new user in */
public class RegisterService {

    /**
     *  Creates a new user account, generates 4 generations of ancestor data for the new user, logs the user in, and returns an auth token.
     * @param req
     * @return  Returns the RegisterLoginResponse Mesage, including the AuthToken
     */
    public RegisterLoginResponse register (RegisterRequest req) {
        System.out.println("in register service");
       try {
           // check username in table: // Fixme: should check for errors withing the request body
           RegisterLoginResponse response;
           UserDao userAccess = new UserDao();
           if (userAccess.getUserWithUsername(req.getUserName()) != null) {
               System.out.println("Not unique username");
               response = new RegisterLoginResponse("Not valid username.");
               return response;

           }
           // Creates new User account:
           User user = new User(
                   req.getUserName(),
                   req.getPassword(),
                   req.getEmail(),
                   req.getFirstName(),
                   req.getLastName(),
                   req.getGender(),
                   UUID.randomUUID().toString()
           );
           System.out.println("adding user:\n " + user.toString());
           userAccess.addNewUser(user);

           // generates 4 generations of ancestor data for the new user
           FillService fill = new FillService();
           fill.fill(user.getUserName());

           // logs the user in
           LoginRequest logRequest = new LoginRequest(req.getUserName(),req.getPassword());
           LoginService logService = new LoginService();
           response = logService.login(logRequest);
           //userAccess.addNewUser(user);

           // Returns an authToken:
           return response;
       }
       catch (SQLException ex) {
           ex.printStackTrace();
           return new RegisterLoginResponse("A database error occurred");
       }
    }


}
