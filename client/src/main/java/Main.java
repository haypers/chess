import chess.*;
import ui.ConnectRepl;

public class Main {
    public static void main(String[] args) {
        System.out.println("♕ 240 Chess Client: ");
        System.out.println("Welcome to You Personal Chess client");
        System.out.println("");
        System.out.println("Help/H -> help menu");
        var serverUrl = "http://localhost:8080";
        if (args.length == 1) {
            serverUrl = args[0];
        }

        new ConnectRepl().run();
    }

}