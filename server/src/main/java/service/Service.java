package service;
import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dataaccess.DataAccess;
import dataaccess.SQLDataAccess;
import exception.ResponseException;
import model.GameData;
import model.PublicGameData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import model.ResponseObject;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;
import org.eclipse.jetty.websocket.api.Session;

import java.util.*;
import java.nio.charset.StandardCharsets; //for hashing passwords
import java.security.MessageDigest; //for hashing passwords
import java.time.Instant; //for making authentication tokens


public class Service {

    private final Random rand = new Random();
    Gson serializer = new Gson();

    //Chose what kind of storage type you will use
    //private final MemoryDataAccess memory = new MemoryDataAccess();
    private final DataAccess memory = new SQLDataAccess();
    private HashMap<Integer, ArrayList<Session>> sessions = new HashMap<>();


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

            if(username.isEmpty() || password.isEmpty()) {
                System.out.println("bad credentials sent for login");
                return new ResponseObject(401,"""
                    { "message": "Error: unauthorized" }
                    """);
            }
            if (memory.checkIfUsersExists(username)){
                String oldHash = memory.getPassHash(username);
                try {
                    String token = makeAuthToken(username);
                    System.out.println("new login auth token: " + token);
                    if(BCrypt.checkpw(password, oldHash)){
                        return new ResponseObject(200,"{\"username\":\""+ username + "\", \"authToken\":\""+ token +"\"}");
                    }
                    else{
                        return new ResponseObject(401,"""
                    { "message": "Error: unauthorized" }
                    """);
                    }
                }
                catch(Exception e){
                    System.out.println("error hashing login password");
                    return new ResponseObject(500,"""
                    { "message": "Error: hashing algorithm failed" }
                    """);
                }
            }
            else{
                return new ResponseObject(401,"""
                    { "message": "Error: unauthorized" }
                    """);
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
            //System.out.println("checkpoint1");
            System.out.println(token);
            String userName = memory.getUserFromToken(token);
            System.out.println(userName);
            if (!userName.isEmpty()){
                //System.out.println("checkpoint2");
                //System.out.println("found user matched to token");
                String gameName;
                JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();
                if (!jsonObject.has("gameName")) {
                    //System.out.println("checkpoint3");
                    return new ResponseObject(400,"""
                    { "message": "Error: bad request" }
                    """);
                }
                gameName = jsonObject.get("gameName").getAsString();
                if (!gameName.isEmpty()) {
                    //System.out.println("checkpoint4");
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
                //System.out.println("checkpoint5");
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
                        //System.out.println("error1");
                        return new ResponseObject(400,"""
                        { "message": "Error: bad request" }
                        """);
                    }
                    if (!memory.checkIfGameExists(gameID)) {
                        //System.out.println("error2");
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
                //System.out.println("error3");
                return new ResponseObject(400,"""
                { "message": "Error: bad request" }
                """);
            }
        }
        return new ResponseObject(401,"""
        { "message": "Error: unauthorized" }
        """);
    }

    public ServerMessage connect(UserGameCommand command, Session session){
        String userName = memory.getUserFromToken(command.getAuthToken());
        if (userName.isEmpty()){
            return new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Authentication error. Please try again.");
        }
        if (memory.checkIfGameExists(command.getGameID())){
            GameData game = memory.getGame(command.getGameID());
            System.out.println("username for user: " + userName);
            System.out.println("username on black record: " + game.blackUsername());
            System.out.println("username on white record: " + game.whiteUsername());
            if (game.blackUsername() != null && game.blackUsername().equals(userName) && command.getRequestedRole() == ServerMessage.clientRole.Black){
                ArrayList<Session> peers;
                if (!sessions.containsKey(game.gameID())){
                    peers = new ArrayList<>();
                }
                else{
                    peers = sessions.get(game.gameID());
                }
                for (Session peer : peers){
                    try {
                        peer.getRemote().sendString(new Gson().toJson(new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                                userName + " joined as black.")));
                    }
                    catch (Exception e){
                        System.out.println("error sending join notification to peers");
                    }
                }
                peers.add(session);
                sessions.put(game.gameID(), peers);
                ServerMessage packet = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, "You are joining as black");
                packet.setBoard(game.game().getBoard());
                packet.setRole(ServerMessage.clientRole.Black);
                return packet;
            }
            else if (game.whiteUsername() != null && game.whiteUsername().equals(userName) && command.getRequestedRole() == ServerMessage.clientRole.White){
                ArrayList<Session> peers;
                if (!sessions.containsKey(game.gameID())){
                    peers = new ArrayList<>();
                }
                else{
                    peers = sessions.get(game.gameID());
                }
                for (Session peer : peers){
                    try {
                        peer.getRemote().sendString(new Gson().toJson(new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                                userName + " joined as white.")));
                    }
                    catch (Exception e){
                        System.out.println("error sending join notification to peers");
                    }
                }
                peers.add(session);
                sessions.put(game.gameID(), peers);
                ServerMessage packet = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, "You are joining as white");
                packet.setBoard(game.game().getBoard());
                packet.setRole(ServerMessage.clientRole.White);
                return packet;
            }
            else if (command.getRequestedRole() == ServerMessage.clientRole.Observer){
                ArrayList<Session> peers;
                if (!sessions.containsKey(game.gameID())){
                    peers = new ArrayList<>();
                }
                else{
                    peers = sessions.get(game.gameID());
                }
                for (Session peer : peers){
                    try {
                        peer.getRemote().sendString(new Gson().toJson(new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                                userName + " joined as an observer.")));
                    }
                    catch (Exception e){
                        System.out.println("error sending join notification to peers");
                    }
                }
                peers.add(session);
                sessions.put(game.gameID(), peers);
                ServerMessage packet = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, "You are joining as an observer");
                packet.setBoard(game.game().getBoard());
                packet.setRole(ServerMessage.clientRole.Observer);
                return packet;
            }
            else{
                ServerMessage packet = new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Invalid Request. Try Again.");
                packet.setRole(ServerMessage.clientRole.non);
                return packet;
            }
        }
        else{
            ServerMessage packet = new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Invalid Request. Try Again.");
            packet.setRole(ServerMessage.clientRole.non);
            return packet;
        }
    }

    public ServerMessage makeMove(UserGameCommand command, Session session){
        String userName = memory.getUserFromToken(command.getAuthToken());
        GameData game;
        if (userName.isEmpty()){
            return new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Authentication error. Please try again.");
        }
        if (!memory.checkIfGameExists(command.getGameID())){
            return new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Bad request. Try again.(1)");
        }
        game = memory.getGame(command.getGameID());
        Collection<ChessMove> valid = game.game().validMoves(command.getMove().getStartPosition());
        if (valid.isEmpty()){
            return new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Invalid Move");
        }
        System.out.println("sent move: " + command.getMove());
        System.out.println("valid moves: " + valid.toString());
        if (game.game().turnColor == ChessGame.TeamColor.WHITE && command.getRequestedRole() == ServerMessage.clientRole.White
            || game.game().turnColor == ChessGame.TeamColor.BLACK && command.getRequestedRole() == ServerMessage.clientRole.Black){
            if(valid.contains(command.getMove())){
                try {
                    game.game().makeMove(command.getMove());
                    //System.out.println(game.game().getBoard().toString());
                    memory.saveGameData(game.gameID(), game);
                } catch (InvalidMoveException e) {
                    System.out.println("Error making move: " + e);
                    return new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Bad request. Try again.(2)");
                }
                ServerMessage packet = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
                packet.setBoard(game.game().getBoard());
                packet.setRole(command.getRequestedRole());
                return packet;
            }
            else{
                return new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Invalid Move");
            }
        }
        else{
            return new ServerMessage(ServerMessage.ServerMessageType.ERROR, "It's not your turn!");
        }
    }
}
