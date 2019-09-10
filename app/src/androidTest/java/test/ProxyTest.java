package test;

import org.junit.Test;

import model.Client;
import response.EventsResponse;
import request.LoginRequest;
import response.PeopleResponse;
import response.RegisterLoginResponse;
import request.RegisterRequest;
import proxy.Proxy;

import static org.junit.Assert.*;

public class ProxyTest {

    @Test
    public void getEvents() throws Exception {
        Proxy proxy = new Proxy();
        Client.getInstance().setPortNumber("8080");
        Client.getInstance().setHostAddress("192.168.1.142");

        RegisterRequest request = new RegisterRequest("a","pass","email","bob","builder","m");
        RegisterLoginResponse resp = proxy.register(request);

        proxy = new Proxy();
        EventsResponse response = proxy.getEvents(resp.getAuthToken());

        // positive case
        assertTrue(response.getEvents().size() > 0);
        assertTrue(response.getEvents().get(0).getDescendant().equals("a"));
        assertTrue(response.getMessage() == null);


        response = proxy.getEvents("ImAFraud");

        //bad authToken, failing case
        assertFalse(response != null);
    }

    @Test
    public void getFamily() throws Exception {
        Proxy proxy = new Proxy();
        Client.getInstance().setPortNumber("8080");
        Client.getInstance().setHostAddress("192.168.1.142");

        RegisterRequest request = new RegisterRequest("abcde","pass","email","bob","builder","m");
        RegisterLoginResponse resp = proxy.register(request);

        PeopleResponse response = proxy.getFamily(resp.getAuthToken());

        //positive cases
        assertTrue(response.getPeople().size() > 0);
        assertTrue(response.getMessage() == null);


        response = proxy.getFamily("MyNameIsFakeAuthToken");

        //bad auth token
        assertFalse(response != null);
    }

    @Test
    public void login() throws Exception {
        Proxy proxy = new Proxy();
        Client.getInstance().setPortNumber("8080");
        Client.getInstance().setHostAddress("192.168.1.142");

        RegisterRequest request = new RegisterRequest("bob","pass","email","bob","builder","m");
        RegisterLoginResponse resp = proxy.register(request);

        LoginRequest logRequest = new LoginRequest("bob","pass");

        RegisterLoginResponse logResp = proxy.login(logRequest);

        //passing case
        assertTrue(logResp.getAuthToken() != null);
        assertTrue(logResp.getPersonID() != null);
        assertFalse(logResp.getMessage() != null);


        logRequest = new LoginRequest("fakeUser123","123");

        resp = proxy.login(logRequest);

        //failing case
        assertTrue(resp.getMessage() != null);
        assertTrue(resp.getAuthToken() == null);
        assertFalse(resp.getPersonID() != null);
    }

    @Test
    public void register() throws Exception {
        Proxy proxy = new Proxy();
        Client.getInstance().setPortNumber("8080");
        Client.getInstance().setHostAddress("192.168.1.142");
        RegisterRequest request = new RegisterRequest("bobby","pass","email","bob","builder","m");

        RegisterLoginResponse resp = proxy.register(request);

        // should fail, already registered..
        assertTrue(resp.getAuthToken() != null);
        assertTrue(resp.getMessage() == null);


        request = new RegisterRequest("hill","pass","email","hillary","clinton","f");

        resp = proxy.register(request);

        //should pass
        assertTrue(resp.getMessage() == null);
        assertTrue(resp.getAuthToken() != null);
        assertFalse(resp.getPersonID() == null);
    }

}

