package service;
import chess.ChessGame;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dataaccess.MemoryDataAccess;
import exception.ResponseException;
import model.GameData;
import model.PublicGameData;
import model.UserData;
import server.ResponseObject;

import java.util.ArrayList;
import java.util.Random; //for making game IDs
import java.nio.charset.StandardCharsets; //for hashing passwords
import java.security.MessageDigest; //for hashing passwords
import java.time.Instant; //for making authentication tokens
import java.util.Base64; //for hashing passwords and tokens
import java.util.Objects;

public class Service {

    private final Random rand = new Random();
    Gson serializer = new Gson();
    private final MemoryDataAccess memory = new MemoryDataAccess();

    public ResponseObject registerUser(String body){
        String username;
        String password;
        String email;
        JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();

        if (jsonObject.has("username") && jsonObject.has("password") && jsonObject.has("email")) {
            username = jsonObject.get("username").getAsString();
            password = jsonObject.get("password").getAsString();
            email = jsonObject.get("email").getAsString();

            if (!username.isEmpty() && !password.isEmpty() && !email.isEmpty()) {
                System.out.println("Good credentials sent for registration");

                if (!memory.checkIfUsersExists(username)) {
                    UserData user = new UserData(username, password, email);
                    memory.addUser(user);
                    String newAuthToken = makeAuthToken(username);
                    return new ResponseObject(200, "{\"username\":\"" + username + "\", \"authToken\":\"" + newAuthToken + "\"}");
                } else {
                    return new ResponseObject(403, """
                            {"message": "Error: already taken"}
                            """);
                }
            }
        }
        return new ResponseObject(400, """
                {"message": "Error: bad request"}
                """);
    }

    public ResponseObject loginUser(String body){
        String username;
        String password;
        JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();

        if (jsonObject.has("username") && jsonObject.has("password")) {
            username = jsonObject.get("username").getAsString();
            password = jsonObject.get("password").getAsString();

            if(!username.isEmpty() && !password.isEmpty()){
                System.out.println("Good credentials sent for login");

                if (memory.checkIfUsersExists(username)){
                    String oldHash = memory.getPassHash(username);
                    String currentHash;
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
        if(memory.checkIfUsersExists(userName)){
            try {
                long currentTimeMillis = Instant.now().toEpochMilli();
                combinedInput = userName + currentTimeMillis;
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] hashBytes = digest.digest(combinedInput.getBytes(StandardCharsets.UTF_8));
                String authToken = Base64.getEncoder().encodeToString(hashBytes);

                /*if this hash already exists than the same user requested several hashes in the same millisecond,
                 just hash it again. It just needs to be unique, not deterministic.
                 */
                while(memory.checkIfHashExists(authToken)){
                    System.out.println("hash already exists, hashing again.");
                    hashBytes = digest.digest(authToken.getBytes(StandardCharsets.UTF_8));
                    authToken = Base64.getEncoder().encodeToString(hashBytes);
                }

                memory.saveAuthToken(userName, authToken);
                System.out.println(authToken);
                return authToken;
            }
            catch(Exception e){
                System.out.println("Danger! Hashing failed, add redundancy");
                memory.saveAuthToken(userName, combinedInput);
                return combinedInput;
            }
        }
        else{
            return "";
        }
    }

    public ResponseObject clearDatabase(){
        System.out.println("clearing database");
        try {
            if (memory.clearDatabase()) {
                return new ResponseObject(200,"{}");
            }
            else{
                throw new ResponseException(200, """
                    {"message": "Error: database offline"}
                    """);
                /*return new ResponseObject(500,"""
                    {"message": "Error: database offline"}
                    """);*/
            }
        }
        catch(ResponseException e){
            System.out.println(e.statusCode());
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
            String userName = memory.getUserFromToken(token);
            if (!userName.isEmpty()){
                //System.out.println("found user matched to token");
                String gameName;
                JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();
                if (!jsonObject.has("gameName")) {
                    return new ResponseObject(400,"""
                    { "message": "Error: bad request" }
                    """);
                }
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
        }
        return new ResponseObject(401,"""
        { "message": "Error: unauthorized" }
        """);
    }

    public ResponseObject getGames(String token){
        if(!token.isEmpty()){
            String userName = memory.getUserFromToken(token);
            if (!userName.isEmpty()){
                ArrayList<PublicGameData> games;
                games = memory.getAllGames();
                var json = serializer.toJson(games);
                return new ResponseObject(200, "{ \"games\": " + json + "}");

                /* Use this code to detect for offline database in future phase.
                return new ResponseObject(500,"""
                { "message": "Error: database offline" }
                """);*/
            }
        }
        return new ResponseObject(401,"""
        { "message": "Error: unauthorized" }
        """);
    }

    public ResponseObject joinGame(String token, String body){
        if(!token.isEmpty()){
            String userName = memory.getUserFromToken(token);
            if (!userName.isEmpty()){
                //System.out.println("found user matched to token");
                int gameID;
                String color;
                ChessGame.TeamColor teamColor;
                JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();
                if (jsonObject.has("gameID") && jsonObject.has("playerColor")) {
                    gameID = jsonObject.get("gameID").getAsInt();
                    color = jsonObject.get("playerColor").getAsString();
                    if(color.equals("WHITE")){
                        teamColor = ChessGame.TeamColor.WHITE;
                    } else if (color.equals("BLACK")){
                        teamColor = ChessGame.TeamColor.BLACK;
                    }
                    else{
                        return new ResponseObject(400,"""
                        { "message": "Error: bad request" }
                        """);
                    }
                    if (!memory.checkIfGameExists(gameID)) {
                        return new ResponseObject(400,"""
                        { "message": "Error: bad request" }
                        """);
                    }
                    GameData game = memory.getGame(gameID);
                    if(teamColor == ChessGame.TeamColor.WHITE && game.whiteUsername() == null){
                        GameData newGame = new GameData(game.gameID(), userName, game.blackUsername(), game.gameName(), game.game());
                        memory.saveGameData(gameID, newGame);
                        return new ResponseObject(200,"{}");
                    } else if(teamColor == ChessGame.TeamColor.BLACK && game.blackUsername() == null){
                        GameData newGame = new GameData(game.gameID(), game.whiteUsername(), userName, game.gameName(), game.game());
                        memory.saveGameData(gameID, newGame);
                        return new ResponseObject(200,"{}");
                    } else{
                        return new ResponseObject(403,"""
                            { "message": "Error: already taken" }
                            """);
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
