package access;
// Username: Unique user name (non-empty string)
// Password: User’s password (non-empty string)
// Email: User’s email address (non-empty string)
// First Name: User’s first name (non-empty string)
// Last Name: User’s last name (non-empty string)
// Gender: User’s gender (string: “f” or “m”)
// Person ID: Unique Person ID assigned to this user’s generated Person object
import java.io.*;
import java.sql.Array;
import java.sql.*;
import java.util.*;

import model.Person;
import model.User;

/**  Accesses the User table in the Data Base. May return rows from that table or add new rows. */
public class UserDao {


    /**
     * Gets the user with specified username
     * @param username  Unique user name (non-empty string)
     * @return          Returns the User for given username
     */
    public User getUserWithUsername (String username) throws SQLException {
        System.out.println("in get user with username: " + username);
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet results = null;

        String dbname = "jdbc:sqlite:newTest.db";

        connection = DriverManager.getConnection(dbname); // throws SQLException

        String query = "select * from users where Username = ?;";
        stmt = connection.prepareStatement(query);        // throws SQLException
        stmt.setString(1, username);                       // throws SQLException
        results = stmt.executeQuery();                    // throws SQLException
//        System.out.print("results is ");
//        System.out.println(results.next() == false);
        if (results.next() == false) {
            System.out.println("returning null");
            return  null;
        }
        else {
            User user = new User(results.getString(1),results.getString(2),
                    results.getString(3),results.getString(4),results.getString(5),
                    results.getString(6),results.getString(7));



            results.close();                                  // throws SQLException
            stmt.close();
            connection.close();                               // throws SQLException
            connection = null;


            return  user;
        }
    }

    /**
     * Gets the user with the specified password
     * @param password  User’s password (non-empty string)
     * @return          Returns the User with given password
     */
    public User getUserWithPassword (String password) throws SQLException {

        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet results = null;

        String dbname = "jdbc:sqlite:newTest.db";

        connection = DriverManager.getConnection(dbname); // throws SQLException
        String query = "select * from users where Password = ?";
        stmt = connection.prepareStatement(query);        // throws SQLException
        stmt.setString(1, password);
        results = stmt.executeQuery();                    // throws SQLExceptio

        if (results.next() == false) {
            return null;
        }

        User user = new User(results.getString(1),results.getString(2),
                results.getString(3),results.getString(4),results.getString(5),
                results.getString(6),results.getString(7));

        results.close();                                  // throws SQLException
        stmt.close();
        connection.close();                               // throws SQLException
        connection = null;


        return  user;
    }

    /**
     * Gets the user with the specified email
     * @param email User’s email address (non-empty string)
     * @return      Returns the User with given email
     */
    public User getUserWithEmail (String email) throws SQLException{

        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet results = null;

        String dbname = "jdbc:sqlite:newTest.db";

        connection = DriverManager.getConnection(dbname); // throws SQLException
        String query = "select * from users where Email = ?";
        stmt = connection.prepareStatement(query);        // throws SQLException
        stmt.setString(1, email);
        results = stmt.executeQuery();                    // throws SQLExceptio

        User user = new User(results.getString(1),results.getString(2),
                results.getString(3),results.getString(4),results.getString(5),
                results.getString(6),results.getString(7));

        results.close();                                  // throws SQLException
        stmt.close();
        connection.close();                               // throws SQLException
        connection = null;


        return  user;
    }

    /**
     * Gets the user with the specified personID
     * @param personID  Unique Person ID assigned to this user’s generated Person object
     * @return          Returns the User with given id
     */
    public User getUserWithPersonID (String personID) throws SQLException {

        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet results = null;

        String dbname = "jdbc:sqlite:newTest.db";

        connection = DriverManager.getConnection(dbname); // throws SQLException
        String query = "select * from users where PersonID = ?";
        stmt = connection.prepareStatement(query);        // throws SQLException
        stmt.setString(1, personID);
        results = stmt.executeQuery();                    // throws SQLExceptio

        User user = new User(results.getString(1),results.getString(2),
                results.getString(3),results.getString(4),results.getString(5),
                results.getString(6),results.getString(7));

        results.close();                                  // throws SQLException
        stmt.close();
        connection.close();                               // throws SQLException
        connection = null;


        return  user;
    }

    /**
     * Adds the given User Object to the User Table in Data Base
     * @param newUser   User to add to Data Base
     * @return          Returns true if added successfully, otherwise false
     */
    public boolean addNewUser(User newUser) throws SQLException {
        System.out.println("in userDao");
        //System.out.println(newUser.getUserName());
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet results = null;

        String dbname = "jdbc:sqlite:newTest.db";

        connection = DriverManager.getConnection(dbname); // throws SQLException

        String insert = "insert into users values (?, ?, ?, ?, ?, ?, ?)";
        stmt = connection.prepareStatement(insert);       // throws SQLException
//        stmt.setString(1, newUser.getUserID());                              // throws SQLException
        stmt.setString( 1, newUser.getUserName());                       // throws SQLException
        stmt.setString( 2, newUser.getPassword());            // throws SQLException
        stmt.setString( 3, newUser.getEmail());
        stmt.setString( 4, newUser.getFirstName());
        stmt.setString( 5, newUser.getLastName());
        stmt.setString( 6, newUser.getGender());
        stmt.setString( 7, newUser.getPersonID());

        int returnCode = stmt.executeUpdate();                             // throws SQLException
        stmt.close();
        //System.out.println(returnCode);
        if (returnCode == 0) {

            return false;
        }
        else {
//            PersonDao personAccess = new PersonDao();
//            Person person = new Person(newUser.getPersonID(),
//                    newUser.getPersonID(),newUser.getFirstName(),
//                    newUser.getLastName(),newUser.getGender(),
//                    null,null,null);
//            personAccess.addNewPerson(person);
            return true;
        }
    }

    /**
     * Returns true if the user was deleted, false otherwise
     * @param user  User to delete from Data Base
     * @return      Returns true if deleted successfully, otherwise false
     */
    public boolean deleteUser (User user) throws SQLException {

        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet results = null;

        String dbname = "jdbc:sqlite:newTest.db";

        connection = DriverManager.getConnection(dbname); // throws SQLException

        String delete = "delete from users where Username = ? ";
        stmt = connection.prepareStatement(delete);       // throws SQLException
        stmt.setString( 1,user.getUserName());                       // throws SQLException
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
     * Clears the User Table of the Data Base
     */
    public void clear() throws SQLException {

        Connection connection = null;
        PreparedStatement stmt = null;

        String dbname = "jdbc:sqlite:newTest.db";

        // drop the table
            // System.out.println("dropping");
        connection = DriverManager.getConnection(dbname); // throws SQLException
        String drop = "drop table if exists users";
        stmt = connection.prepareStatement(drop);        // throws SQLException
        stmt.executeUpdate();
        stmt.close();

        // create the clean table
            //System.out.println("creating");
        connection = DriverManager.getConnection(dbname); // throws SQLException
        String create = "CREATE TABLE if not exists users ( " +
                "Username text not null primary key, " +
                "Password text not null, " +
                "Email text not null, " +
                "FirstName text not null, " +
                "LastName text not null, " +
                "Gender text not null, " +
                "PersonID text not null);";
        stmt = connection.prepareStatement(create);       // throws SQLException
        stmt.executeUpdate();
        stmt.close();
        connection.close();                               // throws SQLException
        connection = null;
    }

    /**
     * Gets all the Users from the User Table
     * @return  Returns an array of User Objects
     */
    public ArrayList<User> getAllUsers() throws SQLException {

        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet results = null;

        String dbname = "jdbc:sqlite:newTest.db";

        connection = DriverManager.getConnection(dbname); // throws SQLException
        String select = "select * from users";
        stmt = connection.prepareStatement(select);        // throws SQLException
        results = stmt.executeQuery();                    // throws SQLException

        ArrayList<User> users = new ArrayList<>();
        while (results.next()) {
            User user = new User(results.getString(1),results.getString(2),
                    results.getString(3),results.getString(4),results.getString(5),
                    results.getString(6),results.getString(7));
            users.add(user);
        }

        results.close();                                  // throws SQLException
        stmt.close();
        connection.close();                               // throws SQLException
        connection = null;


        return  users;
    }

}
