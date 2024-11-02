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
        service.clearDatabase();
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
        service.clearDatabase();
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
        service.clearDatabase();
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
        service.clearDatabase();
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
        service.clearDatabase();
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
        service.clearDatabase();
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
    public void clearDatabase() {
        Service service = new Service();
        service.clearDatabase();
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
    public void positiveLogout() {
        Service service = new Service();
        service.clearDatabase();
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
    public void negativeLogout() {
        Service service = new Service();
        service.clearDatabase();
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
    public void positiveCreateGame() {
        Service service3 = new Service();
        service3.clearDatabase();
        String body = """
                {
                  "username": "username",
                  "password": "password",
                  "email": "email"
                }
                """;
        ResponseObject response = service3.registerUser(body);
        JsonObject jsonObject = JsonParser.parseString(response.responseBody).getAsJsonObject();
        String authToken = jsonObject.get("authToken").getAsString();
        body = """
                {
                  "gameName": "gameName"
                }
                """;
        response = service3.createGame(authToken, body);

        assertEquals(200, response.responseCode);
    }

    @Test
    public void negativeCreateGame() {
        Service service5 = new Service();
        service5.clearDatabase();
        String body = """
                {
                  "username": "username",
                  "password": "password",
                  "email": "email"
                }
                """;
        service5.registerUser(body);
        body = """
                {
                  "gameName": "gameName"
                }
                """;
        ResponseObject response = service5.createGame("bad auth token", body);

        assertEquals(401, response.responseCode);
    }

    @Test
    public void positiveGetGames() {
        Service service1 = new Service();
        service1.clearDatabase();
        String body = """
                {
                  "username": "username",
                  "password": "password",
                  "email": "email"
                }
                """;
        ResponseObject response = service1.registerUser(body);
        JsonObject jsonObject = JsonParser.parseString(response.responseBody).getAsJsonObject();
        String authToken = jsonObject.get("authToken").getAsString();
        body = """
                {
                  "gameName": "gameName"
                }
                """;
        service1.createGame(authToken, body);

        response = service1.getGames(authToken);


        assertEquals(200, response.responseCode);
    }

    @Test
    public void negativeGetGames() {
        Service service2 = new Service();
        service2.clearDatabase();
        String body = """
                {
                  "username": "username",
                  "password": "password",
                  "email": "email"
                }
                """;
        ResponseObject response = service2.registerUser(body);
        JsonObject jsonObject = JsonParser.parseString(response.responseBody).getAsJsonObject();
        String authToken = jsonObject.get("authToken").getAsString();
        body = """
                {
                  "gameName": "gameName"
                }
                """;
        service2.createGame(authToken, body);

        response = service2.getGames("bad auth token");


        assertEquals(401, response.responseCode);
    }

    @Test
    public void positiveJoinGame() {
        Service service4 = new Service();
        service4.clearDatabase();
        String body = """
                {
                  "username": "username",
                  "password": "password",
                  "email": "email"
                }
                """;
        ResponseObject response = service4.registerUser(body);
        JsonObject jsonObject = JsonParser.parseString(response.responseBody).getAsJsonObject();
        String authToken = jsonObject.get("authToken").getAsString();
        body = """
                {
                  "gameName": "gameName"
                }
                """;
        response = service4.createGame(authToken, body);
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
        response = service4.joinGame(authToken, body);


        assertEquals(200, response.responseCode);
    }


    @Test
    public void negativeJoinGame() {
        Service service6 = new Service();
        service6.clearDatabase();
        String body = """
                {
                  "username": "username",
                  "password": "password",
                  "email": "email"
                }
                """;
        ResponseObject response = service6.registerUser(body);
        JsonObject jsonObject = JsonParser.parseString(response.responseBody).getAsJsonObject();
        String authToken = jsonObject.get("authToken").getAsString();
        body = """
                {
                  "gameName": "gameName"
                }
                """;
        response = service6.createGame(authToken, body);
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
        response = service6.joinGame(authToken, body);


        assertEquals(400, response.responseCode);
    }


}
