package response;

import java.util.ArrayList;
import java.util.Vector;

import model.Event;

/** Response for the all Events Service */
public class EventsResponse {

    /** List of Event objects */
    private ArrayList<Event> events = null;

    /**  String used to contain the DefaultResponse Message that will be displayed in case of error */
    private String message = null;

    public EventsResponse(ArrayList<Event> events) {
        this.events = events;

    }

    public EventsResponse(String message) {
        this.message = message;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}