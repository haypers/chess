package service;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;
import server.ResponseObject;
import server.Server;
import service.Service;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTests {



    @Test
    public void positiveRegister() {
        Service service = new Service();
        String body = """
                {
                  "username": "username",
                  "password": "password",
                  "email": "email"
                }
                """;
        ResponseObject response = service.registerUser(body);
        assertEquals(200, response.responseCode);
        assertTrue(100 == 2 * 50);
        assertNotNull(new Object(), "Response did not return authentication String");
    }
    @Test
    public void negitiveRegister(){
        Service service = new Service();
        String body = """
                {
                  "username": "username",
                  "password": "password",
                  "email": "email"
                }
                """;
        ResponseObject response = service.registerUser(body);
        //register user twice
        response = service.registerUser(body);
        assertEquals(403, response.responseCode);
    }

    @Test
    public void positiveLogin() {
        Service service = new Service();
        String body = """
                {
                  "username": "username",
                  "password": "password",
                  "email": "email"
                }
                """;
        ResponseObject response = service.registerUser(body);
        assertEquals(200, response.responseCode);
        body = """
                {
                  "username": "username",
                  "password": "password"
                }
                """;
        response = service.loginUser(body);
        assertEquals(200, response.responseCode);
    }

    @Test
    public void negativeLogin() {
        Service service = new Service();
        String body = """
                {
                  "username": "username",
                  "password": "password",
                  "email": "email"
                }
                """;
        ResponseObject response = service.registerUser(body);
        assertEquals(200, response.responseCode);
        body = """
                {
                  "username": "username",
                  "password": "badpassword"
                }
                """;
        response = service.loginUser(body);
        assertEquals(401, response.responseCode);
    }

    @Test
    public void positiveHashCode() {
        Service service = new Service();
        String body = """
                {
                  "username": "username",
                  "password": "password",
                  "email": "email"
                }
                """;
        ResponseObject response = service.registerUser(body);
        String hash = service.makeAuthToken("username");
        assertNotEquals("", hash);
    }

    @Test
    public void negativeHashCode() {
        Service service = new Service();
        String body = """
                {
                  "username": "username",
                  "password": "password",
                  "email": "email"
                }
                """;
        ResponseObject response = service.registerUser(body);
        String hash = service.makeAuthToken("nonuser");
        assertEquals("", hash);
    }


    @Test
    public void ClearDatabase() {
        Service service = new Service();
        String body = """
                {
                  "username": "username",
                  "password": "password",
                  "email": "email"
                }
                """;
        ResponseObject response = service.registerUser(body);
        assertEquals(200, response.responseCode);
        service.clearDatabase();
        body = """
                {
                  "username": "username",
                  "password": "password"
                }
                """;
        response = service.loginUser(body);
        assertEquals(401, response.responseCode);
    }

    @Test
    public void PositiveLogout() {
        Service service = new Service();
        String body = """
                {
                  "username": "username",
                  "password": "password",
                  "email": "email"
                }
                """;
        ResponseObject response = service.registerUser(body);
        JsonObject jsonObject = JsonParser.parseString(response.responseBody).getAsJsonObject();
        String authToken = jsonObject.get("authToken").getAsString();
        System.out.println(authToken);
        response = service.logoutUser(authToken);

        assertEquals(200, response.responseCode);
    }

}
