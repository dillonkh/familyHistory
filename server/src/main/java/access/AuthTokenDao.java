package access;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import model.AuthToken;
import model.User;

/**  Accesses the AuthToken table in the Data Base. May return rows from that table or add new rows. */
public class AuthTokenDao {

    /**
     * Returns the User Object who belongs to the given token
     * @param token     AuthToken to be found in Data Base
     * @return          Returns the User with given token
     */
    public User getUserWithToken (String token) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet results = null;

        String dbname = "jdbc:sqlite:newTest.db";
        //System.out.println("reached here");
        connection = DriverManager.getConnection(dbname); // throws SQLException

        //String query = "select * from member";
        String select = "select * from authTokens where Token = ?";
        stmt = connection.prepareStatement(select);        // throws SQLException
        stmt.setString(1, token);                       // throws SQLException
        results = stmt.executeQuery();                    // throws SQLException
        if (results.next() == false) {
            return null;
        }
        AuthToken newToken = new AuthToken(results.getString(1),results.getString(2));

        //results.close();                                  // throws SQLException
        //stmt.close();

        select = "select * from users where PersonID = ?";
        stmt = connection.prepareStatement(select);        // throws SQLException
        stmt.setString(1, newToken.getPersonID());      // throws SQLException
        results = stmt.executeQuery();                    // throws SQLException

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

    public AuthToken getTokenWithPersonID (String personID) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet results = null;

        String dbname = "jdbc:sqlite:newTest.db";
        //System.out.println("reached here");
        connection = DriverManager.getConnection(dbname); // throws SQLException

        //String query = "select * from member";
        String select = "select * from authTokens where PersonID = ?";
        stmt = connection.prepareStatement(select);        // throws SQLException
        stmt.setString(1, personID);                       // throws SQLException
        results = stmt.executeQuery();                    // throws SQLException
        if (results.next() == false) {
            return null;
        }
        AuthToken newToken = new AuthToken(results.getString(1),results.getString(2));

        results.close();                                  // throws SQLException
        stmt.close();
        connection.close();                               // throws SQLException
        connection = null;


        return  newToken;
    }
    public ArrayList<AuthToken> getTokensWithPersonID (String personID) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet results = null;

        String dbname = "jdbc:sqlite:newTest.db";
        //System.out.println("reached here");
        connection = DriverManager.getConnection(dbname); // throws SQLException

        //String query = "select * from member";
        String select = "select * from authTokens where PersonID = ?";
        stmt = connection.prepareStatement(select);        // throws SQLException
        stmt.setString(1, personID);                       // throws SQLException
        results = stmt.executeQuery();                    // throws SQLException


        ArrayList<AuthToken> newTokens = new ArrayList<AuthToken>();
        if (results.next() == false) {
            return null;
        }
        else {
            newTokens.add(new AuthToken(results.getString(1),results.getString(2)));
            while (results.next()) {
                newTokens.add(new AuthToken(results.getString(1),results.getString(2)));
            }
        }

        //ArrayList<AuthToken> newTokens = new ArrayList<AuthToken>();
//        while (results.next()) {
//            newTokens.add(new AuthToken(results.getString(1),results.getString(2)));
//        }
        System.out.println("all the tokens dao got: " + newTokens);

        results.close();                                  // throws SQLException
        stmt.close();
        connection.close();                               // throws SQLException
        connection = null;


        return  newTokens;
    }

    /**
     * Returns the AuthToken Object for the given token
     * @param token     AuthToken to be found in the Data Base
     * @return          Returns the given token if found
     */
    public AuthToken getToken (String token) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet results = null;

        String dbname = "jdbc:sqlite:newTest.db";
        //System.out.println("reached here");
        connection = DriverManager.getConnection(dbname); // throws SQLException

        //String query = "select * from member";
        String select = "select * from authTokens where Token = ?";
        stmt = connection.prepareStatement(select);        // throws SQLException
        stmt.setString(1, token);                       // throws SQLException
        results = stmt.executeQuery();                    // throws SQLException
        if (results.next() == false) {
            return null;
        }
        AuthToken newToken = new AuthToken(results.getString(1),results.getString(2));

        results.close();                                  // throws SQLException
        stmt.close();
        connection.close();                               // throws SQLException
        connection = null;


        return  newToken;
    }

    /**
     * Adds a new AuthToken to the Data Base
     * @param token     AuthToken to be added to the Data Base
     * @return          Returns true if added successfully, otherwise false
     */
    public boolean addNewToken (AuthToken token) throws SQLException {
       System.out.println(token.getToken());
        System.out.println(token.getPersonID());

        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet results = null;

        String dbname = "jdbc:sqlite:newTest.db";

        connection = DriverManager.getConnection(dbname); // throws SQLException

        String insert = "insert into authTokens values (?, ?)";
        stmt = connection.prepareStatement(insert);       // throws SQLException
        stmt.setString(1, token.getToken());                              // throws SQLException
        stmt.setString( 2, token.getPersonID());                       // throws SQLException

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
     * Deletes the given token from Data Base
     * @param token     AuthToken to be deleted from Data Base
     * @return          Returns true if the given token was successfully deleted, otherwise returns false
     */
    public boolean deleteToken(String token) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet results = null;

        String dbname = "jdbc:sqlite:newTest.db";

        connection = DriverManager.getConnection(dbname); // throws SQLException

        String delete = "delete from authTokens where Token = ? ";
        stmt = connection.prepareStatement(delete);       // throws SQLException
        stmt.setString( 1, token);                       // throws SQLException
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
     * Clears the AuthToken Table of the Data Base
     */
    public void clear() throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;

        String dbname = "jdbc:sqlite:newTest.db";

        // drop the table
        // System.out.println("dropping");
        connection = DriverManager.getConnection(dbname); // throws SQLException
        String drop = "drop table if exists authTokens";
        stmt = connection.prepareStatement(drop);        // throws SQLException
        stmt.executeUpdate();
        stmt.close();

        // create the clean table
        //System.out.println("creating");
        connection = DriverManager.getConnection(dbname); // throws SQLException
        String create = "CREATE TABLE authTokens (\n" +
                "  Token text not null primary key,\n" +
                "  PersonID text not null\n" +
                ");";
        stmt = connection.prepareStatement(create);       // throws SQLException
        stmt.executeUpdate();
        stmt.close();
        connection.close();                               // throws SQLException
        connection = null;
    }

    /**
     * Gets all the AuthToken objects from the AuthToken Table
     * @return  Returns an array of AuthToken Objects
     */
    public ArrayList<AuthToken> getAllTokens() throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet results = null;

        String dbname = "jdbc:sqlite:newTest.db";

        connection = DriverManager.getConnection(dbname); // throws SQLException
        String select = "select * from authTokens";
        stmt = connection.prepareStatement(select);        // throws SQLException
        results = stmt.executeQuery();                    // throws SQLException

        ArrayList<AuthToken> events = new ArrayList<>();
        while (results.next()) {
            AuthToken token = new AuthToken(results.getString(1),results.getString(2));
            events.add(token);
        }

        results.close();                                  // throws SQLException
        stmt.close();
        connection.close();                               // throws SQLException
        connection = null;


        return  events;
    }
}
