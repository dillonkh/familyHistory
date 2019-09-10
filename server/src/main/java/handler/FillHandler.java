package handler;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import com.google.gson.Gson;
import com.sun.net.httpserver.*;

import response.DefaultResponse;
import service.FillService;

/*
	The ClaimRouteHandler is the HTTP handler that processes
	incoming HTTP requests that contain the "/routes/claim" URL path.
	
	Notice that ClaimRouteHandler implements the HttpHandler interface,
	which is define by Java.  This interface contains only one method
	named "handle".  When the HttpServer object (declared in the Server class)
	receives a request containing the "/routes/claim" URL path, it calls 
	ClaimRouteHandler.handle() which actually processes the request.
*/
public class FillHandler implements HttpHandler {

    private Integer generations = null;
    private String username = null;

    public Integer getGenerations() {
        return generations;
    }

    public void setGenerations(Integer generations) {
        this.generations = generations;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Handles HTTP requests containing the "/routes/claim" URL path.
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
    @Override
    public void handle(HttpExchange exchange) throws IOException {

        boolean success = false;

        try {
            // Determine the HTTP request type (GET, POST, etc.).
            // Only allow POST requests for this operation.
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {

                // Get the HTTP request headers
                Headers reqHeaders = exchange.getRequestHeaders();

                String path = exchange.getRequestURI().getPath();
                System.out.println(path);
                parsePath(path);

                System.out.println("username: " + getUsername());
                System.out.println("generations: " + getGenerations());

                Gson gson = new Gson();
                DefaultResponse response;
                if (generations == null) {
                    FillService fill = new FillService();
                    response = fill.fill(username);
                }
                else {
                    FillService fill = new FillService();
                    response = fill.fill(username,generations);
                }
                String respData = gson.toJson(response);


                // Start sending the HTTP response to the client, starting with
                // the status code and any defined headers.
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                // Now that the status code and headers have been sent to the client,
                // next we send the JSON data in the HTTP response body.

                // Get the response body output stream.
                OutputStream respBody = exchange.getResponseBody();
                // Write the JSON string to the output stream.
                writeString(respData, respBody);
                // Close the output stream.  This is how Java knows we are done
                // sending data and the response is complete/
                respBody.close();

                success = true;

            }

            if (!success) {
                // The HTTP request was invalid somehow, so we return a "bad request"
                // status code to the client.
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                // We are not sending a response body, so close the response body
                // output stream, indicating that the response is complete.
                exchange.getResponseBody().close();
            }
        }
        catch (IOException e) {
            // Some kind of internal error has occurred inside the server (not the
            // client's fault), so we return an "internal server error" status code
            // to the client.
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            // We are not sending a response body, so close the response body
            // output stream, indicating that the response is complete.
            exchange.getResponseBody().close();

            // Display/log the stack trace
            e.printStackTrace();
        }
    }
    private void parsePath (String path) {
        //System.out.println(path);
        StringBuilder sb = new StringBuilder(path);
        System.out.println("deleting " + sb.charAt(0));
        sb.deleteCharAt(0); // get rid of the first "/"

        int i = 0;
        StringBuilder username = new StringBuilder();
        while (sb.charAt(i) != '/') {
            //System.out.println("burning " + sb.charAt(i));
            i++;
        }
        sb.deleteCharAt(i);
        while (i < sb.length() && sb.charAt(i) != '/') {
            //System.out.println("appending " + sb.charAt(i));
            username.append(sb.charAt(i));
            i++;
//            System.out.println(sb.charAt(i) != '/');
        }

        setUsername(username.toString());
        //System.out.println(username.toString());

        System.out.println(i < sb.length());
        if (i < sb.length()) {
            StringBuilder generations = new StringBuilder();
            sb.deleteCharAt(i);
            while (i < sb.length()) {
                //System.out.println("appending " + sb.charAt(i));
                generations.append(sb.charAt(i));
                i++;
            }
            setGenerations(Integer.parseInt(generations.toString()));
        }

    }

    /*
        The readString method shows how to read a String from an InputStream.
    */
    private void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
}