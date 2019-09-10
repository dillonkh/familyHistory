package serviceTests;

import org.junit.* ;

import access.PersonDao;
import model.Person;
import request.RegisterRequest;
import response.RegisterLoginResponse;
import service.ClearService;
import service.PersonService;
import service.RegisterService;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PersonServiceTest {

    @Before
    public void setUp() throws Exception {

        new ClearService().clear();
        new PersonDao().addNewPerson(new Person(
                "some_guy",
                "id",
                "joe",
                "blo",
                "m",
                "father",
                "mother",
                null
        ));
        new PersonDao().addNewPerson(new Person(
                "some_guy",
                "1",
                "joe",
                "shmo",
                "m",
                "father",
                "mother",
                null
        ));
        new PersonDao().addNewPerson(new Person(
                "some_guy",
                "2",
                "joe",
                "know",
                "m",
                "father",
                "mother",
                null
        ));
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getPerson() throws Exception {

        RegisterRequest req = new RegisterRequest("user","pass","email","name","last","m");
        RegisterLoginResponse response = new RegisterService().register(req);

        String token = response.getAuthToken();
        PersonService service = new PersonService();

        //get peron we have access to
        String ancestor = new PersonDao().getPeopleWithDescendant("user").get(0).getPersonID();
        assertTrue(service.getPerson(ancestor,token).getDescendent().equals("user"));

        //get one with bad token
        assertTrue(service.getPerson(ancestor,"fake_token").getMessage().equals("Not authorized to access that info"));

        //get real person we dont have access to
        assertTrue(service.getPerson("2",token).getMessage().equals("Not authorized to access that info"));
    }
}