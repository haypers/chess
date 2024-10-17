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
        // Register your endpoints and handle exceptions here.

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }


    public static String createUser(Request req, Response res) {
        String body = req.body();

        System.out.println(body);

        res.status(201);
        return """
               { "username":"","password":"","email":""}
               """;
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
