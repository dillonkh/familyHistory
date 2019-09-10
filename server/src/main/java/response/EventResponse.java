package response;

/** Response for the single Event Service */
public class EventResponse {

    /**  Unique identifier for this event (non-empty string) */
    private String descendant = null;

    /**  User (Username) to which this person belongs */
    private String eventID = null;

    /**  ID of person to which this event belongs */
    private String personID = null;

    /**  Latitude of event’s location */
    private String latitude = null;

    /**  Longitude of event’s location */
    private String longitude = null;

    /**  Country in which event occurred */
    private String country = null;

    /**  City in which event occurred */
    private String city = null;

    /**  Type of event (birth, baptism, christening, marriage, death, etc.) */
    private String eventType = null;

    /**  Year in which event occurred */
    private String year = null;

    /**  String used to contain the DefaultResponse Message that will be displayed in case of error */
    private String message = null;

    public EventResponse(String descendant, String eventID, String personID, String latitude, String longitude, String country, String city, String eventType, String year) {
        this.descendant = descendant;
        this.eventID = eventID;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
//        this.message = message;
    }

    public EventResponse(String message) {
        this.message = message;
    }

    public String getDescendant() {
        return descendant;
    }

    public void setDescendant(String descendant) {
        this.descendant = descendant;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}