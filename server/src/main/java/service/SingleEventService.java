package service;

import java.sql.SQLException;

import access.AuthTokenDao;
import access.EventDao;
import access.PersonDao;
import access.UserDao;
import model.Event;
import model.Person;
import model.User;
import response.EventResponse;

/** Gets the single Event object with the specified ID */
public class SingleEventService {

    /**
     * Gets the single Event object with the specified ID.
     * @param eventID   The event ID we will be searching for
     * @return          Returns the EventResponse Message.
     */
    public EventResponse getEvent (String eventID,String authToken) { //FIXME: i may have to do something with the authToken.
        try {
            System.out.println("in get event");
            EventDao access = new EventDao();

            Event event = access.getEventWithEventID(eventID);
            if (event == null) {
                return new EventResponse("That event doesn't exist");
            }
            Person person = new PersonDao().getPersonWithPersonID(event.getPersonID());
            if (person == null) {
                return new EventResponse("There is not a person associated with that event");
            }
            User user = new UserDao().getUserWithUsername(person.getDescendant());
            if (user == null) {
                return new EventResponse("Not authorized to access that data");
            }
            String token = new AuthTokenDao().getTokenWithPersonID(user.getPersonID()).getToken();

            //System.out.println(token);
            //System.out.println(authToken);

            if (!token.equals(authToken)) {
                return new EventResponse("Not authorized to access that data");
            }

            EventResponse response = new EventResponse(event.getDescendant(),
                    event.getEventID(), event.getPersonID(), event.getLatitude(),
                    event.getLongitude(), event.getCountry(), event.getCity(),
                    event.getEventType(), event.getYear());

            return response;
        }
        catch(SQLException ex) {
            ex.printStackTrace();
            return new EventResponse("A database error occurred");
        }
    }


}
