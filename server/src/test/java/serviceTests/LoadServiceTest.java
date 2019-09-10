package serviceTests;

import org.junit.* ;

import access.EventDao;
import access.PersonDao;
import access.UserDao;
import model.Person;
import request.LoadRequest;
import service.LoadService;

import com.google.gson.Gson;

import java.io.FileReader;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LoadServiceTest {

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void load() throws Exception {

        new PersonDao().addNewPerson(new Person(
                "des",
                "id",
                "bob",
                "blob",
                "M",
                "father",
                "mom",
                "spouse"
        ));

        assertTrue(new PersonDao().getPersonWithPersonID("id") != null);

        Gson gson = new Gson();
        LoadService service = new LoadService();
        LoadRequest req = gson.fromJson(new FileReader("jsonResourses/example.json"),LoadRequest.class);
        service.load(req);

        assertFalse(new PersonDao().getPersonWithPersonID("id") != null);

        assertTrue(new PersonDao().getPersonWithPersonID("Sheila_Parker") != null);
        assertTrue(new UserDao().getUserWithPersonID("Sheila_Parker") != null);
        assertTrue(new EventDao().getEventWithEventID("Sheila_Family_Map") != null);

    }
}