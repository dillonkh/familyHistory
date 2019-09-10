package model;

/** A type of data stored in the Data Base */
public class AuthToken {

    /** A unique token generated for each authorized login */
    private String token = new String();
    private String personID = new String();

    public AuthToken(String token, String personID) {
        this.token = token;
        this.personID = personID;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPersonID() {
        //System.out.println(personID);
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(getToken());
        sb.append("\n"+getPersonID());

        return sb.toString();
    }

}
