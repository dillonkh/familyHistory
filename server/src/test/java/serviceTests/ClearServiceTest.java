package serviceTests;

import org.junit.* ;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import access.AuthTokenDao;
import access.EventDao;
import access.PersonDao;
import access.UserDao;
import model.AuthToken;
import model.Event;
import model.Person;
import model.User;
import service.ClearService;

public class ClearServiceTest {

    @Before
    public void setUp() throws Exception {
        EventDao access = new EventDao();
        access.clear();

        Event event1 = new Event(
                "1",
                "someone",
                "1",
                "12",
                "12",
                "USA",
                "Provo",
                "Death",
                "2000"
        );
        Event event2 = new Event(
                "2",
                "someone",
                "1",
                "12",
                "12",
                "USA",
                "Provo",
                "marriage",
                "1996"
        );
        access.addNewEvent(event1);
        access.addNewEvent(event2);

        User user1 = new User("someone",
                "1",
                "email",
                "paco",
                "dude",
                "f",
                "1"
        );
        UserDao userAccess = new UserDao();
        userAccess.clear();
        userAccess.addNewUser(user1);

        Person person1 = new Person(
                "someone",
                "1",
                "paco",
                "dude",
                "f",
                "daddy",
                "mother",
                null
        );
        PersonDao personAccess = new PersonDao();
        personAccess.clear();
        personAccess.addNewPerson(person1);

        AuthToken token = new AuthToken("1","1");
        AuthTokenDao tokenAccess = new AuthTokenDao();
        tokenAccess.clear();
        tokenAccess.addNewToken(token);

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void clear() throws Exception {
        AuthTokenDao t = new AuthTokenDao();
        PersonDao p = new PersonDao();
        UserDao u = new UserDao();
        EventDao e = new EventDao();

        ArrayList<AuthToken> tokens = t.getAllTokens();
        assertTrue(tokens.size() == 1);

        ArrayList<Person> persons = p.getAllPeople();
        assertTrue(persons.size() == 1);

        ArrayList<User> users = u.getAllUsers();
        assertTrue(users.size() == 1);

        ArrayList<Event> events = e.getAllEvents();
        assertTrue(events.size() == 2);

        ClearService clear = new ClearService();
        clear.clear();

        tokens = t.getAllTokens();
        assertTrue(tokens.size() == 0);

        persons = p.getAllPeople();
        assertTrue(persons.size() == 0);

        users = u.getAllUsers();
        assertTrue(users.size() == 0);

        events = e.getAllEvents();
        assertTrue(events.size() == 0);


    }
}