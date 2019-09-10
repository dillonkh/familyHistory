package proxy;

import java.io.*;
import java.net.*;
//import java.util.*;
//import com.sun.net.httpserver.*;
//import static java.net.HttpURLConnection.HTTP_OK;
import com.google.gson.Gson;

import model.Client;
import response.EventsResponse;
import request.LoginRequest;
import response.PeopleResponse;
import model.Person;
import response.RegisterLoginResponse;
import request.RegisterRequest;

public class Proxy {
    private String portNumber;
    private String hostAddress;
    private String responseMessage;


    public static void main(String[] args) {
        RegisterRequest req = new RegisterRequest("dill","pass","email","dillon","harris","m");
        Proxy proxy = new Proxy();
        try {
            RegisterLoginResponse resp = proxy.register(req);
            proxy.getPerson(resp.getPersonID(),resp.getAuthToken());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public EventsResponse getEvents(String authToken) throws Exception {
        System.out.println("PROXY: getEvents:");

        String url = "http://" + Client.getInstance().getHostAddress() + ":"
                + Client.getInstance().getPortNumber() + "/event";

        System.out.println(url);
        HttpURLConnection connection =
                (HttpURLConnection) new URL(url).openConnection();

        connection.setRequestMethod("GET");
        connection.setDoOutput(false);
        System.out.println("token: " + authToken);
        connection.setRequestProperty("Authorization", authToken);
        connection.connect();

        // add checks for failure status codes
        int status = connection.getResponseCode();
        System.out.print("status: " + status);
        if (status != 200) {
            return null;
        }

        Reader in = new InputStreamReader(connection.getInputStream());
        Gson gson = new Gson();
        EventsResponse response = gson.fromJson(in, EventsResponse.class);

        System.out.println("PROXY: getEvents:");
        System.out.println(response);

        return response;

    }
    public PeopleResponse getFamily(String authToken) throws Exception {
        System.out.println("PROXY: getFamily:");

        String url = "http://" + Client.getInstance().getHostAddress() + ":"
                + Client.getInstance().getPortNumber() + "/personID";
        System.out.println(url);
        HttpURLConnection connection =
                (HttpURLConnection) new URL(url).openConnection();

        connection.setRequestMethod("GET");
        connection.setDoOutput(false);
        System.out.println("token: " + authToken);
        connection.setRequestProperty("Authorization", authToken);
        connection.connect();

        // add checks for failure status codes
        int status = connection.getResponseCode();
        System.out.print("status: " + status);
        if (status != 200) {
            return null;
        }

        Reader in = new InputStreamReader(connection.getInputStream());
        Gson gson = new Gson();
        PeopleResponse response = gson.fromJson(in, PeopleResponse.class);

        System.out.println("PROXY: getFamily:");
        System.out.println(response);

        return response;
    }

    public Person getPerson(String personID, String authToken) throws Exception {
        System.out.println("PROXY: getPerson:");

        String url = "http://" + Client.getInstance().getHostAddress() + ":"
                + Client.getInstance().getPortNumber() + "/person/"+personID;

        System.out.println(url);
        HttpURLConnection connection =
                (HttpURLConnection) new URL(url).openConnection();

        connection.setRequestMethod("GET");
        connection.setDoOutput(false);
        System.out.println("token: " + authToken);
        connection.setRequestProperty("Authorization", authToken);
        connection.connect();

        // add checks for failure status codes
        int status = connection.getResponseCode();
        System.out.print("status: " + status);
        if (status != 200) {
            return null;
        }

        Reader in = new InputStreamReader(connection.getInputStream());
        Gson gson = new Gson();
        Person person = gson.fromJson(in, Person.class);

        System.out.println("PROXY: getPerson:");
        System.out.println(person);

        return person;
    }
    public RegisterLoginResponse login (LoginRequest request) throws Exception {
        System.out.println("PROXY: login:");

        String url = "http://" + Client.getInstance().getHostAddress() + ":"
                + Client.getInstance().getPortNumber() + "/user/login";

        System.out.println(url);
        HttpURLConnection connection =
                (HttpURLConnection) new URL(url).openConnection();

        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.connect();

        Writer out = new OutputStreamWriter(connection.getOutputStream());
        Gson gson = new Gson();
        gson.toJson(request, out);
        out.close();

        System.out.println("request body: " + gson.toJson(request));
        System.out.println();

        // need to wait for response or operation will fail
        // add checks for failure status codes
        int status = connection.getResponseCode();
        if (status != 200) {
            return new RegisterLoginResponse(null,null,null);
        }

        Reader in = new InputStreamReader(connection.getInputStream());
        Gson gson1 = new Gson();
        RegisterLoginResponse resp = gson1.fromJson(in, RegisterLoginResponse.class);

        System.out.println("PROXY: login");
        System.out.println(resp);
        System.out.println();

        return resp;
    }

    public RegisterLoginResponse register(RegisterRequest request) throws Exception {

        System.out.println("PROXY: register:");
        System.out.println(request);

        String url = "http://" + Client.getInstance().getHostAddress() + ":"
                + Client.getInstance().getPortNumber() + "/user/register";
        System.out.println(url);
        HttpURLConnection connection =
                (HttpURLConnection) new URL(url).openConnection();

        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.connect();

        Writer out = new OutputStreamWriter(connection.getOutputStream());
        Gson gson = new Gson();
        gson.toJson(request, out);
        out.close();

        System.out.println("request body: " + gson.toJson(request));
        System.out.println();

        // need to wait for response or operation will fail
        // add checks for failure status codes
        int status = connection.getResponseCode();
        if (status != 200) {
            return new RegisterLoginResponse(null,null,null);
        }

        Reader in = new InputStreamReader(connection.getInputStream());
        Gson gson1 = new Gson();
        RegisterLoginResponse resp = gson1.fromJson(in, RegisterLoginResponse.class);

        System.out.println("PROXY: register:");
        System.out.println(resp);
        System.out.println();

        return resp;

    }

}
