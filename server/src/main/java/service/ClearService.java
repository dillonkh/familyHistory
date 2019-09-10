package service;

import java.sql.SQLException;

import access.AuthTokenDao;
import access.EventDao;
import access.PersonDao;
import access.UserDao;
import response.DefaultResponse;

/** Deletes ALL data from the database, including user accounts, auth tokens, and generated person and event data. */
public class ClearService {

    /**
     * Deletes ALL data from the database, including user accounts, auth tokens, and generated person and event data.
     * @return      Returns the DefaultResponse message
     */
    public DefaultResponse clear() { //FIXME: may need to make dao clears boolean
        try {
            AuthTokenDao tokenAccess = new AuthTokenDao();
            EventDao eventAccess = new EventDao();
            PersonDao personAccess = new PersonDao();
            UserDao userAccess = new UserDao();

            tokenAccess.clear();
            eventAccess.clear();
            personAccess.clear();
            userAccess.clear();

            DefaultResponse response = new DefaultResponse("Clear Succeeded");

            return response;
        }
        catch (SQLException ex) {
            ex.printStackTrace();
            DefaultResponse response = new DefaultResponse("An error occurred during the clear."); //Fixme needs real message...
            return response;
        }
        //return null;
    }

}
