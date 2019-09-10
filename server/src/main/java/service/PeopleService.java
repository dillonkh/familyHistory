package service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

import access.AuthTokenDao;
import access.PersonDao;
import model.AuthToken;
import model.Person;
import model.User;
import response.PeopleResponse;

/** Gets ALL family members of the current user. */
public class PeopleService {


    /**
     * Gets ALL family members of the current user. The current user is determined from the provided auth token.
     * @return  Returns the PeopleResponse Message.
     */
    public PeopleResponse getPeople (String authToken) {

        System.out.println("in all people");

        try {
            AuthTokenDao tokenAccess = new AuthTokenDao();
            AuthToken token = tokenAccess.getToken(authToken);
            if (token == null) {
                PeopleResponse response = new PeopleResponse("Not a valid auth token");
                return response;
            }
            User currentUser = tokenAccess.getUserWithToken(authToken);

            ArrayList<Person> people = new ArrayList<Person>();
            PersonDao access = new PersonDao();

            people = access.getPeopleWithDescendant(currentUser.getUserName());
            PeopleResponse response = new PeopleResponse(people);

            return response;
        }
        catch(SQLException ex) {
            ex.printStackTrace();
            return new PeopleResponse("A database error occurred");
        }
    }

  
}
