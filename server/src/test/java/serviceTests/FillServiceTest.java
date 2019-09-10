package serviceTests;

import org.junit.* ;
import static org.junit.Assert.assertTrue;

import access.EventDao;
import access.PersonDao;
import access.UserDao;
import model.User;
import service.ClearService;
import service.FillService;

public class FillServiceTest {

    @Before
    public void setUp() throws Exception {
        ClearService clear = new ClearService();
        clear.clear();

        User user = new User(
                "bob",
                "1",
                "email",
                "bob",
                "man",
                "M",
                "1"
                );
        new UserDao().addNewUser(user);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void fill() throws Exception {

        assertTrue(new PersonDao().getAllPeople().size() == 0);
        assertTrue(new EventDao().getAllEvents().size() == 0);

        FillService fill = new FillService();
        fill.fill("bob");

        System.out.println(new EventDao().getAllEvents().size());
        assertTrue(new PersonDao().getAllPeople().size() == 31);
        assertTrue(new EventDao().getAllEvents().size() == 122);

    }

    @Test
    public void fill1() throws Exception {

        ClearService clear = new ClearService();

        assertTrue(new PersonDao().getAllPeople().size() == 0);
        assertTrue(new EventDao().getAllEvents().size() == 0);

        FillService fill = new FillService();
        fill.fill("bob",4);

        assertTrue(new PersonDao().getAllPeople().size() == 31);
        assertTrue(new EventDao().getAllEvents().size() == 122);

        //clear.clear();

        //fill = new FillService();
        fill.fill("bob",3);

        assertTrue(new PersonDao().getAllPeople().size() == 15);
        assertTrue(new EventDao().getAllEvents().size() == 58);

        //clear.clear();

        //fill = new FillService();
        fill.fill("bob",2);

        assertTrue(new PersonDao().getAllPeople().size() == 7);
        assertTrue(new EventDao().getAllEvents().size() == 26);

        //clear.clear();

        //fill = new FillService();
        fill.fill("bob",1);

        assertTrue(new PersonDao().getAllPeople().size() == 3);
        assertTrue(new EventDao().getAllEvents().size() == 10);

        //clear.clear();

        //fill = new FillService();
        fill.fill("bob",6);

        assertTrue(new PersonDao().getAllPeople().size() == 127);
        assertTrue(new EventDao().getAllEvents().size() == 506);
    }
}