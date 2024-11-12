package ui;

import java.util.Objects;
import java.util.Scanner;

import static java.awt.Color.BLUE;

public class ConnectRepl {

//5: help, register(http), login(http), quit
//6:help create game(http), list games(http), join game (http then websocket), join observer (http then websocket)
        //help, redraw, leave (remove yourself as a player or observer, someone takes your place), make move(web socket), resign (end game, lose), highlight.
    public void run() {
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            System.out.print("♕ > ");
            String line = scanner.nextLine();

            try {
                result = this.eval(line);
                //result = line;
                System.out.println("\u001b[34m" + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
    }

    public String eval(String input){
        if (Objects.equals(input, "test")){
            return "nice!";
        }
        else{
            return input;
        }
    }


}
