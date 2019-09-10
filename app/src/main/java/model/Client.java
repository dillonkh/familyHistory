package model;

import android.graphics.Color;

import com.dillonkharris.familymapclient.LoginFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

import proxy.Proxy;

public class Client {

    private static final Client instance = new Client();
    private Proxy proxy;
    private LoginFragment LoginFragment;
    private Person user;
    private String username;
    private String password;
    private String portNumber;
    private String hostAddress;
    private ArrayList<Person> family;
    private ArrayList<Event> events;
    private Set<String> eventTypes;

    private HashMap<String,Boolean> typeToBool;

    private boolean fathersSideMarkersOn = true;
    private boolean mothersSideMarkersOn = true;

    private boolean maleMarkersOn = true;
    private boolean femaleMarkersOn = true;

    private ArrayList<MarkerOptions> allMarkers;
    private ArrayList<MarkerOptions> birthMarkers;
    private ArrayList<MarkerOptions> baptismMarkers;
    private ArrayList<MarkerOptions> marriageMarkers;
    private ArrayList<MarkerOptions> deathMarkers;
    private ArrayList<MarkerOptions> otherMarkers;
    private ArrayList<MarkerOptions> markersShown;
    private HashMap<MarkerOptions,Event> shownMarkerEventMap;

    private ArrayList<Event> shownEvents;

    private int mapType = GoogleMap.MAP_TYPE_NORMAL;

    private ArrayList<Polyline> spousePolylines = new ArrayList<>();
    private ArrayList<Polyline> familyTreeLines = new ArrayList<>();
    private ArrayList<Polyline> lifeStoryLines = new ArrayList<>();
    private boolean lifeStoryLinesOn = true;
    private boolean familyTreeLinesOn = true;
    private boolean spouseLinesOn = true;
    private int lifeStoryLineColor = Color.GREEN;
    private int familyTreeLineColor = Color.BLUE;
    private int spouseLineColor = Color.RED;

    private Person currentPerson;
    private ArrayList<Person> familyOfCurrentPerson;
    private ArrayList<Event> eventsOfCurrentPerson;

    private Event currentEvent = null;


    private Client () {}

    public void initiateTypeToBool() {
        HashMap<String,Boolean> hash = new HashMap<>();
        for (String str : Client.getInstance().getEventTypes()) {
            hash.put(str,true);
        }
        setTypeToBool(hash);
    }
    public HashMap<String, Boolean> getTypeToBool() {
        return typeToBool;
    }

    public void setTypeToBool(HashMap<String, Boolean> typeToBool) {
        this.typeToBool = typeToBool;
    }

    public void setLoginFragment(com.dillonkharris.familymapclient.LoginFragment loginFragment) {
        LoginFragment = loginFragment;
    }

    public Proxy getProxy() {
        return proxy;
    }

    public void setProxy(Proxy proxy) {
        this.proxy = proxy;
    }

    public static Client getInstance() {
        return instance;
    }

    public String getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(String portNumber) {
        this.portNumber = portNumber;
    }

    public String getHostAddress() {
        return hostAddress;
    }

    public void setHostAddress(String hostAddress) {
        this.hostAddress = hostAddress;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<Event> getShownEvents() {
        return shownEvents;
    }

    public void setShownEvents(ArrayList<Event> shownEvents) {
        this.shownEvents = shownEvents;
    }

    public int getMapType() {
        return mapType;
    }

    public void setMapType(int mapType) {
        this.mapType = mapType;
    }

    public int getLifeStoryLineColor() {
        return lifeStoryLineColor;
    }

    public void setLifeStoryLineColor(int lifeStoryLineColor) {
        this.lifeStoryLineColor = lifeStoryLineColor;
    }

    public int getFamilyTreeLineColor() {
        return familyTreeLineColor;
    }

    public void setFamilyTreeLineColor(int familyTreeLineColor) {
        this.familyTreeLineColor = familyTreeLineColor;
    }

    public int getSpouseLineColor() {
        return spouseLineColor;
    }

    public void setSpouseLineColor(int spouseLineColor) {
        this.spouseLineColor = spouseLineColor;
    }

    public ArrayList<Polyline> getSpousePolylines() {
        return spousePolylines;
    }
    public void setSpousePolylines(ArrayList<Polyline> spousePolylines) {
        this.spousePolylines = spousePolylines;
    }

    public ArrayList<Polyline> getFamilyTreeLines() {
        return familyTreeLines;
    }

    public void setFamilyTreeLines(ArrayList<Polyline> familyTreeLines) {
        this.familyTreeLines = familyTreeLines;
    }

    public ArrayList<Polyline> getLifeStoryLines() {
        return lifeStoryLines;
    }

    public void setLifeStoryLines(ArrayList<Polyline> lifeStoryLines) {
        this.lifeStoryLines = lifeStoryLines;
    }

    public boolean isLifeStoryLinesOn() {
        return lifeStoryLinesOn;
    }

    public void setLifeStoryLinesOn(boolean lifeStoryLinesOn) {
        this.lifeStoryLinesOn = lifeStoryLinesOn;
    }

    public boolean isFamilyTreeLinesOn() {
        return familyTreeLinesOn;
    }

    public void setFamilyTreeLinesOn(boolean familyTreeLinesOn) {
        this.familyTreeLinesOn = familyTreeLinesOn;
    }

    public boolean isSpouseLinesOn() {
        return spouseLinesOn;
    }

    public void setSpouseLinesOn(boolean spouseLinesOn) {
        this.spouseLinesOn = spouseLinesOn;
    }

    public Person getUser() {
        return user;
    }

    public void setUser(Person user) {
        this.user = user;
    }

    public ArrayList<Person> getFamily() {
        return family;
    }

    public void setFamily(ArrayList<Person> family) {
        this.family = family;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }
    public void setEvents(ArrayList<Event> events) {

        this.events = events;
    }

    public void setEventTypes(ArrayList<Event> theEvents) {
        TreeSet<String> set = new TreeSet<String>();
        for (int i = 0; i < theEvents.size(); i++) {
            set.add(theEvents.get(i).getEventType().toLowerCase());
        }
        eventTypes = set;
    }

    public Set<String> getEventTypes() {
        return eventTypes;
    }

    public boolean isFathersSideMarkersOn() {
        return fathersSideMarkersOn;
    }

    public void setFathersSideMarkersOn(boolean fathersSideMarkersOn) {
        this.fathersSideMarkersOn = fathersSideMarkersOn;
    }

    public boolean isMothersSideMarkersOn() {
        return mothersSideMarkersOn;
    }

    public void setMothersSideMarkersOn(boolean mothersSideMarkersOn) {
        this.mothersSideMarkersOn = mothersSideMarkersOn;
    }

    public boolean isMaleMarkersOn() {
        return maleMarkersOn;
    }

    public void setMaleMarkersOn(boolean maleMarkersOn) {
        this.maleMarkersOn = maleMarkersOn;
    }

    public boolean isFemaleMarkersOn() {
        return femaleMarkersOn;
    }

    public void setFemaleMarkersOn(boolean femaleMarkersOn) {
        this.femaleMarkersOn = femaleMarkersOn;
    }

    public void setAllMarkers(ArrayList<MarkerOptions> allMarkers) {
        this.allMarkers = allMarkers;
    }

    public void setMarkersShown(ArrayList<MarkerOptions> markersShown) {
        this.markersShown = markersShown;
    }

    public HashMap<MarkerOptions, Event> getShownMarkerEventMap() {
        return shownMarkerEventMap;
    }

    public void setShownMarkerEventMap(HashMap<MarkerOptions, Event> shownMarkerEventMap) {
        this.shownMarkerEventMap = shownMarkerEventMap;
    }

    public Person getCurrentPerson() {
        return currentPerson;
    }

    public void setCurrentPerson(Person currentPerson) {
        this.currentPerson = currentPerson;
    }
    public ArrayList<Person> getFamilyOfCurrentPerson() {
        return familyOfCurrentPerson;
    }

    public void setFamilyOfCurrentPerson(ArrayList<Person> familyOfCurrentPerson) {
            this.familyOfCurrentPerson = familyOfCurrentPerson;

    }

    public ArrayList<Event> getEventsOfCurrentPerson() {
            return eventsOfCurrentPerson;
    }

    public void setEventsOfCurrentPerson(ArrayList<Event> eventsOfCurrentPerson) {
        this.eventsOfCurrentPerson = eventsOfCurrentPerson;
    }


    public void setCurrentPersonFamily(Person person) {
        ArrayList<Person> list = new ArrayList<>();

        if (person.getSpouse() != null) {
            list.add(findPerson(person.getSpouse()));
        }
        if (person.getFather() != null) {
            list.add(findPerson(person.getFather()));
        }
        if (person.getMother() != null) {
            list.add(findPerson(person.getMother()));
        }
        if (getChild(person) != null) {
            list.add(getChild(person));
        }
        Client.getInstance().setFamilyOfCurrentPerson(list);
    }
    private Person getChild(Person person) {
        for (int i = 0; i < family.size(); i++) {
            if (family.get(i).getFather() != null) {
                if (family.get(i).getFather().equals(person.getPersonID())
                        || family.get(i).getMother().equals(person.getPersonID())) {
                    return family.get(i);
                }
            }

        }
        return null;
    }
    public void setCurrentPersonEvents(Person person) {
        ArrayList<Event> list = new ArrayList<>();
        Client client = Client.getInstance();

        for (int i = 0; i < client.getShownEvents().size(); i++) {
            Event event = client.getShownEvents().get(i);
            if (event.getPersonID().equals(person.getPersonID())) {

                list.add(event);
            }
        }

        Client.getInstance().setEventsOfCurrentPerson(list);
    }
    public Person findPerson(String id) {
        ArrayList<Person> people = Client.getInstance().getFamily();
        for (int i = 0; i < people.size(); i++) {
            if (people.get(i).getPersonID().equals(id)) {
                return people.get(i);
            }
        }
        return null;
    }

    public Event getCurrentEvent() {
        return currentEvent;
    }

    public void setCurrentEvent(Event currentEvent) {
        this.currentEvent = currentEvent;
    }

    public ArrayList<Event> getEventsOfPerson(String person) {
        ArrayList<Event> events = new ArrayList<>();

        for (int i = 0; i < getShownEvents().size(); i++) {
            if (getShownEvents().get(i).getPersonID().equals(person)) {
                events.add(getShownEvents().get(i));
            }
        }
        return events;
    }

    public ArrayList<Person> getFamilyOfPerson(Person person) {
        ArrayList<Person> people = new ArrayList<>();

        if (person == null) {
            return null;
        }

        for (int i = 0; i < getFamily().size(); i++) {
            if (getFamily().get(i).getPersonID().equals(person.getFather())
                        || getFamily().get(i).getPersonID().equals(person.getMother())) {
                    people.add(getFamily().get(i));
            }
            people.add(getChild(person));

        }
        return people;
    }
    public boolean notFiltered(Event event) {
        String type = event.getEventType().toLowerCase();
        if (getTypeToBool().get(type) == true) {
            return true;
        }
        else {
            return false;
        }
    }

    public Event getEarliestEvent(ArrayList<Event> events) {
        final int FUTUREDATE = 2030;
        int currEarliestDate = FUTUREDATE;
        Event earliestEvent = null;

        if (events == null) {
            return null;
        }

        for (int i = 0; i < events.size(); i++) {
            if (events.get(i).getYear().length() != 4) {
                continue;
            }
            int eventyear = Integer.parseInt(events.get(i).getYear());
            if (eventyear < currEarliestDate) {
                currEarliestDate = eventyear;
                earliestEvent = events.get(i);
            }
        }
        return earliestEvent;
    }

    public void turnOnEventsType(String type) {
        ArrayList<Event> eventsToSave = new ArrayList<>();

        for (int i = 0; i < getEvents().size(); i++) {
            String eventType = getEvents().get(i).getEventType().toLowerCase();
            if (eventType.equals(type.toLowerCase())) {
                eventsToSave.add(getEvents().get(i));
            }
        }
        eventsToSave.addAll(getShownEvents());
        setShownEvents(eventsToSave);
    }
    public void turnOffEventsGender(String gender) {
        ArrayList<Event> shownEvents = getShownEvents();

        ArrayList<Event> eventsToShow = new ArrayList<>();
        for (int i = 0; i < shownEvents.size(); i++) {
            Person person = findPerson(shownEvents.get(i).getPersonID());
            if (!person.getGender().toLowerCase().equals(gender)) {
                eventsToShow.add(shownEvents.get(i));
                System.out.println("keeping: " + person.getGender());
            }

        }
        setShownEvents(eventsToShow);
    }

    private ArrayList<Person> leaveOn(String personId) {

        ArrayList<Person> peopleToKeep = new ArrayList<>();
        Person currPerson = findPerson(personId);
        peopleToKeep.add(currPerson);
        if (currPerson.getFather() != null) {
            peopleToKeep.addAll(leaveOn(currPerson.getFather()));
        }
        if (currPerson.getMother() != null) {
            peopleToKeep.addAll(leaveOn(currPerson.getMother()));
        }
        return peopleToKeep;
    }
    public void turnOffEventsBySide(String side) {
        ArrayList<Person> peopleToLeaveOn = new ArrayList<>();
        Person userLoggedIn = getUser();

        if (side.equals("d")) {
            peopleToLeaveOn = leaveOn(userLoggedIn.getMother());
        }
        else {
            peopleToLeaveOn = leaveOn(userLoggedIn.getFather());
        }
        peopleToLeaveOn.add(userLoggedIn);
        ArrayList<Event> eventsToLeaveOn = new ArrayList<>();
        for(int i = 0; i < peopleToLeaveOn.size(); i++) {
            eventsToLeaveOn.addAll(getEventsOfPerson(peopleToLeaveOn.get(i).getPersonID()));
        }
        setShownEvents(eventsToLeaveOn);

    }

    public void reApplyFilters() {
        setShownEvents(new ArrayList<Event>());
        HashMap<String,Boolean> typeMap = getTypeToBool();

        for (String str : typeMap.keySet()) {
            if (typeMap.get(str) == true) {
                turnOnEventsType(str.toLowerCase());
            }
        }

        if (isFathersSideMarkersOn() != true) {
            turnOffEventsBySide("d");
        }

        if (isMothersSideMarkersOn() != true) {
            turnOffEventsBySide("m");
        }

        if (isMaleMarkersOn() != true) {
            turnOffEventsGender("m");
        }

        if (isFemaleMarkersOn() != true) {
            turnOffEventsGender("f");
        }

    }

}
