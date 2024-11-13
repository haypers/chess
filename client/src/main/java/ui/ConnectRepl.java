package ui;

import com.google.gson.JsonObject;
import exception.ResponseException;

import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class ConnectRepl {

//5: help, register(http), login(http), quit
//6:help create game(http), list games(http), join game (http then websocket), join observer (http then websocket)
    //help, redraw, leave (remove yourself as a player or observer, someone takes your place), make move(web socket), resign (end game, lose), highlight.

    private String serverURL = null;
    private ServerFacade sf = null;
    private String username = null;
    private String authToken = null;


    public ConnectRepl(String serverUrl){
        serverURL = serverUrl;
        sf = new ServerFacade(serverURL);
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            System.out.print(RESET_TEXT_COLOR + "♕ > ");
            String line = scanner.nextLine();

            try {
                result = this.eval(line);
                System.out.println(SET_TEXT_COLOR_BLUE + result);
            } catch (Throwable e) {
                System.out.println("error in run:");
                System.out.print(String.valueOf(e));
            }
        }
    }

    public String eval(String input){
        input = input.trim().toLowerCase();
        try {
            var tokens = input.split(" ");
            if(input.isEmpty()){
                return "no command";
            }
            String cmd = tokens[0];
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "signup", "s" -> registerUser(params);
                case "login", "l" -> loginUser(params);
                case "help", "h" -> RESET_TEXT_COLOR + """
                        
                        You are not logged in. Use the commands below to continue.
                        
                        help   / h                               -- Print this key
                        signup / s <username> <password> <email> -- Register a new user
                        login  / l <username> <password>         -- Log in to your account
                        quit   / q                               -- End this chess shession
                        
                        """;
                default -> "Unknown Command";
            };
        } catch (ResponseException e) {
            System.out.println("error in eval:");
            System.out.println(e.toString());
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


            return "You are now signed in as: " + username;
        }
        else{
            return "Expected: signup <username> <password> <email>";
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
            return "You are now signed in as: " + username;
        }
        else{
            return "Expected: login <username> <password> <email>";
        }
    }


}
