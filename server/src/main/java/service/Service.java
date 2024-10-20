package service;
import chess.ChessGame;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dataaccess.MemoryDataAccess;
import model.GameData;
import model.UserData;
import server.ResponseObject;
import java.util.Random;
import spark.Response;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Base64;
import java.util.Objects;

public class Service {

    private Random rand = new Random();

    private MemoryDataAccess memory = new MemoryDataAccess();

    public ResponseObject registerUser(String body){
        String username = "";
        String password = "";
        String email = "";
        JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();

        if (jsonObject.has("username") && jsonObject.has("password") && jsonObject.has("email")) {
            username = jsonObject.get("username").getAsString();
            password = jsonObject.get("password").getAsString();
            email = jsonObject.get("email").getAsString();

            if(!username.isEmpty() && !password.isEmpty() && !email.isEmpty()){
                System.out.println("Good credentials sent for registration");

                if(!memory.checkIfUsersExists(username)) {
                    UserData user = new UserData(username, password, email);
                    memory.addUser(user);
                    String newAuthToken = makeAuthToken(username);
                    return new ResponseObject(200, "{\"username\":\"" + username + "\", \"authToken\":\"" + newAuthToken + "\"}" );
                }
                else{
                    return new ResponseObject(403,"""
                        {"message": "Error: already taken"}
                        """);
                }
            }
        }
        return new ResponseObject(400,"""
                        {"message": "Error: bad request"}
                        """);
    }

    public ResponseObject loginUser(String body){
        String username = "";
        String password = "";
        JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();

        if (jsonObject.has("username") && jsonObject.has("password")) {
            username = jsonObject.get("username").getAsString();
            password = jsonObject.get("password").getAsString();

            if(!username.isEmpty() && !password.isEmpty()){
                System.out.println("Good credentials sent for login");

                if (memory.checkIfUsersExists(username)){
                    String oldHash = memory.getPassHash(username);
                    String currentHash = "";
                    try {
                        MessageDigest digest = MessageDigest.getInstance("SHA-256");
                        byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
                        currentHash = Base64.getEncoder().encodeToString(hashBytes);
                    }
                    catch(Exception e){
                        System.out.println("error hashing login password");
                        return new ResponseObject(500,"""
                        { "message": "Error: hashing algorithm failed" }
                        """);
                    }
                    if (Objects.equals(oldHash, currentHash)){
                        String newAuthToken = makeAuthToken(username);
                        return new ResponseObject(200,"{\"username\":\""+ username + "\", \"authToken\":\""+ newAuthToken +"\"}");
                    }
                    else{
                        return new ResponseObject(401,"""
                        { "message": "Error: unauthorized" }
                        """);
                    }
                }
                else{
                    return new ResponseObject(401,"""
                        { "message": "Error: unauthorized" }
                        """);
                }
            }
        }
        return new ResponseObject(401,"""
            { "message": "Error: unauthorized" }
            """);
    }

    public String makeAuthToken(String userName){
        String combinedInput = "";
        try {
            long currentTimeMillis = Instant.now().toEpochMilli();
            combinedInput = userName + currentTimeMillis;
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(combinedInput.getBytes(StandardCharsets.UTF_8));
            String authToken = Base64.getEncoder().encodeToString(hashBytes);
            memory.saveAuthToken(userName, authToken);
            return authToken;
        }
        catch(Exception e){
            System.out.println("Danger! Hashing failed, add redundancy");
            memory.saveAuthToken(userName, combinedInput);
            return combinedInput;
        }
    }

    public ResponseObject clearDatabase(){
        System.out.println("clearing database");
        try {
            if (memory.clearDatabase()) {
                return new ResponseObject(200,"{}");
            }
            else{
                return new ResponseObject(500,"""
                    {"message": "Error: database offline"}
                    """);
            }
        }
        catch(Exception e){
            return new ResponseObject(500,"""
                    {"message": "Error: database offline"}
                    """);
        }
    }

    public ResponseObject logoutUser(String token){
        if(!token.isEmpty()){
            //System.out.println("Good authToken sent for logout");
            String userName = memory.getUserFromToken(token);
            if (!userName.isEmpty()){
                //System.out.println("found user matched to token");
                if(memory.logoutUser(token)){
                    System.out.println("logout successful");
                    return new ResponseObject(200,"{}");
                }
                else{
                    System.out.println("logout unsuccessful");
                    return new ResponseObject(500,"""
                    { "message": "Error: database lost value before modifying it" }
                    """);
                }
            }
        }
        return new ResponseObject(401,"""
        { "message": "Error: unauthorized" }
        """);
    }

    public ResponseObject createGame(String token, String body){
        if(!token.isEmpty()){
            //System.out.println("Good authToken sent for logout");
            String userName = memory.getUserFromToken(token);
            if (!userName.isEmpty()){
                //System.out.println("found user matched to token");
                String gameName = "";
                JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();
                if (jsonObject.has("gameName")) {
                    gameName = jsonObject.get("gameName").getAsString();
                    if (!gameName.isEmpty()) {
                        int gameID = rand.nextInt(100000);
                        while(memory.checkIfGameExists(gameID)){
                            gameID = rand.nextInt(100000);
                        }
                        GameData gameData = new GameData(gameID, null, null, gameName, new ChessGame());
                        if(memory.saveGameData(gameID, gameData)){
                            return new ResponseObject(200, "{ \"gameID\": " + gameID + " }");
                        }
                        else{
                            return new ResponseObject(500,"""
                            { "message": "Error: database offline" }
                            """);
                        }
                    }
                }
                return new ResponseObject(400,"""
                { "message": "Error: bad request" }
                """);
            }
        }
        return new ResponseObject(401,"""
        { "message": "Error: unauthorized" }
        """);
    }


}
