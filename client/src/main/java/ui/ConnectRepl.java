package ui;

import exception.ResponseException;

import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

import static java.awt.Color.BLUE;
import static ui.EscapeSequences.*;

public class ConnectRepl {

//5: help, register(http), login(http), quit
//6:help create game(http), list games(http), join game (http then websocket), join observer (http then websocket)
        //help, redraw, leave (remove yourself as a player or observer, someone takes your place), make move(web socket), resign (end game, lose), highlight.
    public void run() {
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            System.out.print(RESET_TEXT_COLOR + "♕ > ");
            String line = scanner.nextLine();

            try {
                result = this.eval(line);
                //result = line;
                System.out.println(SET_TEXT_COLOR_BLUE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
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
                //case "signin" -> signIn(params);
                //case "rescue" -> rescuePet(params);
                //case "list" -> listPets();
                //case "signout" -> signOut();
                //case "adopt" -> adoptPet(params);
                //case "adoptall" -> adoptAllPets();
                case "test" -> "nice!";
                default -> "Unknown Command";
            };
        } catch (Throwable e) {
            return e.getMessage();
        }
    }


}
