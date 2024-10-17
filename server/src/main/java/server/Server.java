package server;

import spark.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;


public class Server {

    //private Service s = new Service();

    public int run(int desiredPort) {
        Spark.port(desiredPort);


        Spark.staticFiles.location("web");

        //Spark.post("/user", (req, res) -> "hello post");
        Spark.post("/user", (req, res) -> createUser(req, res));

        Spark.delete("/db", (req, res) -> "{}");
        // Register your endpoints and handle exceptions here.

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }


    public static String createUser(Request req, Response res) {
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

            } else {
                System.out.println("invalid request");
            }
            res.status(201);
            return """
               { "username":"","authToken":""}
               """;
        }
        catch (JsonSyntaxException e){
            res.status(400);
            return """
                    {"message": "Error: bad request"}
                    """
                    ;
        }
        catch (IllegalStateException e){
            res.status(400);
            return """
                    {"message":"illegal state exception"}
                    """
                    ;
        }
        //System.out.println(body);


    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
