package serviceTests;

import com.google.gson.Gson;

import org.junit.* ;

import java.io.FileReader;

import access.AuthTokenDao;
import access.UserDao;
import model.AuthToken;
import model.User;
import request.LoginRequest;
import service.ClearService;
import service.LoginService;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LoginServiceTest {

    @Before
    public void setUp() throws Exception {

        new ClearService().clear();

        new UserDao().addNewUser(new User(
                "bob",
                "pass",
                "email",
                "bob",
                "last",
                "M",
                "id"
        ));
//        new AuthTokenDao().addNewToken(new AuthToken(
//                "1",
//                "id"
//        ));
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void login() throws Exception {

        System.out.println("token size: " + new AuthTokenDao().getAllTokens().size());

        Gson gson = new Gson();
        LoginService service = new LoginService();
        LoginRequest badUsername = gson.fromJson("{\n" +
                "\t\"userName\":\"userName\",\n" +
                "\t\"password\":\"pass\"\n" +
                "}",
                LoginRequest.class);
        //System.out.println("message: " + service.login(badUsername).getMessage());
        assertTrue(service.login(badUsername).getMessage().equals("Username not valid"));

        LoginRequest badPassword = gson.fromJson("{\n" +
                        "\t\"userName\":\"bob\",\n" +
                        "\t\"password\":\"password\"\n" +
                        "}",
                LoginRequest.class);
        assertTrue(service.login(badPassword).getMessage().equals("password not valid"));

        LoginRequest good = gson.fromJson("{\n" +
                        "\t\"userName\":\"bob\",\n" +
                        "\t\"password\":\"pass\"\n" +
                        "}",
                LoginRequest.class);

        service.login(good);
        //System.out.println("token size: " + new AuthTokenDao().getAllTokens().size());
//        System.out.println("token: " + service.login(good).getAuthToken());
        assertTrue(new AuthTokenDao().getAllTokens().size() == 1);
    }
}