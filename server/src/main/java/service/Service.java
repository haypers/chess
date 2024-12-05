package service;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dataaccess.DataAccess;
import dataaccess.SQLDataAccess;
import exception.ResponseException;
import model.GameData;
import model.PublicGameData;
import model.ResponseObject;
import model.UserData;
import org.eclipse.jetty.websocket.api.Session;
import org.mindrot.jbcrypt.BCrypt;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.*;

public class Service {
    private final Random rand = new Random();
    //Chose what kind of storage type you will use
    //private final MemoryDataAccess memory = new MemoryDataAccess();
    private final DataAccess memory = new SQLDataAccess();
    private final HashMap<Integer, ArrayList<Session>> sessions = new HashMap<>();
    Gson serializer = new Gson();

    public ResponseObject registerUser(String body) {
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
                            """);}
            }
        }
        return new ResponseObject(400, """
                {"message": "Error: bad request"}
                """);}
    public ResponseObject loginUser(String body) {
        String username;
        String password;
        JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();
        if (jsonObject.has("username") && jsonObject.has("password")) {
            username = jsonObject.get("username").getAsString();
            password = jsonObject.get("password").getAsString();
            if (username.isEmpty() || password.isEmpty()) {
                System.out.println("bad credentials sent for login");
                return new ResponseObject(401, """
                        { "message": "Error: unauthorized" }
                        """);}
            if (memory.checkIfUsersExists(username)) {
                String oldHash = memory.getPassHash(username);
                try {
                    String token = makeAuthToken(username);
                    System.out.println("new login auth token: " + token);
                    if (BCrypt.checkpw(password, oldHash)) {
                        return new ResponseObject(200, "{\"username\":\"" + username + "\", \"authToken\":\"" + token + "\"}");
                    } else {
                        return new ResponseObject(401, """
                                { "message": "Error: unauthorized" }
                                """);}
                } catch (Exception e) {
                    System.out.println("error hashing login password");
                    return new ResponseObject(500, """
                            { "message": "Error: hashing algorithm failed" }
                            """);}
            } else {
                return new ResponseObject(401, """
                        { "message": "Error: unauthorized" }
                        """);}
        }
        return new ResponseObject(401, """
                { "message": "Error: unauthorized" }
                """);}
    public String makeAuthToken(String userName) {
        String combinedInput = "";
        if (memory.checkIfUsersExists(userName)) {
            try {
                long currentTimeMillis = Instant.now().toEpochMilli();
                combinedInput = userName + currentTimeMillis;
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] hashBytes = digest.digest(combinedInput.getBytes(StandardCharsets.UTF_8));
                String authToken = Base64.getEncoder().encodeToString(hashBytes);
                while (memory.checkIfHashExists(authToken)) {
                    System.out.println("hash already exists, hashing again.");
                    hashBytes = digest.digest(authToken.getBytes(StandardCharsets.UTF_8));
                    authToken = Base64.getEncoder().encodeToString(hashBytes);
                }
                memory.saveAuthToken(userName, authToken);
                System.out.println(authToken);
                return authToken;
            } catch (Exception e) {
                System.out.println("Danger! Hashing failed, add redundancy");
                memory.saveAuthToken(userName, combinedInput);
                return combinedInput;
            }
        } else {
            return "";
        }
    }
    public ResponseObject clearDatabase() {
        System.out.println("clearing database");
        try {
            if (memory.clearDatabase()) {
                return new ResponseObject(200, "{}");
            } else {
                throw new ResponseException(200, """
                        {"message": "Error: database offline"}
                        """);}
        } catch (ResponseException e) {
            System.out.println(e.statusCode());
            return new ResponseObject(500, """
                    {"message": "Error: database offline"}
                    """);}
    }
    public ResponseObject logoutUser(String token) {
        if (!token.isEmpty()) {
            String userName = memory.getUserFromToken(token);
            if (!userName.isEmpty()) {
                if (memory.logoutUser(token)) {
                    System.out.println("logout successful");
                    return new ResponseObject(200, "{}");
                } else {
                    System.out.println("logout unsuccessful");
                    return new ResponseObject(500, """
                            { "message": "Error: database lost value before modifying it" }
                            """);}
            }
        }
        return new ResponseObject(401, """
                { "message": "Error: unauthorized" }
                """);}
    public ResponseObject createGame(String token, String body) {
        if (!token.isEmpty()) {
            System.out.println(token);
            String userName = memory.getUserFromToken(token);
            System.out.println(userName);
            if (!userName.isEmpty()) {
                String gameName;
                JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();
                if (!jsonObject.has("gameName")) {
                    return new ResponseObject(400, """
                            { "message": "Error: bad request" }
                            """);}
                gameName = jsonObject.get("gameName").getAsString();
                if (!gameName.isEmpty()) {
                    int gameID = rand.nextInt(100000);
                    while (memory.checkIfGameExists(gameID)) {
                        gameID = rand.nextInt(100000);
                    }
                    GameData gameData = new GameData(gameID, null, null, gameName, new ChessGame());
                    if (memory.saveGameData(gameID, gameData)) {
                        return new ResponseObject(200, "{ \"gameID\": " + gameID + " }");
                    } else {
                        return new ResponseObject(500, """
                                { "message": "Error: database offline" }
                                """);}
                }
            }
        }
        return new ResponseObject(401, """
                { "message": "Error: unauthorized" }
                """);}
    public ResponseObject getGames(String token) {
        if (!token.isEmpty()) {
            String userName = memory.getUserFromToken(token);
            if (!userName.isEmpty()) {
                ArrayList<PublicGameData> games;
                games = memory.getAllGames();
                var json = serializer.toJson(games);
                return new ResponseObject(200, "{ \"games\": " + json + "}");}
        }
        return new ResponseObject(401, """
                { "message": "Error: unauthorized" }
                """);}
    public ResponseObject joinGame(String token, String body) {
        if (!token.isEmpty()) {
            String userName = memory.getUserFromToken(token);
            if (!userName.isEmpty()) {
                int gameID;
                String color;
                ChessGame.TeamColor teamColor;
                JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();
                if (jsonObject.has("gameID") && jsonObject.has("playerColor")) {
                    gameID = jsonObject.get("gameID").getAsInt();
                    color = jsonObject.get("playerColor").getAsString();
                    if (color.equals("WHITE")) {
                        teamColor = ChessGame.TeamColor.WHITE;
                    } else if (color.equals("BLACK")) {
                        teamColor = ChessGame.TeamColor.BLACK;
                    } else {
                        return new ResponseObject(400, """
                                { "message": "Error: bad request" }
                                """);}
                    if (!memory.checkIfGameExists(gameID)) {
                        return new ResponseObject(400, """
                                { "message": "Error: bad request" }
                                """);}
                    GameData game = memory.getGame(gameID);
                    System.out.println(game.toString());
                    if (teamColor == ChessGame.TeamColor.WHITE && game.whiteUsername() == null) {
                        GameData newGame = new GameData(game.gameID(), userName, game.blackUsername(), game.gameName(), game.game());
                        memory.saveGameData(gameID, newGame);
                        return new ResponseObject(200, "{}");
                    } else if (teamColor == ChessGame.TeamColor.BLACK && game.blackUsername() == null) {
                        GameData newGame = new GameData(game.gameID(), game.whiteUsername(), userName, game.gameName(), game.game());
                        memory.saveGameData(gameID, newGame);
                        return new ResponseObject(200, "{}");
                    } else {
                        return new ResponseObject(403, """
                                { "message": "Error: already taken" }
                                """);}
                }
                return new ResponseObject(400, """
                        { "message": "Error: bad request" }
                        """);}
        }
        return new ResponseObject(401, """
                { "message": "Error: unauthorized" }
                """);}
    public ServerMessage connect(UserGameCommand command, Session session) {
        System.out.println("in connect" + command.getRequestedRole());
        String userName = memory.getUserFromToken(command.getAuthToken());
        if (userName.isEmpty()) {
            return new ServerMessage(ServerMessage.ServerMessageType.ERROR,
                    "Sever error: Authentication error. Please try again.");
        }
        if (!memory.checkIfGameExists(command.getGameID())){
            ServerMessage packet = new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Invalid Request. Try Again.");
            packet.setRole(ServerMessage.ClientRole.non);
            return packet;
        }
        GameData game = memory.getGame(command.getGameID());
        System.out.println("username for user: " + userName);
        System.out.println("username on black record: " + game.blackUsername());
        System.out.println("username on white record: " + game.whiteUsername());
        if (game.blackUsername() != null && game.blackUsername().equals(userName)) {
            ArrayList<Session> peers;
            if (!sessions.containsKey(game.gameID())) {
                peers = new ArrayList<>();
            } else {
                peers = sessions.get(game.gameID());
            }
            for (Session peer : peers) {
                try {
                    ServerMessage packet = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
                    packet.setMessage(userName + " joined as black.");
                    packet.setRole(ServerMessage.ClientRole.noChange);
                    peer.getRemote().sendString(new Gson().toJson(packet));
                } catch (Exception e) {
                    System.out.println("Sever error: error sending join notification to peers");}
            }
            peers.add(session);
            sessions.put(game.gameID(), peers);
            ServerMessage packet = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
            packet.setBoard(game.game().getBoard());
            packet.setRole(ServerMessage.ClientRole.Black);
            return packet;
        } else if (game.whiteUsername() != null && game.whiteUsername().equals(userName)) {
            ArrayList<Session> peers;
            if (!sessions.containsKey(game.gameID())) {
                peers = new ArrayList<>();
            } else {
                peers = sessions.get(game.gameID());}
            for (Session peer : peers) {
                try {
                    ServerMessage packet = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
                    packet.setMessage(userName + " joined as white.");
                    packet.setRole(ServerMessage.ClientRole.noChange);
                    peer.getRemote().sendString(new Gson().toJson(packet));
                } catch (Exception e) {
                    System.out.println("error sending join notification to peers");
                }
            }
            peers.add(session);
            sessions.put(game.gameID(), peers);
            ServerMessage packet = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
            packet.setBoard(game.game().getBoard());
            packet.setRole(ServerMessage.ClientRole.White);
            return packet;
        } else {
            ArrayList<Session> peers;
            if (!sessions.containsKey(game.gameID())) {
                peers = new ArrayList<>();
            } else {
                peers = sessions.get(game.gameID());}
            for (Session peer : peers) {
                try {
                    ServerMessage packet = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
                    packet.setMessage(userName + " joined as an observer.");
                    packet.setRole(ServerMessage.ClientRole.noChange);
                    peer.getRemote().sendString(new Gson().toJson(packet));
                } catch (Exception e) {
                    System.out.println("Sever error: error sending join notification to peers");
                }
            }
            peers.add(session);
            sessions.put(game.gameID(), peers);
            ServerMessage packet = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
            packet.setBoard(game.game().getBoard());
            packet.setRole(ServerMessage.ClientRole.Observer);
            return packet;
        }
    }
    public ServerMessage makeMove(UserGameCommand command, Session session) {
        String userName = memory.getUserFromToken(command.getAuthToken());
        GameData game;
        if (userName.isEmpty()) {
            return new ServerMessage(ServerMessage.ServerMessageType.ERROR,
                    "Sever error: Authentication error. Please try again.");}
        if (!memory.checkIfGameExists(command.getGameID())) {
            return new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Sever error: Bad request. Try again.(1)");}
        game = memory.getGame(command.getGameID());
        Collection<ChessMove> valid = game.game().validMoves(command.getMove().getStartPosition());
        if (valid.isEmpty()) {
            return new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Sever error: Invalid Move");
        }
        System.out.println("sent move: " + command.getMove());
        System.out.println("valid moves: " + valid);
        ChessPiece piece = game.game().getBoard().getPiece(command.getMove().getStartPosition());
        if (!(piece.getTeamColor() == ChessGame.TeamColor.WHITE && Objects.equals(game.whiteUsername(), userName)) &&
                !(piece.getTeamColor() == ChessGame.TeamColor.BLACK && Objects.equals(game.blackUsername(), userName))) {
            return new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Sever error: That's not your piece");
        }
        if (valid.contains(command.getMove())) {
            String messageExtra = "";
            try {
                game.game().makeMove(command.getMove());
                memory.saveGameData(game.gameID(), game);
                if (game.game().isInCheck(ChessGame.TeamColor.WHITE)) {
                    messageExtra += "'s move put White in Check!";}
                if (game.game().isInCheck(ChessGame.TeamColor.BLACK)) {
                    messageExtra += "'s move put Black in Check!";}
                if (game.game().isInCheckmate(ChessGame.TeamColor.WHITE)) {
                    messageExtra = "'s move put White in CheckMate!";}
                if (game.game().isInCheckmate(ChessGame.TeamColor.BLACK)) {
                    messageExtra = "'s move put Black in checkMate!";}
                ArrayList<Session> peers;
                if (!sessions.containsKey(game.gameID())) {
                    peers = new ArrayList<>();
                } else {
                    peers = sessions.get(game.gameID());}
                for (Session peer : peers) {
                    if (peer == session) {
                        continue;}
                    try {
                        ServerMessage packet = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
                        packet.setBoard(game.game().getBoard());
                        packet.setRole(ServerMessage.ClientRole.noChange);
                        peer.getRemote().sendString(new Gson().toJson(packet));
                        packet = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
                        packet.setMessage(userName + " made move: " + command.getMove().toString());
                        packet.setRole(ServerMessage.ClientRole.noChange);
                        peer.getRemote().sendString(new Gson().toJson(packet));
                    } catch (Exception e) {
                        System.out.println("error sending move notification to peers");}
                }
                if (!messageExtra.equals("")){
                    for (Session peer : peers) {
                        try {
                            ServerMessage packet = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
                            packet.setMessage(userName + messageExtra);
                            packet.setRole(ServerMessage.ClientRole.noChange);
                            peer.getRemote().sendString(new Gson().toJson(packet));
                        } catch (Exception e) {
                            System.out.println("error sending move notification to peers");}
                    }
                }
            } catch (InvalidMoveException e) {
                System.out.println("Error making move: " + e);
                return new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Sever error: Not your turn.");
            }
            ServerMessage packet = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
            packet.setBoard(game.game().getBoard());
            packet.setRole(command.getRequestedRole());
            return packet;
        } else {
            return new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Sever error: Invalid Move");
        }
    }
    public void leave(UserGameCommand command, Session session) {
        String userName = memory.getUserFromToken(command.getAuthToken());
        GameData game;
        if (userName.isEmpty()) {
            return;}
        if (!memory.checkIfGameExists(command.getGameID())) {
            return;}
        game = memory.getGame(command.getGameID());
        ArrayList<Session> peers;
        if (!sessions.containsKey(game.gameID())) {
            peers = new ArrayList<>();
        } else {
            peers = sessions.get(game.gameID());
        }
        String leftMessage;
        if (Objects.equals(game.whiteUsername(), userName)) {
            leftMessage = " as White.";
            GameData modified = new GameData(game.gameID(), null, game.blackUsername(), game.gameName(), game.game());
            memory.saveGameData(game.gameID(), modified);
        } else if (Objects.equals(game.blackUsername(), userName)) {
            leftMessage = " as Black.";
            GameData modified = new GameData(game.gameID(), game.whiteUsername(), null, game.gameName(), game.game());
            memory.saveGameData(game.gameID(), modified);
        } else {
            leftMessage = " as an Observer.";
        }
        for (Session peer : peers) {
            if (peer == session) {
                continue;}
            try {
                ServerMessage packet = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
                packet.setMessage(userName + " left the game" + leftMessage);
                packet.setRole(ServerMessage.ClientRole.noChange);
                peer.getRemote().sendString(new Gson().toJson(packet));
            } catch (Exception e) {
                System.out.println("error sending move notification to peers");
            }
        }
        peers.remove(session);
        sessions.put(game.gameID(), peers);
    }
    public ServerMessage resign(UserGameCommand command, Session session) {
        String userName = memory.getUserFromToken(command.getAuthToken());
        GameData game;
        if (userName.isEmpty()) {
            return new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Sever error: Authentication error. Please try again.");
        }
        if (!memory.checkIfGameExists(command.getGameID())) {
            return new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Sever error: Bad request. Try again.(1)");
        }
        game = memory.getGame(command.getGameID());
        ArrayList<Session> peers;
        if (!sessions.containsKey(game.gameID())) {
            peers = new ArrayList<>();
        } else {
            peers = sessions.get(game.gameID());
        }
        String leftMessage;
        if (game.whiteUsername().equals(userName)) {
            leftMessage = " as White has resigned the game, and lost. BLACK WINS!";
        } else if (game.blackUsername().equals(userName)){
            leftMessage = " as Black has resigned the game, and lost. WHITE WINS!";
        } else{
            leftMessage = " you can't resign as an observer";
            ServerMessage packet = new ServerMessage(ServerMessage.ServerMessageType.ERROR, userName + leftMessage);
            packet.setRole(ServerMessage.ClientRole.noChange);
            return packet;
        }
        for (Session peer : peers) {
            if (peer == session) {
                continue;}
            try {
                ServerMessage packet = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
                packet.setMessage(userName + leftMessage);
                packet.setRole(ServerMessage.ClientRole.non);
                peer.getRemote().sendString(new Gson().toJson(packet));
            } catch (Exception e) {
                System.out.println("error sending move notification to peers");
            }
        }
        sessions.remove(game.gameID());
        memory.removeGame(game.gameID());

        ServerMessage packet = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        packet.setMessage(userName + leftMessage);
        packet.setRole(ServerMessage.ClientRole.non);
        return packet;
    }
}