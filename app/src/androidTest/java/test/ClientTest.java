package test;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import model.Client;
import model.Event;
import response.EventsResponse;
import request.LoginRequest;
import response.PeopleResponse;
import model.Person;
import response.RegisterLoginResponse;
import proxy.Proxy;

import static org.junit.Assert.*;

public class ClientTest {
    Proxy proxy = new Proxy();
    String personID;
    Person person = new Person();

    @Before
    public void setUp() throws Exception {
        Client.getInstance().setProxy(new Proxy());

        Client.getInstance().setPortNumber("8080");
        Client.getInstance().setHostAddress("192.168.1.142");

        LoginRequest request = new LoginRequest("dill","123");
        RegisterLoginResponse resp = new Proxy().login(request);

        personID = resp.getPersonID();
        person = Client.getInstance().getProxy().getPerson(personID,resp.getAuthToken());

        PeopleResponse family = Client.getInstance().getProxy().getFamily(resp.getAuthToken());
        EventsResponse events = Client.getInstance().getProxy().getEvents(resp.getAuthToken());

        Client.getInstance().setEvents(events.getEvents());
        Client.getInstance().setShownEvents(events.getEvents());
        Client.getInstance().setEventTypes(events.getEvents());
        Client.getInstance().initiateTypeToBool();
        Client.getInstance().setFamily(family.getPeople());
        Client.getInstance().setUser(person);
    }

    @Test
    public void getFamily() {
        Client client = Client.getInstance();
        ArrayList<Person> family = client.getFamily();

        //pos case
        assertTrue(family.size() > 0);
        assertTrue(family.get(0).getDescendant().equals("dill"));

        // neg case
        client.setFamily(null);
        family = client.getFamily();
        assertTrue(family == null);
    }

    @Test
    public void getEvents() {
        Client client = Client.getInstance();
        ArrayList<Event> events = client.getEvents();

        //pos case
        assertTrue(events.size() > 0);
        assertTrue(events.get(0).getDescendant().equals("dill"));

        //neg case
        client.setEvents(null);
        events = client.getEvents();
        assertTrue(events == null);
    }

    @Test
    public void findPerson() {
        Client client = Client.getInstance();
        Person person = client.findPerson(personID);

        //pos case
        assertTrue(person.getDescendant().equals("dill"));
        assertTrue(person != null);

        //neg case
        person = client.findPerson("a string");
        assertTrue(person == null);

    }

    @Test
    public void getEventsOfPerson() {
        Client client = Client.getInstance();
        ArrayList<Event> events = client.getEventsOfPerson(personID);

        //pos case
        assertTrue(events.size() > 0);
        assertTrue(events.get(0).getDescendant().equals("dill"));

        //neg  case
        events = client.getEventsOfPerson(null);
        assertTrue(events.size() == 0);

    }

    @Test
    public void getFamilyOfPerson() {
        Client client = Client.getInstance();
        ArrayList<Person> family = client.getFamilyOfPerson(person);

        //pos case
        assertTrue(family.size() > 0);
        for (int i = 0; i < family.size(); i++) {
            if (family.get(i) != null) {
                assertTrue(family.get(i).getDescendant().equals("dill"));
            }
        }

        //neg case
        family = client.getFamilyOfPerson(null);
        assertTrue(family == null);
    }

    @Test
    public void getEarliestEvent() {
        Client client = Client.getInstance();
        ArrayList<Event> events = client.getEventsOfPerson(personID);

        //pos case
        Event event = client.getEarliestEvent(events);
        assertTrue(event.getYear().equals("1990"));
        assertTrue(event != null);

        //neg case
        event = client.getEarliestEvent(null);
        assertTrue(event == null);

        event = client.getEarliestEvent(new ArrayList<Event>());
        assertTrue(event == null);
    }

    @Test
    public void reApplyFilters() {
        Client client = Client.getInstance();

        ArrayList<Event> allEvents = client.getShownEvents();
        client.reApplyFilters();
        ArrayList<Event> shownEvents = client.getShownEvents();

        //pos case
        assertTrue(allEvents.size() == shownEvents.size());
        assertTrue(shownEvents.size() == 122);

        client.setFemaleMarkersOn(false);
        client.reApplyFilters();
        shownEvents = client.getShownEvents();
        assertTrue(shownEvents.size() < allEvents.size());
        for (int i = 0; i < shownEvents.size(); i++) {
            Person person = client.findPerson(shownEvents.get(i).getPersonID());
            assertTrue(person.getGender().toLowerCase().equals("m"));
        }

        client.setMaleMarkersOn(false);
        client.reApplyFilters();
        shownEvents = client.getShownEvents();
        assertTrue(shownEvents.size() == 0);

        client.setMaleMarkersOn(true);
        client.setFemaleMarkersOn(true);
        client.reApplyFilters();
        shownEvents = client.getShownEvents();

        assertTrue(shownEvents.size() > 0);

        client.setFathersSideMarkersOn(false);
        client.setMothersSideMarkersOn(false);
        client.reApplyFilters();
        shownEvents = client.getShownEvents();
        assertTrue(shownEvents.size() == 2); //users 2 events

        client.setMothersSideMarkersOn(true);
        client.setFathersSideMarkersOn(true);

        client.setEventTypes(client.getEvents());
        client.initiateTypeToBool();
        HashMap<String,Boolean> hash = new HashMap<>();
        for (String type : client.getTypeToBool().keySet()) {
            hash.put(type,false);
        }
        client.setTypeToBool(hash);
        client.reApplyFilters();
        assertTrue(client.getShownEvents().size() == 0);

        hash = new HashMap<>();
        for (String type : client.getTypeToBool().keySet()) {
            hash.put(type,true);
        }
        client.setTypeToBool(hash);
        client.reApplyFilters();
        assertTrue(client.getShownEvents().size() > 0);
    }
}