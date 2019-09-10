package access;
// Person ID: Unique identifier for this person (non-empty string)
// Descendant: User (Username) to which this person belongs
// First Name: Person’s first name (non-empty string)
// Last Name: Person’s last name (non-empty string)
// Gender: Person’s gender (string: “f” or “m”)
// Father: ID of person’s father (possibly null)
// Mother: ID of person’s mother (possibly null)
// Spouse: ID of person’s spouse (possibly null)

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import model.Person;

/**  Accesses the Person table in the Data Base. May return rows from that table or add new rows. */
public class PersonDao {

    /**
     * Gets the person from the Data Base with specified ID
     * @param id    Unique identifier for this person (non-empty string)
     * @return      Returns the Person with given ID
     */
    public Person getPersonWithPersonID (String id) throws SQLException {

        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet results = null;

        String dbname = "jdbc:sqlite:newTest.db";
        connection = DriverManager.getConnection(dbname); // throws SQLException

        String select = "select * from persons where PersonID = ?";
        stmt = connection.prepareStatement(select);        // throws SQLException
        stmt.setString(1, id);
        results = stmt.executeQuery();                    // throws SQLException

        if (results.next() == false) {
            return null;
        }

        Person person = new Person(results.getString(1),results.getString(2),
                results.getString(3),results.getString(4),results.getString(5),
                results.getString(6),results.getString(7),results.getString(8));

        results.close();                                  // throws SQLException
        stmt.close();
        connection.close();                               // throws SQLException
        connection = null;


        return  person;
    }

    /**
     * Gets all the people in Data Base that have the specified descendant
     * @param descendant    User (Username) to which this person belongs
     * @return              Returns Array of Persons with given descendant
     */
    public ArrayList<Person> getPeopleWithDescendant (String descendant) throws SQLException {

        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet results = null;

        String dbname = "jdbc:sqlite:newTest.db";
        connection = DriverManager.getConnection(dbname); // throws SQLException

        String select = "select * from persons where Descendant = ?";
        stmt = connection.prepareStatement(select);        // throws SQLException
        stmt.setString(1, descendant);
        results = stmt.executeQuery();                    // throws SQLException

        ArrayList<Person> people = new ArrayList<>();
        while (results.next()) {
            Person person = new Person(results.getString(1),results.getString(2),
                    results.getString(3),results.getString(4),results.getString(5),
                    results.getString(6),results.getString(7),results.getString(8));
            people.add(person);
        }

        results.close();                                  // throws SQLException
        stmt.close();
        connection.close();                               // throws SQLException
        connection = null;


        return  people;
    }

    /**
     * Adds the given Person to the Data Base
     * @param personToAdd   The Person that will be added to the Data Base
     * @return              Returns true if successfully added, otherwise false
     */
    public boolean addNewPerson(Person personToAdd) throws SQLException {
        //System.out.println("in add person");
        if (personToAdd == null) {
            return false;
        }
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet results = null;

        String dbname = "jdbc:sqlite:newTest.db";

        connection = DriverManager.getConnection(dbname); // throws SQLException
        //System.out.println(personToAdd.getPersonID());
        String insert = "insert into persons values (?, ?, ?, ?, ?, ?, ?, ?)";
        stmt = connection.prepareStatement(insert);       // throws SQLException
        stmt.setString(1, personToAdd.getDescendant());                              // throws SQLException
        stmt.setString( 2, personToAdd.getPersonID());                       // throws SQLException
        stmt.setString( 3, personToAdd.getFirstName());            // throws SQLException
        stmt.setString( 4, personToAdd.getLastName());
        stmt.setString( 5, personToAdd.getGender());
        stmt.setString( 6, personToAdd.getFather());
        stmt.setString( 7, personToAdd.getMother());
        stmt.setString( 8, personToAdd.getSpouse());

        int returnCode = stmt.executeUpdate();                             // throws SQLException
        stmt.close();

        if (returnCode == 0) {
            return false;
        }
        else {
            return true;
        }
    }

    public boolean addSpouseOf(Person person, Person spouse) throws SQLException {
        if (person == null) {
            return false;
        }
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet results = null;
        String spouseID = spouse.getPersonID();
        String personID = person.getPersonID();

        //System.out.println("both ids in db: \n"+spouseID+"\n"+personID);

        String dbname = "jdbc:sqlite:newTest.db";
        connection = DriverManager.getConnection(dbname); // throws SQLException


        String insert = "UPDATE persons SET Spouse = ? WHERE PersonID = ?;";
        stmt = connection.prepareStatement(insert);       // throws SQLException
        stmt.setString(1, spouseID);
        stmt.setString(2, personID);
        int returnCode = stmt.executeUpdate();                             // throws SQLException
        stmt.close();

        if (returnCode == 0) {
            return false;
        }


        insert = "UPDATE persons SET Spouse = ? WHERE PersonID = ?;";
        stmt = connection.prepareStatement(insert);       // throws SQLException
        stmt.setString(1, personID);
        stmt.setString(2, spouseID);
        returnCode = stmt.executeUpdate();                             // throws SQLException
        stmt.close();

        if (returnCode == 0) {
            return false;
        }
        else {
            return true;
        }
    }

    /**
     * Returns true if the Person was successfully deleted from Data Base, otherwise false
     * @param personID    The Person that will be deleted from the Data Base
     * @return          Returns true if deleted successfully, otherwise false
     */
    public boolean deletePerson (String personID) throws SQLException {

        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet results = null;

        String dbname = "jdbc:sqlite:newTest.db";

        connection = DriverManager.getConnection(dbname); // throws SQLException

        String delete = "delete from persons where PersonID = ? ";
        stmt = connection.prepareStatement(delete);       // throws SQLException
        stmt.setString( 1,personID);                       // throws SQLException
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
     * Clears the Person Table of the Data Base
     */
    public void clear() throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;

        String dbname = "jdbc:sqlite:newTest.db";

        // drop the table
        // System.out.println("dropping");
        connection = DriverManager.getConnection(dbname); // throws SQLException
        String drop = "drop table if exists persons";
        stmt = connection.prepareStatement(drop);        // throws SQLException
        stmt.executeUpdate();
        stmt.close();

        // create the clean table
        //System.out.println("creating");
        connection = DriverManager.getConnection(dbname); // throws SQLException
        String create = "CREATE TABLE persons (\n" +
                "  Descendant text not null,\n" +
                "  PersonID text not null primary key,\n" +
                "  FirstName text not null,\n" +
                "  LastName text not null,\n" +
                "  Gender text not null,\n" +
                "  Father text,\n" +
                "  Mother text,\n" +
                "  Spouse text\n" +
                ");";
        stmt = connection.prepareStatement(create);       // throws SQLException
        stmt.executeUpdate();
        stmt.close();
        connection.close();                               // throws SQLException
        connection = null;
    }

    /**
     * Gets all the Person objects from the Person Table
     * @return  Returns an array of Person Objects
     */
    public ArrayList<Person> getAllPeople() throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet results = null;

        String dbname = "jdbc:sqlite:newTest.db";

        connection = DriverManager.getConnection(dbname); // throws SQLException
        String select = "select * from persons";
        stmt = connection.prepareStatement(select);        // throws SQLException
        results = stmt.executeQuery();                    // throws SQLException

        ArrayList<Person> people = new ArrayList<>();
        while (results.next()) {
            Person person = new Person(results.getString(1),results.getString(2),
                    results.getString(3),results.getString(4),results.getString(5),
                    results.getString(6),results.getString(7),results.getString(8));
            people.add(person);
        }

        results.close();                                  // throws SQLException
        stmt.close();
        connection.close();                               // throws SQLException
        connection = null;


        return  people;
    }
}
