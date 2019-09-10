package serviceTests;

import org.junit.* ;

import access.AuthTokenDao;
import access.EventDao;
import access.PersonDao;
import access.UserDao;
import request.RegisterRequest;
import response.RegisterLoginResponse;
import service.ClearService;
import service.RegisterService;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RegisterServiceTest {

    @Before
    public void setUp() throws Exception {
        new ClearService().clear();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void register() throws Exception {
        assertTrue(new PersonDao().getAllPeople().size() == 0);
        assertTrue(new UserDao().getAllUsers().size() == 0);
        assertTrue(new EventDao().getAllEvents().size() == 0);
        assertTrue(new AuthTokenDao().getAllTokens().size() == 0);

        RegisterService service = new RegisterService();
        RegisterRequest req = new RegisterRequest(
                "user",
                "pass",
                "email",
                "first",
                "last",
                "M"
        );
        RegisterLoginResponse resp = service.register(req);
        assertTrue(resp.getAuthToken() != null);

        resp = service.register(req);
        assertFalse(resp.getAuthToken() != null);

        resp = service.register(new RegisterRequest(
                "new",
                "newPass",
                "mail",
                "name",
                "last",
                "M")
        );
        assertTrue(resp.getAuthToken() != null);



    }
}