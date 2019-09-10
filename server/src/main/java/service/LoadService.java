package service;

import java.sql.SQLException;
import java.util.ArrayList;

import access.EventDao;
import access.PersonDao;
import access.UserDao;
import model.Event;
import model.Person;
import model.User;
import request.LoadRequest;
import response.DefaultResponse;

/** Clears all data from the database then loads new data in */
public class LoadService {

    /**
     * Clears all data from the database (just like the /clear API), and then loads the posted user, person, and event data into the database.
     * @param req
     * @return  Returns the DefaultResponse Message
     */
    public DefaultResponse load (LoadRequest req) {
        try {
            // Clears all data from the database
            ClearService service = new ClearService();
            service.clear();
            //System.out.println("here");

            for (int i = 0; i < req.getUsers().size(); i++) {
                System.out.println(req.getPersons().get(i)+"\n");
            }

            int numUsersAdded = addUsers(req.getUsers());
            //System.out.println(numUsersAdded);
            int numPeopleAdded = addPeople(req.getPersons());
            //System.out.println(numPeopleAdded);
            int numEventsAdded = addEvents(req.getEvents());
            //System.out.println(numEventsAdded);

            //System.out.println(numEventsAdded + numPeopleAdded + numUsersAdded);

            DefaultResponse response = new DefaultResponse("Successfully added " + numUsersAdded
                    + " users, " + numPeopleAdded + " persons, and " + numEventsAdded + " events to the database.");

            return response;
        }
        catch (SQLException ex) {
            ex.printStackTrace();
            DefaultResponse response = new DefaultResponse("A database error occurred");
            return response;
        }
    }

    private int addUsers(ArrayList<User> users) throws SQLException { //FIXME: check for errors
        UserDao access = new UserDao();
        int numUsers = 0;
        for (int i = 0; i < users.size(); i++) {
            access.addNewUser(users.get(i));
            numUsers++;
        }
        //System.out.println(numUsers);
        return numUsers;
    }
    private int addPeople(ArrayList<Person> people) throws SQLException { //FIXME: check for errors
        PersonDao access = new PersonDao();
        int numPeople = 0;
        for (int i = 0; i < people.size(); i++) {
            access.addNewPerson(people.get(i));
            numPeople++;
        }
        System.out.println(numPeople);
        return numPeople;
    }
    private int addEvents(ArrayList<Event> events) throws SQLException { //FIXME: check for errors
        EventDao access = new EventDao();
        int numEvents = 0;
        for (int i = 0; i < events.size(); i++) {
            access.addNewEvent(events.get(i));
            numEvents++;
        }
        return numEvents;
    }



}
