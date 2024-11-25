package server;

import model.ResponseObject;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import spark.*;
import service.Service;

@WebSocket
public class Server {

    public static Service service = new Service();
    //public webSocketHandler = new WebSocketHandler();

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.webSocket("/ws", Server.class);


        Spark.staticFiles.location("web");

        Spark.post("/user", Server::createUser);
        Spark.post("/session", Server::logIn);
        Spark.delete("/db", Server::clearDatabase);
        Spark.delete("/session", Server::logOut);
        Spark.post("/game", Server::createGame);
        Spark.get("/game", Server::getGames);
        Spark.put("/game", Server::joinGame);
        Spark.awaitInitialization();
        return Spark.port();
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        session.getRemote().sendString(message);
        System.out.println("message echoed");
    }

    public static String createUser(Request req, Response res) throws Exception{
        System.out.println("createUserCalled");
        ResponseObject response = service.registerUser(req.body());
        res.status(response.responseCode);
        return response.responseBody;
    }

    public static String logIn(Request req, Response res) throws Exception{
        System.out.println("login called");
        ResponseObject response = service.loginUser(req.body());
        res.status(response.responseCode);
        return response.responseBody;
    }

    public static String logOut(Request req, Response res) throws Exception{
        System.out.println("logout called");
        ResponseObject response = service.logoutUser(req.headers("authorization"));
        res.status(response.responseCode);
        return response.responseBody;
    }

    public static String clearDatabase(Request req, Response res) throws Exception {
        System.out.println("clearDatabase called");
        ResponseObject response = service.clearDatabase();
        res.status(response.responseCode);
        return response.responseBody;

    }

    public static String createGame(Request req, Response res) throws Exception{
        System.out.println("createGame called");
        ResponseObject response = service.createGame(req.headers("authorization"), req.body());
        res.status(response.responseCode);
        return response.responseBody;
    }

    public static String getGames(Request req, Response res) throws Exception{
        System.out.println("get games called");
        ResponseObject response = service.getGames(req.headers("authorization"));
        res.status(response.responseCode);
        return response.responseBody;
    }

    public static String joinGame(Request req, Response res) throws Exception{
        System.out.println("join game called");
        ResponseObject response = service.joinGame(req.headers("authorization"), req.body());
        res.status(response.responseCode);
        return response.responseBody;
    }

    public void stop() {
        System.out.println("stop server called");
        Spark.stop();
        Spark.awaitStop();
    }
}
