package server;

import model.UserData;
import spark.*;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import service.Service;


public class Server {

    public static Service service = new Service();

    public int run(int desiredPort) {
        Spark.port(desiredPort);


        Spark.staticFiles.location("web");

        //Spark.post("/user", (req, res) -> "hello post");
        Spark.post("/user", (req, res) -> createUser(req, res));
        Spark.post("/session", (req, res) -> logIn(req, res));
        Spark.delete("/db", (req, res) -> clearDatabase(req, res));
        Spark.delete("/session", (req, res) -> logOut(req, res));
        Spark.post("/game", (req, res) -> createGame(req, res));
        Spark.get("/game", (req, res) -> getGames(req, res));
        Spark.put("/game", (req, res) -> joinGame(req, res));
        Spark.awaitInitialization();
        return Spark.port();
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
