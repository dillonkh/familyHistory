package daoTests;

import org.junit.* ;

import java.util.ArrayList;

import access.AuthTokenDao;
import access.UserDao;
import model.AuthToken;
import model.User;

import static org.junit.Assert.* ;

public class AuthTokenDaoTest {

    @Before
    public void setUp() throws Exception {
        AuthTokenDao access = new AuthTokenDao();
        UserDao userAccess = new UserDao();

        System.out.println("clearing");
        access.clear();

        userAccess.clear();
        System.out.println("adding tom");
        AuthToken test1 = new AuthToken("1","tom");
        access.addNewToken(test1);

        System.out.println("adding joe");
        AuthToken test2 = new AuthToken("2","joe");
        access.addNewToken(test2);

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void getUserWithToken() throws Exception{
        AuthTokenDao access = new AuthTokenDao();
        UserDao userAccess = new UserDao();

        User user = new User("tom","pass","email","tom","bombadill","m","tom");
        userAccess.addNewUser(user);

        System.out.println("user with token 1: " + access.getUserWithToken("1"));
        System.out.println("user just added: " + user);

        assertTrue(access.getUserWithToken("1").getPersonID().equals(user.getPersonID()));

        assertTrue(access.getUserWithToken("3") == null);

    }

    @Test
    public void getTokenWithPersonID() throws Exception{
        AuthTokenDao access = new AuthTokenDao();

        assertTrue(access.getTokenWithPersonID("tom") != null);
        assertTrue(access.getTokenWithPersonID("joe") != null);

        assertTrue(access.getTokenWithPersonID("bob") == null);
    }

    @Test
    public void getToken() throws Exception{
        AuthTokenDao access = new AuthTokenDao();

        assertTrue(access.getToken("1") != null);
        assertTrue(access.getToken("2") != null);

        assertTrue(access.getToken("3") == null);
    }

    @Test
    public void deleteToken() throws Exception{
        AuthTokenDao access = new AuthTokenDao();
        ArrayList<AuthToken> list = new ArrayList<>();

        System.out.println("adding bob");
        AuthToken test = new AuthToken("3","bob");
        access.addNewToken(test);

        list = access.getAllTokens();
        assertTrue(list.size() == 3);

        access.deleteToken("3");
        list = access.getAllTokens();
        assertTrue(list.size() == 2);
    }

    @Test
    public void getAllTokens() throws Exception{
        AuthTokenDao access = new AuthTokenDao();
        ArrayList<AuthToken> list = new ArrayList<>();

        list = access.getAllTokens();
        assertTrue(list.size() == 2);

        access.clear();
        list = access.getAllTokens();
        assertTrue(list.size() == 0);

    }
}