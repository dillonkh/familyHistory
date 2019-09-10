package serviceTests;

import org.junit.* ;

import access.AuthTokenDao;
import access.EventDao;
import access.PersonDao;
import access.UserDao;
import model.AuthToken;
import model.Event;
import model.Person;
import model.User;
import response.EventsResponse;
import service.AllEventsService;

import static org.junit.Assert.assertTrue;

public class AllEventsServiceTest {

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
    public void getAllEvents() {
        AllEventsService service = new AllEventsService();

        // not valid token
        EventsResponse response = service.getAllEvents("");
        assertTrue(response.getMessage().equals("Not a valid auth token"));

        // valid token
        response = service.getAllEvents("1");
        System.out.println("events: \n" + response.getEvents());
        assertTrue(response.getEvents().size() == 2);



    }
}