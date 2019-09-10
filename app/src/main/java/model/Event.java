package model;


/** Type of Data stored in the Data Base */
public class Event {

    /** Unique identifier for this event (non-empty string) */
    private String eventID = new String();

    /** User (Username) to which this personID belongs */
    private String descendant = new String();

    /** ID of personID to which this event belongs */
    private String personID = new String();

    /** Latitude of event’s location */
    private String latitude = new String();

    /** Longitude of event’s location */
    private String longitude = new String();

    /** Country in which event occurred */
    private String country = new String();

    /** City in which event occurred */
    private String city = new String();

    /** Type of event (birth, baptism, christening, marriage, death, etc.) */
    private String eventType = new String();

    /** Year in which event occurred */
    private String year = new String();

    public Event(String eventID, String descendant, String personID, String latitude, String longitude, String country, String city, String eventType, String year) {
        this.eventID = eventID;
        this.descendant = descendant;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getDescendant() {
        return descendant;
    }

    public void setDescendant(String descendant) {
        this.descendant = descendant;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(getEventID());
        sb.append("\n"+getDescendant());
        sb.append("\n"+ getPersonID());
        sb.append("\n"+getLatitude());
        sb.append("\n"+getLongitude());
        sb.append("\n"+getCountry());
        sb.append("\n"+getCity());
        sb.append("\n"+getEventType());
        sb.append("\n"+getYear());

        return sb.toString();
    }
}