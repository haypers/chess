package service;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;
import server.ResponseObject;

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
    }
    @Test
    public void negativeRegister(){
        Service service = new Service();
        String body = """
                {
                  "username": "username",
                  "password": "password",
                  "email": "email"
                }
                """;
        service.registerUser(body);
        //register user twice
        ResponseObject response = service.registerUser(body);
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
                  "password": "badPassword"
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
        service.registerUser(body);
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
        service.registerUser(body);
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
        response = service.logoutUser(authToken);

        assertEquals(200, response.responseCode);
    }

    @Test
    public void NegativeLogout() {
        Service service = new Service();
        String body = """
                {
                  "username": "username",
                  "password": "password",
                  "email": "email"
                }
                """;
        service.registerUser(body);
        ResponseObject response = service.logoutUser("bad token");

        assertEquals(401, response.responseCode);
    }


    @Test
    public void PositiveCreateGame() {
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
        body = """
                {
                  "gameName": "gameName"
                }
                """;
        response = service.createGame(authToken, body);

        assertEquals(200, response.responseCode);
    }

    @Test
    public void NegativeCreateGame() {
        Service service = new Service();
        String body = """
                {
                  "username": "username",
                  "password": "password",
                  "email": "email"
                }
                """;
        service.registerUser(body);
        body = """
                {
                  "gameName": "gameName"
                }
                """;
        ResponseObject response = service.createGame("bad auth token", body);

        assertEquals(401, response.responseCode);
    }

    @Test
    public void PositiveGetGames() {
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
        body = """
                {
                  "gameName": "gameName"
                }
                """;
        service.createGame(authToken, body);

        response = service.getGames(authToken);


        assertEquals(200, response.responseCode);
    }

    @Test
    public void NegativeGetGames() {
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
        body = """
                {
                  "gameName": "gameName"
                }
                """;
        service.createGame(authToken, body);

        response = service.getGames("bad auth token");


        assertEquals(401, response.responseCode);
    }

    @Test
    public void PositiveJoinGame() {
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
        body = """
                {
                  "gameName": "gameName"
                }
                """;
        response = service.createGame(authToken, body);
        jsonObject = JsonParser.parseString(response.responseBody).getAsJsonObject();
        String gameID = jsonObject.get("gameID").getAsString();
        System.out.println(gameID);
        body = """
                {
                             "playerColor": "WHITE",
                                 "gameID":
                """
                + gameID + """
                         }
                """;
        response = service.joinGame(authToken, body);


        assertEquals(200, response.responseCode);
    }


    @Test
    public void NegativeJoinGame() {
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
        body = """
                {
                  "gameName": "gameName"
                }
                """;
        response = service.createGame(authToken, body);
        jsonObject = JsonParser.parseString(response.responseBody).getAsJsonObject();
        String gameID = jsonObject.get("gameID").getAsString();
        System.out.println(gameID);
        //bad game ID
        body = """
                {
                             "playerColor": "WHITE",
                                 "gameID":
                """
                + (gameID + 1) + """
                         }
                """;
        response = service.joinGame(authToken, body);


        assertEquals(400, response.responseCode);
    }


}
