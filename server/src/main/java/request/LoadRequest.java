package request;

import java.util.ArrayList;

import model.Event;
import model.Person;
import model.User;

/**  The Request used to load new family history data */
public class LoadRequest {
    /**  List of users in the Request */
    private ArrayList<User> users = null;

    /**  List of persons in the Request */
    private ArrayList<Person> persons = null;

    /**  List of events in the Request */
    private ArrayList<Event> events = null;

    public LoadRequest(ArrayList<User> users, ArrayList<Person> persons, ArrayList<Event> events) {
        this.users = users;
        //System.out.println(users.get(0).getUserName());
        this.persons = persons;
        this.events = events;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public ArrayList<Person> getPersons() {
        return persons;
    }

    public void setPersons(ArrayList<Person> persons) {
        this.persons = persons;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }
}
