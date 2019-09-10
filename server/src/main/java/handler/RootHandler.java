package handler;

import java.nio.file.*;
import java.io.*;
import java.net.*;
import java.util.Scanner;

import com.sun.net.httpserver.*;

public class RootHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        System.out.println("In roothandler.handle");
        String filePathStr = "./web";
        URI uri = exchange.getRequestURI();

        if (uri.getPath().equals("/")) {
            //System.out.println("here");
            filePathStr = filePathStr + "/index.html";
        }
        else {
            filePathStr += uri.getPath();
        }

        Path filePath = FileSystems.getDefault().getPath(filePathStr);
        OutputStream respBody = exchange.getResponseBody();

        if (!Files.exists(filePath) || !Files.isReadable(filePath)) {
            System.out.println("Here");
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR,0);
            filePath = FileSystems.getDefault().getPath("./web/HTML/404.html");
            Files.copy(filePath, respBody);
        }
        else {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            Files.copy(filePath,respBody);
        }

        exchange.getResponseBody().close();

        respBody.close();



    }

}
