package handler;

import java.io.*;
import java.net.*;

import com.google.gson.Gson;
import com.sun.net.httpserver.*;

import response.EventResponse;
import response.EventsResponse;
import service.AllEventsService;
import service.SingleEventService;
import service.ValidateTokenService;

/*
	The ListGamesHandler is the HTTP handler that processes
	incoming HTTP requests that contain the "/games/list" URL path.

	Notice that ListGamesHandler implements the HttpHandler interface,
	which is define by Java.  This interface contains only one method
	named "handle".  When the HttpServer object (declared in the Server class)
	receives a request containing the "/games/list" URL path, it calls
	ListGamesHandler.handle() which actually processes the request.
*/
public class EventHandler implements HttpHandler {

    // Handles HTTP requests containing the "/games/list" URL path.
    // The "exchange" parameter is an HttpExchange object, which is
    // defined by Java.
    // In this context, an "exchange" is an HTTP request/response pair
    // (i.e., the client and server exchange a request and response).
    // The HttpExchange object gives the handler access to all of the
    // details of the HTTP request (Request type [GET or POST],
    // request headers, request body, etc.).
    // The HttpExchange object also gives the handler the ability
    // to construct an HTTP response and send it back to the client
    // (Status code, headers, response body, etc.).
    public void allEvents (HttpExchange exchange) throws IOException {
        boolean success = false;
        System.out.println("in get all events");
        if (exchange.getRequestMethod().toLowerCase().equals("get")) {

            // Get the HTTP request headers
            Headers reqHeaders = exchange.getRequestHeaders();
            // Check to see if an "Authorization" header is present
            //if (reqHeaders.containsKey("Authorization")) {

            // Extract the auth token from the "Authorization" header
            String authToken = reqHeaders.getFirst("Authorization");
            //String authToken = "12";
            //System.out.println(authToken);
            ValidateTokenService service = new ValidateTokenService(authToken);

            Gson gson = new Gson();
            String respData = null;
            //System.out.println(service.isValidToken() == true);
            if (service.isValidToken() == true) {

                AllEventsService events = new AllEventsService();
                EventsResponse response = events.getAllEvents(authToken);
                //EventsResponse response = new EventsResponse("that works");
                respData = gson.toJson(response);
                System.out.println(respData);
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            }
            else {
                EventsResponse response = new EventsResponse("Not valid auth token");
                respData = gson.toJson(response);

                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST,0);
            }


            OutputStream respBody = exchange.getResponseBody();
            // Write the JSON string to the output stream.
            writeString(respData, respBody);
            System.out.println(respBody);
            // Close the output stream.  This is how Java knows we are done
            // sending data and the response is complete/
            respBody.close();

            success = true;
            //}
        }

        if (!success) {
            // The HTTP request was invalid somehow, so we return a "bad request"
            // status code to the client.
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            // Since the client request was invalid, they will not receive the
            // list of games, so we close the response body output stream,
            // indicating that the response is complete.
            exchange.getResponseBody().close();
        }
    }

    private String getEventID(String uri) {
        StringBuilder tempHolder = new StringBuilder(uri);
        StringBuilder username = new StringBuilder();

        tempHolder.deleteCharAt(0); // get rid of first "/"

        int i = 0;
        while (tempHolder.charAt(i) != '/') {
            i++;
        }
        tempHolder.deleteCharAt(i);
        while (i < tempHolder.length()) {
            username.append(tempHolder.charAt(i));
            i++;
        }

        return username.toString();
    }


    public void singleEvent (HttpExchange exchange,String uri) throws IOException {
        boolean success = false;
        if (exchange.getRequestMethod().toLowerCase().equals("get")) {

            // Get the HTTP request headers
            Headers reqHeaders = exchange.getRequestHeaders();
            // Check to see if an "Authorization" header is present
            //if (reqHeaders.containsKey("Authorization")) {

            // Extract the auth token from the "Authorization" header
            String authToken = reqHeaders.getFirst("Authorization");
            //String authToken = "12";
            //System.out.println(authToken);
            ValidateTokenService service = new ValidateTokenService(authToken);


            String eventID = getEventID(uri);

            Gson gson = new Gson();
            String respData = null;
            //System.out.println(service.isValidToken() == true);
            if (service.isValidToken() == true) {

                SingleEventService event = new SingleEventService();
                EventResponse response = event.getEvent(eventID,authToken);
                //EventsResponse response = new EventsResponse("that works");
                respData = gson.toJson(response);
                System.out.println(respData);
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            }
            else {
                EventsResponse response = new EventsResponse("Not valid auth token");
                respData = gson.toJson(response);

                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST,0);
            }


            OutputStream respBody = exchange.getResponseBody();
            // Write the JSON string to the output stream.
            writeString(respData, respBody);
            // Close the output stream.  This is how Java knows we are done
            // sending data and the response is complete/
            respBody.close();

            success = true;
            //}
        }

        if (!success) {
            // The HTTP request was invalid somehow, so we return a "bad request"
            // status code to the client.
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            // Since the client request was invalid, they will not receive the
            // list of games, so we close the response body output stream,
            // indicating that the response is complete.
            exchange.getResponseBody().close();
        }
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {


        boolean success = false;
        System.out.println("in event handler");
        final int ALL_EVENTS_SIZE = 6;

        try {

            URI uri = exchange.getRequestURI();
            int size = uri.toString().length();

            //how do i find out if it is events or event???

            if (size == ALL_EVENTS_SIZE) {
                allEvents(exchange);
            }
            else if (size > ALL_EVENTS_SIZE) {
                singleEvent(exchange,uri.toString());
            }
            else {
                // The HTTP request was invalid somehow, so we return a "bad request"
                // status code to the client.
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                // Since the client request was invalid, they will not receive the
                // list of games, so we close the response body output stream,
                // indicating that the response is complete.
                exchange.getResponseBody().close();
            }
        }
        catch (IOException e) {
            // Some kind of internal error has occurred inside the server (not the
            // client's fault), so we return an "internal server error" status code
            // to the client.
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            // Since the server is unable to complete the request, the client will
            // not receive the list of games, so we close the response body output stream,
            // indicating that the response is complete.
            exchange.getResponseBody().close();

            // Display/log the stack trace
            e.printStackTrace();
        }
    }

    /*
        The writeString method shows how to write a String to an OutputStream.
    */
    private void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
}
