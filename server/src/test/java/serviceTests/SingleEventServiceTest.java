package serviceTests;

import org.junit.* ;

import access.EventDao;
import access.PersonDao;
import model.Event;
import model.Person;
import request.RegisterRequest;
import response.RegisterLoginResponse;
import service.ClearService;
import service.RegisterService;
import service.SingleEventService;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class SingleEventServiceTest {

    @Before
    public void setUp() throws Exception {
        new ClearService().clear();

        new EventDao().addNewEvent(new Event(
                "1",
                "des",
                "id",
                "100",
                "100",
                "usa",
                "provo",
                "birth",
                "1990"
        ));
        new EventDao().addNewEvent(new Event(
                "2",
                "des",
                "id",
                "100",
                "100",
                "usa",
                "provo",
                "bap",
                "1998"
        ));
        new EventDao().addNewEvent(new Event(
                "3",
                "des",
                "id",
                "100",
                "100",
                "usa",
                "provo",
                "death",
                "2018"
        ));
        new PersonDao().addNewPerson(new Person(
                "des",
                "id",
                "gandalf",
                "grey",
                "M",
                null,
                null,
                null
        ));
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getEvent() throws Exception {
        EventDao access = new EventDao();

        assertFalse(access.getAllEvents().size() == 0);

        RegisterService regService = new RegisterService();
        RegisterLoginResponse resp = regService.register(new RegisterRequest(
                "user",
                "pass",
                "mail",
                "tom",
                "bombadill",
                "M")
        );
        assertTrue(access.getAllEvents().size() == 125);

        String token = resp.getAuthToken();
        assertTrue(token != null);

        SingleEventService service = new SingleEventService();

        //get one we have access to
        String eventID = new EventDao().getEventsWithDescendant("user").get(0).getEventID();
        assertTrue(service.getEvent(eventID,token).getDescendant().equals("user"));

        // try to get one with bad token
        assertTrue(service.getEvent(eventID,"fake_token").getMessage().equals("Not authorized to access that data"));

        // try to get data that doesnt belong to me
        System.out.println("message: " + service.getEvent("3",token).getMessage());
        assertTrue(service.getEvent("3",token).getMessage().equals("Not authorized to access that data"));



    }
}