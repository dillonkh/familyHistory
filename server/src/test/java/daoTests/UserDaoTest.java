package daoTests;

import org.junit.* ;

import java.sql.SQLException;
import java.util.ArrayList;

import access.UserDao;
import model.User;

import static org.junit.Assert.* ;

public class UserDaoTest { // Tests all the needed UserDao methods

    @Before
    public void setUp() throws Exception {
        UserDao access = new UserDao();

        System.out.println("clearing");
        access.clear();

        System.out.println("adding bob");
        User test = new User("bob3","password1","email1","bob","by","m","1");
        access.addNewUser(test);

        System.out.println("adding joe");
        User test2 = new User("joe3","password2","email2","joe","dirt","m","2");
        access.addNewUser(test2);

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void getUserWithUsername() throws SQLException {
        UserDao access = new UserDao();
        assertTrue(access.getUserWithUsername("bob3") != null);
        assertTrue(access.getUserWithUsername("joe3") != null);
    }

    @Test
    public void getUserWithPassword() throws SQLException {
        UserDao access = new UserDao();
        assertTrue(access.getUserWithPassword("password1") != null);
        assertTrue(access.getUserWithPassword("password2") != null);
    }

    @Test
    public void getUserWithPersonID() throws SQLException {
        UserDao access = new UserDao();
        assertTrue(access.getUserWithPersonID("1") != null);
        assertTrue(access.getUserWithPersonID("2") != null);
    }

    @Test
    public void deleteUser() throws SQLException {
        UserDao access = new UserDao();
        User test3 = new User("tom3","password3","email3","tom","cat","m","3");
        access.addNewUser(test3);
        assertTrue(access.getUserWithUsername("tom3") != null);
        access.deleteUser(test3);
        assertTrue(access.getUserWithUsername("tom3") == null);
    }

    @Test
    public void clear() throws SQLException {
        UserDao access = new UserDao();
        ArrayList<User> list = new ArrayList<>();

        list = access.getAllUsers();
        assertTrue(list.size() == 2);

        access.clear();

        list = access.getAllUsers();
        assertTrue(list.size() == 0);

    }

}