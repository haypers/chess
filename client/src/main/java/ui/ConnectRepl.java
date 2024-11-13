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

    String serverURL = null;
    ServerFacade sf = null;


    public ConnectRepl(String serverUrl){
        serverURL = serverUrl;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            System.out.print(RESET_TEXT_COLOR + "♕ > ");
            String line = scanner.nextLine();

            try {
                result = this.eval(line);
                System.out.println("Output: " + SET_TEXT_COLOR_BLUE + result);
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
                case "signup" -> registerUser(params);
                //case "rescue" -> rescuePet(params);
                //case "list" -> listPets();
                //case "signout" -> signOut();
                //case "adopt" -> adoptPet(params);
                //case "adoptall" -> adoptAllPets();
                case "test" -> "nice!";
                default -> "Unknown Command";
            };
        } catch (ResponseException e) {
            System.out.println("error in eval:");
            System.out.println(e.toString());
            return e.getMessage();
        }
    }

    public String registerUser(String... params) throws ResponseException {
        if (params.length >= 1) {
            String visitorName = String.join("-", params); //need to change, keep params apart.
            sf = new ServerFacade(serverURL);
            JsonObject json = new JsonObject();
            json.addProperty("username", params[0]);
            json.addProperty("password", params[1]);
            json.addProperty("email", params[2]);

            System.out.println("internal output:");
            System.out.println(sf.registerUser(json));


            return "success";
        }
        System.out.println("error in registerUser:");
        throw new ResponseException(400, "Expected: <yourname>");
    }


}
