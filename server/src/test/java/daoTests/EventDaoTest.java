package daoTests;

import org.junit.* ;

import java.sql.SQLException;
import java.util.ArrayList;

import access.EventDao;
import model.Event;

import static org.junit.Assert.* ;

public class EventDaoTest {

    @Before
    public void setUp() throws Exception {
        EventDao access = new EventDao();

        System.out.println("clearing");
        access.clear();

        System.out.println("adding baptism");
        Event test = new Event("1","tom","personid1","1000","2000","USA","Provo","Baptism","1990");
        access.addNewEvent(test);

        System.out.println("adding marriage");
        test = new Event("2","tom","personid2","1000","2000","USA","Provo","Marriage","2007");
        access.addNewEvent(test);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getEventWithEventID() throws SQLException {
        EventDao access = new EventDao();
        assertTrue(access.getEventWithEventID("1") != null);
        assertTrue(access.getEventWithEventID("2") != null);
    }

    @Test
    public void getEventsWithDescendant() throws SQLException {
        EventDao access = new EventDao();
        ArrayList<Event> list = new ArrayList<>();

        list = access.getEventsWithDescendant("tom");
        assertTrue(list.size() == 2);

        list = access.getEventsWithDescendant("joe");
        assertTrue(list.size() == 0);
    }

    @Test
    public void deleteEvent() throws SQLException {
        EventDao access = new EventDao();
        ArrayList<Event> list = new ArrayList<>();

        System.out.println("adding death");
        Event test = new Event("3","tom","personid2","1000","2000","USA","Provo","Marriage","2007");
        access.addNewEvent(test);

        list = access.getAllEvents();
        assertTrue(list.size() == 3);

        access.deleteEvent("3");
        list = access.getAllEvents();
        assertTrue(list.size() == 2);
    }

    @Test
    public void clear() throws SQLException {
        EventDao access = new EventDao();
        ArrayList<Event> list = new ArrayList<>();

        list = access.getAllEvents();
        assertTrue(list.size() == 2);

        access.clear();
        list = access.getAllEvents();
        assertTrue(list.size() == 0);
    }

}