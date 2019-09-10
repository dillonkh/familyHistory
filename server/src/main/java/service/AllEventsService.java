package service;

import java.sql.SQLException;
import java.util.ArrayList;

import access.AuthTokenDao;
import access.EventDao;
import model.AuthToken;
import model.Event;
import model.Person;
import model.User;
import response.EventResponse;
import response.EventsResponse;

/** Gets ALL events for ALL family members of the current user. */
public class AllEventsService {

    /**
     * Gets ALL events for ALL family members of the current user. The current user is determined from the provided auth token.
     * @return  Returns the EventsResponse Message
     */
    public EventsResponse getAllEvents (String authToken) {
        try {
            System.out.println("in all events service");

            AuthTokenDao tokenAccess = new AuthTokenDao();
            AuthToken token = tokenAccess.getToken(authToken);
            if (token == null) {
                EventsResponse response = new EventsResponse("Not a valid auth token");
                return response;
            }
            User currentUser = tokenAccess.getUserWithToken(authToken);

            EventDao access = new EventDao();
            ArrayList<Event> events = access.getEventsWithDescendant(currentUser.getUserName());

            //EventsResponse response = new EventsResponse("A database error occurred");
            EventsResponse response = new EventsResponse(events);
            //System.out.println("the resp is : " + response);

            return response;
        }
        catch (SQLException ex) {
            ex.printStackTrace();
            return new EventsResponse("A database error occurred");
        }
    }

}
