package response;

import java.util.ArrayList;
import java.util.Vector;

import model.Person;

/** Response for the all People Service */
public class PeopleResponse {

    /** List of Person objects */
    private ArrayList<Person> people = null;

    /**  String used to contain the DefaultResponse Message that will be displayed in case of error */
    private String message = null;

    public PeopleResponse(ArrayList<Person> people) {
        this.people = people;
    }

    public PeopleResponse(String message) {
        this.message = message;
    }

    public ArrayList<Person> getPeople() {
        return people;
    }

    public void setPeople(ArrayList<Person> people) {
        this.people = people;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}