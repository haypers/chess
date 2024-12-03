package server;

import com.google.gson.Gson;
import model.ResponseObject;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import spark.*;
import service.Service;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

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
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        System.out.println("Got command: " + command.getCommandType());
        if (command.getCommandType() == UserGameCommand.CommandType.CONNECT){
            System.out.println("connect");
            ServerMessage reply = service.connect(command, session);
            session.getRemote().sendString(new Gson().toJson(reply));
        }
        else if (command.getCommandType() == UserGameCommand.CommandType.LEAVE){
            System.out.println("leave");
        }
        else if (command.getCommandType() == UserGameCommand.CommandType.MAKE_MOVE){
            System.out.println("make move");
            ServerMessage reply = service.makeMove(command, session);
            session.getRemote().sendString(new Gson().toJson(reply));
        }
        else if (command.getCommandType() == UserGameCommand.CommandType.RESIGN){
            System.out.println("Resign");
        }
        System.out.println("sent response object");
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
