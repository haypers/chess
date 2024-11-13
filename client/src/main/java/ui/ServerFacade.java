package ui;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import exception.ResponseException;

import java.io.*;
import java.net.*;


public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public String registerUser(JsonObject jsonString) throws ResponseException {
        System.out.println("made it to registerUser facade");
        var path = "/user";
        ServerResponseObject reply = this.makeRequest("POST", path, jsonString, ServerResponseObject.class);
        return reply.authToken();
    }

    /*public void deletePet(int id) throws ResponseException {
        var path = String.format("/pet/%s", id);
        this.makeRequest("DELETE", path, null, null);
    }

    public void deleteAllPets() throws ResponseException {
        var path = "/pet";
        this.makeRequest("DELETE", path, null, null);
    }

    public Pet[] listPets() throws ResponseException {
        var path = "/pet";
        record listPetResponse(Pet[] pet) {
        }
        var response = this.makeRequest("GET", path, null, listPetResponse.class);
        return response.pet();
    }*/

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            var response = readBody(http, responseClass);//this is where the error is.
            System.out.println(response);
            return response;
        } catch (Exception ex) {
            System.out.println("error in makeRequest:");
            System.out.println(ex.toString());
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
            throw new ResponseException(status, "failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {//this is where the error is thrown.
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);//this is where the error is caused. This line is trying to deserialize the message into an object.
                }
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
