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



public class Server {

    public static Service s = new Service();

    public int run(int desiredPort) {
        Spark.port(desiredPort);


        Spark.staticFiles.location("web");

        //Spark.post("/user", (req, res) -> "hello post");
        Spark.post("/user", (req, res) -> createUser(req, res));

        Spark.delete("/db", (req, res) -> "{}");
        /*Spark.post("/pet", this::addPet);
        Spark.get("/pet", this::listPets);
        Spark.delete("/pet/:id", this::deletePet);
        Spark.delete("/pet", this::deleteAllPets);
        Spark.exception(ResponseException.class, this::exceptionHandler);*/

        Spark.awaitInitialization();
        return Spark.port();
    }

    private void exceptionHandler(ResponseException ex, Request req, Response res) {
        res.status(ex.StatusCode());
    }


    public static String createUser(Request req, Response res) throws ResponseException{
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
                System.out.println(username + " " + password + " " + email);

                if(!username.isEmpty() && !password.isEmpty() && !email.isEmpty()){
                    System.out.println("good credentials, proceed to make user");
                    UserData user = new UserData(username, password, email);
                    if(s.registerUser(user)) {
                        res.status(200);
                        return """
                            {"username":"", "authToken":""}
                            """;
                    }else{
                        res.status(403);
                        return """
                                {"message": "Error: already taken"}
                                """;
                        }
                }
                else{
                    res.status(400);
                    return """
                    {"message": "Error: bad request"}
                    """;
                }

            } else {
                res.status(400);
                return """
                    {"message": "Error: bad request"}
                    """;
            }
        }
        catch (JsonSyntaxException | IllegalStateException e){
            res.status(400);
            return """
                    {"message": "Error: bad request"}
                    """;
        }
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
