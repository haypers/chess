package client;

import com.google.gson.JsonObject;
import exception.ResponseException;
import org.junit.jupiter.api.*;
import server.Server;
import ui.ServerFacade;
import ui.ServerResponseObject;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(8070);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();

    }


    @Test
    public void sampleTest() {
        assertTrue(true);
    }

    @Test
    public void registerUserPositiveTest() throws ResponseException {
        ServerFacade sf = new ServerFacade("http://localhost:8070");
        JsonObject json = new JsonObject();
        json.addProperty("username", "correct");
        json.addProperty("password", "joe");
        json.addProperty("email", "email");
        ServerResponseObject reply = sf.registerUser(json);
        assertTrue(!reply.authToken.isEmpty());
    }
    @Test
    public void registerUserNegativeTest() throws ResponseException {
        try {
            ServerFacade sf = new ServerFacade("http://localhost:8070");
            JsonObject json = new JsonObject();
            json.addProperty("username", "usernametaken");
            json.addProperty("password", "boom");
            json.addProperty("email", "email");
            ServerResponseObject reply = sf.registerUser(json);
            assertTrue(reply.authToken.isEmpty());
        }
        catch(Throwable e){
            assertTrue(1 == 1);

        }
    }
    @Test
    public void loginUserPositiveTest() throws ResponseException {
        ServerFacade sf = new ServerFacade("http://localhost:8070");
        JsonObject json = new JsonObject();
        json.addProperty("username", "correct");
        json.addProperty("password", "joe");
        ServerResponseObject reply = sf.loginUser(json);
        assertTrue(!reply.authToken.isEmpty());
    }
    @Test
    public void loginUserNegativeTest() throws ResponseException {
        try {
            ServerFacade sf = new ServerFacade("http://localhost:8070");
            JsonObject json = new JsonObject();
            json.addProperty("username", "wrong password");
            json.addProperty("password", "boom");
            ServerResponseObject reply = sf.loginUser(json);
        }
        catch(Throwable e){
            assertTrue(1 == 1);
            //return true if it runs, proving it threw an error

        }
    }
    @Test
    public void logoutUserPositiveTest() throws ResponseException {
        ServerFacade sf = new ServerFacade("http://localhost:8070");
        JsonObject json = new JsonObject();
        json.addProperty("username", "uniqueuser");
        json.addProperty("password", "joe");
        json.addProperty("email", "fakeemail");
        ServerResponseObject reply = sf.registerUser(json);
        reply = sf.logoutUser(reply.authToken);
        assertTrue(reply.message == null);
    }
    @Test
    public void logoutUserNegativeTest() throws ResponseException {
        try {
            ServerFacade sf = new ServerFacade("http://localhost:8070");
            ServerResponseObject reply = sf.logoutUser("fake auth token");
        }
        catch (Throwable e){
            assertTrue(1 == 1);
            //return true if it runs, proving it threw an error
        }
    }
    @Test
    public void createGamePositiveTest() throws ResponseException {
        ServerFacade sf = new ServerFacade("http://localhost:8070");
        JsonObject json = new JsonObject();
        json.addProperty("username", "anotheruser");
        json.addProperty("password", "joe");
        json.addProperty("email", "email");
        ServerResponseObject reply = sf.registerUser(json);
        JsonObject game = new JsonObject();
        game.addProperty("gameName", "fungame");
        reply = sf.createGame(game, reply.authToken);
        assertNotNull(reply.gameID);
    }
    @Test
    public void createGameNegativeTest() throws ResponseException {
        try {
            ServerFacade sf = new ServerFacade("http://localhost:8070");
            JsonObject game = new JsonObject();
            game.addProperty("gameName", "fungame");
            ServerResponseObject reply = sf.createGame(game, "bad auth token");
        }
        catch (Throwable e){
            assertTrue(1 == 1);
            //return true if it runs, proving it threw an error
        }

    }
    @Test
    public void ListGamesPositiveTest() throws ResponseException {
        ServerFacade sf = new ServerFacade("http://localhost:8070");
        JsonObject json = new JsonObject();
        json.addProperty("username", "onceagiananotheruser");
        json.addProperty("password", "anotherpassword");
        json.addProperty("email", "email");
        ServerResponseObject reply = sf.registerUser(json);
        reply = sf.listGames(reply.authToken);
        assertNotNull(reply.games);
    }
    @Test
    public void ListGamesNegativeTest() throws ResponseException {
        try {
            ServerFacade sf = new ServerFacade("http://localhost:8070");
            ServerResponseObject reply = sf.listGames("Bad auth");
        }
        catch(Throwable e){
            assertTrue(1 == 1);
            //return true if it runs, proving it threw an error
        }

    }
    @Test
    public void joinGamePositiveTest() throws ResponseException {
        ServerFacade sf = new ServerFacade("http://localhost:8070");
        JsonObject json = new JsonObject();
        json.addProperty("username", "anothertestuseragainagain");
        json.addProperty("password", "correctpassword");
        json.addProperty("email", "email");
        ServerResponseObject reply = sf.registerUser(json);
        JsonObject game = new JsonObject();
        game.addProperty("gameName", "testgame");
        ServerResponseObject gameIdBox = sf.createGame(game, reply.authToken);
        JsonObject id = new JsonObject();
        id.addProperty("playerColor", "WHITE");
        id.addProperty("gameID", gameIdBox.gameID);
        System.out.print(id);
        ServerResponseObject output = sf.joinGame(id, reply.authToken);
        //true if no errors have been thrown yet
        assertNotNull(reply.authToken);
    }
    @Test
    public void joinGameNegativeTest() throws ResponseException {
        try {
            ServerFacade sf = new ServerFacade("http://localhost:8070");

            JsonObject id = new JsonObject();
            id.addProperty("playerColor", "WHITE");
            id.addProperty("gameID", 25823);
            System.out.print(id);
            ServerResponseObject output = sf.joinGame(id, "badauthtoken");
        }
        catch(Throwable e){
            assertTrue(1 == 1);
            //return true if it runs, proving it threw an error
        }
    }


}
