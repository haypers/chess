package server;

import exception.ResponseException;
import model.UserData;
import spark.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import service.Service;
import exception.ResponseException;

import java.util.Objects;


public class Server {

    public static Service s = new Service();

    public int run(int desiredPort) {
        Spark.port(desiredPort);


        Spark.staticFiles.location("web");

        //Spark.post("/user", (req, res) -> "hello post");
        Spark.post("/user", (req, res) -> createUser(req, res));

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

        String username = "";
        String password = "";
        String email = "";
        String body = req.body();

        try {
            JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();

            if (jsonObject.has("username") && jsonObject.has("password") && jsonObject.has("email")) {

                username = jsonObject.get("username").getAsString();
                password = jsonObject.get("password").getAsString();
                email = jsonObject.get("email").getAsString();

                if(!username.isEmpty() && !password.isEmpty() && !email.isEmpty()){

                    System.out.println("Good credentials sent for registration");
                    UserData user = new UserData(username, password, email);

                    //Send to service for registration. True if success, false if username taken
                    if(s.registerUser(user)) {
                        String newAuthToken = s.makeAuthToken(username);
                        if (Objects.equals(newAuthToken, "")){
                            res.status(400);
                            return """
                                {"message": "Error: auth token error"}
                                """;
                        }
                        res.status(200);
                        return "{\"username\":\""+ username + "\", \"authToken\":\""+ newAuthToken +"\"}";

                    }else{
                        res.status(403);
                        return """
                            {"message": "Error: already taken"}
                            """;
                    }
                }
            }
            res.status(400);
            return """
                {"message": "Error: bad request"}
                """;

        }
        catch (Exception  e){
            res.status(400);
            return """
                    {"message": "Error: bad request"}
                    """;
        }
    }

    public static String clearDatabase(Request req, Response res) throws Exception {
        try {
            if (s.clearDatabase()) {
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
