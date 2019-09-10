package serviceTests;

import org.junit.* ;

import access.AuthTokenDao;
import access.PersonDao;
import model.Person;
import request.LoginRequest;
import request.RegisterRequest;
import response.PeopleResponse;
import response.RegisterLoginResponse;
import service.ClearService;
import service.PeopleService;
import service.RegisterService;

import static org.junit.Assert.assertTrue;

public class PeopleServiceTest {

    @Before
    public void setUp() throws Exception {
        new ClearService().clear();
//        new PersonDao().addNewPerson(new Person(
//                "des",
//                "id",
//                "joe",
//                "blo",
//                "m",
//                "father",
//                "mother",
//                null
//        ));
//        new PersonDao().addNewPerson(new Person(
//                "user",
//                "1",
//                "joe",
//                "shmo",
//                "m",
//                "father",
//                "mother",
//                null
//        ));
//        new PersonDao().addNewPerson(new Person(
//                "user",
//                "2",
//                "joe",
//                "know",
//                "m",
//                "father",
//                "mother",
//                null
//        ));
//        RegisterRequest req = new RegisterRequest("user","pass","email","name","last","m");
//        RegisterLoginResponse response = new RegisterService().register(req);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getPeople() throws Exception {

        RegisterRequest req = new RegisterRequest("user","pass","email","name","last","m");
        RegisterLoginResponse response = new RegisterService().register(req);

        String token = response.getAuthToken();

        PeopleService service = new PeopleService();
        PeopleResponse resonse = service.getPeople(token);
        assertTrue(resonse.getPeople().size() == 31);

        resonse = service.getPeople("fake_token");
        assertTrue(resonse.getMessage().equals("Not a valid auth token"));

    }
}