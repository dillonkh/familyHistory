package service;

import java.sql.SQLException;
import java.util.ArrayList;

import access.AuthTokenDao;
import access.PersonDao;
import access.UserDao;
import model.AuthToken;
import model.Person;
import model.User;
import response.DefaultResponse;
import response.PersonResponse;
import response.RegisterLoginResponse;

/** Gets the single Person object with the specified ID. */
public class PersonService {


    /**
     * ​Gets the single Person object with the specified ID.
     * @param personID The ID of the person we are trying to get
     * @return      Returns the PersonResponse Message
     */
    public PersonResponse getPerson (String personID,String authToken) {
        System.out.println("in get person");
        try {

            PersonDao access = new PersonDao();

            Person person = access.getPersonWithPersonID(personID);
            //Person person = new PersonDao().getPersonWithPersonID(event.getPersonID());
            if (person == null) {
                return new PersonResponse("That person does not exist");
            }
            User user = new UserDao().getUserWithUsername(person.getDescendant());
            if (user == null) {
                return new PersonResponse("Not authorized to access that info");
            }
            //String token = new AuthTokenDao().getTokenWithPersonID(user.getPersonID()).getToken();
            ArrayList<AuthToken> tokens = new AuthTokenDao().getTokensWithPersonID(user.getPersonID());
            boolean found = false;
            for (int i = 0; i < tokens.size(); i++) {
                System.out.println("printing tokens: " + tokens.get(i).getToken());
                if (tokens.get(i).getToken().equals(authToken)) {
                    found = true;
                }
            }
            if (!found) {
                return new PersonResponse("Not authorized to access that info");
            }

            PersonResponse response = new PersonResponse(
                    person.getDescendant(),
                    person.getPersonID(),
                    person.getFirstName(),
                    person.getLastName(),
                    person.getGender(),
                    person.getFather(),
                    person.getMother(),
                    person.getSpouse()
            );
            //System.out.println(person.toString());
            return response;
        }
        catch (SQLException ex) {
            ex.printStackTrace();
            return new PersonResponse("database error occurred");
        }
    }


}
