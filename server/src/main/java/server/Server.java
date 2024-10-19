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

        /*Spark.post("/pet", this::addPet);
        Spark.get("/pet", this::listPets);
        Spark.delete("/pet/:id", this::deletePet);
        Spark.delete("/pet", this::deleteAllPets);
        Spark.exception(ResponseException.class, this::exceptionHandler);*/

        Spark.awaitInitialization();
        return Spark.port();
    }

    public static String createUser(Request req, Response res) throws Exception{
        ResponseObject response = service.registerUser(req.body());
        res.status(response.responseCode);
        return response.responseBody;
    }

    public static String logIn(Request req, Response res) throws Exception{
        ResponseObject response = service.loginUser(req.body());
        res.status(response.responseCode);
        return response.responseBody;
    }

    public static String clearDatabase(Request req, Response res) throws Exception {
        try {
            if (service.clearDatabase()) {
                res.status(200);
                return "{}";
            }
            res.status(500);
            return """
                    {"message": "Error: database offline"}
                    """;
        }
        catch(Exception e){
            res.status(500);
            return """
                    {"message": "Error: database offline"}
                    """;
        }
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
