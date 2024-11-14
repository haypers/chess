package ui;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import exception.ResponseException;

import java.io.*;
import java.net.*;

import static ui.EscapeSequences.SET_TEXT_COLOR_RED;


public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public ServerResponseObject registerUser(JsonObject jsonString) throws ResponseException {
        //System.out.println("made it to registerUser facade");
        var path = "/user";
        return this.makeRequest("POST", path, jsonString, ServerResponseObject.class, null);
    }

    public ServerResponseObject loginUser(JsonObject jsonString) throws ResponseException {
        //System.out.println("made it to registerUser facade");
        var path = "/session";
        return this.makeRequest("POST", path, jsonString, ServerResponseObject.class, null);
    }

    public ServerResponseObject logoutUser(String authToken) throws ResponseException {
        //System.out.println("made it to registerUser facade");
        var path = "/session";
        return this.makeRequest("DELETE", path, null, ServerResponseObject.class, authToken);
    }

    public ServerResponseObject createGame(JsonObject jsonString, String authToken) throws ResponseException {
        //System.out.println("made it to registerUser facade");
        var path = "/game";
        return this.makeRequest("POST", path, jsonString, ServerResponseObject.class, authToken);
    }

    public ServerResponseObject listGames(String authToken) throws ResponseException {
        var path = "/game";
        return this.makeRequest("GET", path, null, ServerResponseObject.class, authToken);
    }

    public ServerResponseObject joinGame(JsonObject jsonString, String authToken) throws ResponseException {
        var path = "/game";
        return this.makeRequest("PUT", path, jsonString, ServerResponseObject.class, authToken);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, String authToken) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);
            if (authToken != null && !authToken.isEmpty()) {
                http.setRequestProperty("authorization", authToken);
            }
            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            var response = readBody(http, responseClass);
            return response;
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }


    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            if (status == 401){
                throw new ResponseException(status, SET_TEXT_COLOR_RED + "Bad credentials provided. Please try again.");
            }
            else if (status == 400){
                throw new ResponseException(status, SET_TEXT_COLOR_RED + "Bad request. Check your command syntax and try again.");
            }
            else if (status == 403){
                throw new ResponseException(status, SET_TEXT_COLOR_RED + "Parameter already taken.");
            }
            else if (status == 500){
                throw new ResponseException(status, SET_TEXT_COLOR_RED + "500 Error" + status);
            }
            else {
                throw new ResponseException(status, SET_TEXT_COLOR_RED + "Unknown Error" + status);
            }
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
