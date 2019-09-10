package access;
// Event ID: Unique identifier for this event (non-empty string)
// Descendant: User (Username) to which this person belongs
// Person: ID of person to which this event belongs
// Latitude: Latitude of event’s location
// Longitude: Longitude of event’s location
// Country: Country in which event occurred
// City: City in which event occurred
// EventType: Type of event (birth, baptism, christening, marriage, death, etc.)
// Year: Year in which event occurred

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import model.Event;

/**  Accesses the Event table in the Data Base. May return rows from that table or add new rows. */
public class EventDao {

    /**
     * Gets the Event with the specified ID
     * @param eventID       Unique identifier for this event (non-empty string)
     * @return              Returns the event with given ID
     */
    public Event getEventWithEventID (String eventID) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet results = null;

        String dbname = "jdbc:sqlite:newTest.db";
        //System.out.println("reached here");
        connection = DriverManager.getConnection(dbname); // throws SQLException

        //String query = "select * from member";
        String select = "select * from events where EventID = ?";
        stmt = connection.prepareStatement(select);        // throws SQLException
        stmt.setString(1, eventID);                       // throws SQLException
        results = stmt.executeQuery();                    // throws SQLException

        if (results.next() == false) {
            return null;
        }

        Event event = new Event(results.getString(1),results.getString(2),
                results.getString(3),results.getString(4),results.getString(5),
                results.getString(6),results.getString(7),results.getString(8),results.getString(9));

        results.close();                                  // throws SQLException
        stmt.close();
        connection.close();                               // throws SQLException
        connection = null;


        return  event;
    }

    /**
     * Gets the Events related the the specified descendant
     * @param descendant    User (Username) to which this person belongs
     * @return              Returns an array of Events with specified descendant
     */
    public ArrayList<Event> getEventsWithDescendant (String descendant) throws SQLException {

        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet results = null;

        String dbname = "jdbc:sqlite:newTest.db";
        connection = DriverManager.getConnection(dbname); // throws SQLException

        String select = "select * from events where Descendant = ?";
        stmt = connection.prepareStatement(select);        // throws SQLException
        stmt.setString(1, descendant);
        results = stmt.executeQuery();                    // throws SQLException

        ArrayList<Event> events = new ArrayList<>();
        while (results.next()) {
            Event event = new Event(results.getString(1),results.getString(2),
                    results.getString(3),results.getString(4),results.getString(5),
                    results.getString(6),results.getString(7),results.getString(8),results.getString(9));
            events.add(event);
        }

        results.close();                                  // throws SQLException
        stmt.close();
        connection.close();                               // throws SQLException
        connection = null;


        return  events;

    }

    /**
     * Adds the new event to the Event Table
     * @param event Event to be added
     * @return      Returns true if the event was added, false otherwise
     */
    public boolean addNewEvent(Event event) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet results = null;

        String dbname = "jdbc:sqlite:newTest.db";

        connection = DriverManager.getConnection(dbname); // throws SQLException

        if (event.getPersonID() == null) {
            return false;
        }

        String insert = "insert into events values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        stmt = connection.prepareStatement(insert);       // throws SQLException
        stmt.setString(1, event.getEventID());                              // throws SQLException
        stmt.setString( 2, event.getDescendant());                       // throws SQLException
        stmt.setString( 3, event.getPersonID());            // throws SQLException
        stmt.setString( 4, event.getLatitude());
        stmt.setString( 5, event.getLongitude());
        stmt.setString( 6, event.getCountry());
        stmt.setString( 7, event.getCity());
        stmt.setString( 8, event.getEventType());
        stmt.setString( 9, event.getYear());

        int returnCode = stmt.executeUpdate();                             // throws SQLException
        stmt.close();

        if (returnCode == 0) {
            return false;
        }
        else {
            return true;
        }
    }

    /**
     * Deletes the given Event from the Data Base
     * @param eventID The Event to be deleted from Data Base
     * @return      Returns true if the Event was successfully deleted from Data Base, otherwise false
     */
    public boolean deleteEvent (String eventID) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet results = null;

        String dbname = "jdbc:sqlite:newTest.db";

        connection = DriverManager.getConnection(dbname); // throws SQLException

        String delete = "delete from events where EventID = ? ";
        stmt = connection.prepareStatement(delete);       // throws SQLException
        stmt.setString( 1,eventID);                       // throws SQLException
        int returnCode = stmt.executeUpdate();                             // throws SQLException
        stmt.close();

        if (returnCode == 0) {
            return false;
        }
        else {
            return true;
        }
    }

    /**
     * Clears the Event Table of the Data Base
     */
    public void clear() throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;

        String dbname = "jdbc:sqlite:newTest.db";

        // drop the table
        // System.out.println("dropping");
        connection = DriverManager.getConnection(dbname); // throws SQLException
        String drop = "drop table if exists events";
        stmt = connection.prepareStatement(drop);        // throws SQLException
        stmt.executeUpdate();
        stmt.close();

        // create the clean table
        //System.out.println("creating");
        connection = DriverManager.getConnection(dbname); // throws SQLException
        String create = "CREATE TABLE events (\n" +
                "  EventID text not null primary key,\n" +
                "  Descendant text not null,\n" +
                "  Person text not null,\n" +
                "  Latitude text not null,\n" +
                "  Longitude text not null,\n" +
                "  Country text not null,\n" +
                "  City text not null,\n" +
                "  EventType text not null,\n" +
                "  Year text not null\n" +
                ");";
        stmt = connection.prepareStatement(create);       // throws SQLException
        stmt.executeUpdate();
        stmt.close();
        connection.close();                               // throws SQLException
        connection = null;
    }

    /**
     * Gets all the Event objects from the Event Table
     * @return  Returns an array of Event Objects
     */
    public ArrayList<Event> getAllEvents() throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet results = null;

        String dbname = "jdbc:sqlite:newTest.db";

        connection = DriverManager.getConnection(dbname); // throws SQLException
        String select = "select * from events";
        stmt = connection.prepareStatement(select);        // throws SQLException
        results = stmt.executeQuery();                    // throws SQLException

        ArrayList<Event> events = new ArrayList<>();
        while (results.next()) {
            Event event = new Event(results.getString(1),results.getString(2),
                    results.getString(3),results.getString(4),results.getString(5),
                    results.getString(6),results.getString(7),results.getString(8),results.getString(9));
            events.add(event);
        }

        results.close();                                  // throws SQLException
        stmt.close();
        connection.close();                               // throws SQLException
        connection = null;


        return  events;
    }


}
