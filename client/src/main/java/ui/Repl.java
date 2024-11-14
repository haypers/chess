package ui;

import com.google.gson.JsonObject;
import exception.ResponseException;
import model.PublicGameData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Repl {

    private String serverURL = null;
    private ServerFacade sf = null;
    private String username = null;
    private String authToken = null;
    private boolean isSignedIn = false;
    private Scanner scanner = null;
    private List<GameRecord> games = new ArrayList<>();
    private Integer nextGameIndex = 1;


    public Repl(String serverUrl){
        serverURL = serverUrl;
        sf = new ServerFacade(serverURL);
        scanner = new Scanner(System.in);
    }

    public void preLoginREPL() {
        var result = "";
        System.out.println(SET_TEXT_COLOR_BLUE + this.evalPreLogin("help"));
        while (!result.equals("goodbye!")) {
            System.out.print(RESET_TEXT_COLOR + "♕ > ");
            String line = scanner.nextLine();

            try {
                result = this.evalPreLogin(line);
                System.out.println(SET_TEXT_COLOR_BLUE + result);
                if (isSignedIn){
                    postLoginREPL();
                    System.out.println(SET_TEXT_COLOR_BLUE + this.evalPreLogin("help"));
                }
            } catch (Throwable e) {
                System.out.println("error 1:");
                System.out.print(String.valueOf(e));
            }
        }
    }

    public void postLoginREPL(){
        var result = "";
        System.out.println(SET_TEXT_COLOR_BLUE + this.evalPostLogin("help"));
        while (isSignedIn) {
            System.out.print(RESET_TEXT_COLOR + username + " > ");
            String line = scanner.nextLine();

            try {
                result = this.evalPostLogin(line);
                System.out.println(SET_TEXT_COLOR_BLUE + result);
            } catch (Throwable e) {
                System.out.println("error 2:");
                System.out.print(String.valueOf(e));
            }
        }
    }

    public String evalPreLogin(String input){
        input = input.trim().toLowerCase();
        try {
            var tokens = input.split(" ");
            if(input.isEmpty()){
                return SET_TEXT_COLOR_YELLOW + "no command";
            }
            String cmd = tokens[0];
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "signup", "s" -> registerUser(params);
                case "login", "l" -> loginUser(params);
                case "quit", "q" -> "goodbye!";
                case "help", "h" -> RESET_TEXT_COLOR + """
                        
                        You are not logged in. Use the commands below to continue.
                        
                        help   / h                               -- Print this key
                        signup / s <username> <password> <email> -- Register a new user
                        login  / l <username> <password>         -- Log in to your account
                        quit   / q                               -- End this chess shession
                        
                        """;
                default -> SET_TEXT_COLOR_YELLOW + "Unknown Command";
            };
        } catch (ResponseException e) {
            return e.getMessage();
        }
    }

    public String registerUser(String... params) throws ResponseException {
        if (params.length == 3) {
            JsonObject json = new JsonObject();
            json.addProperty("username", params[0]);
            json.addProperty("password", params[1]);
            json.addProperty("email", params[2]);
            ServerResponseObject reply = sf.registerUser(json);
            authToken = reply.authToken();
            username = reply.username();
            isSignedIn = true;
            return "You are now signed in as: " + username;
        }
        else{
            return SET_TEXT_COLOR_YELLOW + "Expected: signup <username> <password> <email>";
        }
    }

    public String loginUser(String... params) throws ResponseException {
        if (params.length == 2) {
            JsonObject json = new JsonObject();
            json.addProperty("username", params[0]);
            json.addProperty("password", params[1]);
            ServerResponseObject reply = sf.loginUser(json);
            authToken = reply.authToken();
            username = reply.username();
            isSignedIn = true;
            return "You are now signed in as: " + username;
        }
        else{
            return SET_TEXT_COLOR_YELLOW + "Expected: login <username> <password>";
        }
    }

    public String evalPostLogin(String input){
        input = input.trim().toLowerCase();
        try {
            var tokens = input.split(" ");
            if(input.isEmpty()){
                return SET_TEXT_COLOR_YELLOW + "no command";
            }
            String cmd = tokens[0];
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "logout", "l" -> logoutUser(params);
                case "create", "c" -> createGame(params);
                case "list", "s" -> listGames(params);
                //case "play", "p" -> playGame(params);
                //case "observe", "o" -> observeGame(params);
                case "help", "h" -> RESET_TEXT_COLOR + """
                        
                        You are logged in as:""" + username + """
                          Use the commands below to play.
                        
                        help    / h                              -- Print this key
                        logout  / l                              -- Logout of your account
                        create  / c <gameName>                   -- Create a new game
                        list    / s                              -- Show a list of all games
                        play    / p <gameID> [BLACK|WHITE]       -- Join and play a game
                        observe / o <gameID>                     -- Observe a game
                        
                        """;
                default -> SET_TEXT_COLOR_YELLOW + "Unknown Command";
            };
        } catch (ResponseException e) {
            return e.getMessage();
        }
    }

    public String logoutUser(String... params) throws ResponseException {
        if (params.length == 0) {
            sf.logoutUser(authToken);
            isSignedIn = false;
            return "You are now logged out";
        }
        else{
            return SET_TEXT_COLOR_YELLOW + "Expected: logout (no parameters)";
        }
    }
    public String createGame(String... params) throws ResponseException {
        if (params.length == 1) {
            JsonObject json = new JsonObject();
            json.addProperty("gameName", params[0]);
            ServerResponseObject reply = sf.createGame(json, authToken);
            games.add(new GameRecord(nextGameIndex, params[0], reply.gameID, null, null));
            String s = "Created game \"" + params[0] + "\" With Index: " + nextGameIndex;
            nextGameIndex++;
            return s;
        }
        else{
            return SET_TEXT_COLOR_YELLOW + "Expected: create <gameName>";
        }
    }

    public String listGames(String... params) throws ResponseException {
        if (params.length == 0) {
            ServerResponseObject reply = sf.listGames(authToken);
            boolean needsAdded = true;
            for (PublicGameData serverGame : reply.games){
                for (GameRecord localGame : games){
                    if(localGame.getGameID() == serverGame.gameID()){
                        needsAdded = false;
                    }
                }
                if (needsAdded){
                    games.add(new GameRecord(nextGameIndex, serverGame.gameName(), serverGame.gameID(), serverGame.whiteUsername(), serverGame.blackUsername()));
                    nextGameIndex++;
                }
            }
            StringBuilder s = new StringBuilder();
            for (GameRecord game : games) {
                s.append(game.toString() + "\n");
            }
            return s.toString();
        }
        else{
            return SET_TEXT_COLOR_YELLOW + "Expected: create <gameName>";
        }
    }

}
