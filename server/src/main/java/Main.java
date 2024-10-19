import chess.*;
import server.Server;

public class Main {
    public static void main(String[] args) {
        var server = new Server();
        var test = server.run(8080);
        System.out.println("♕ 240 Chess Server: Booting up");
        System.out.println("♕ 240 Chess Server: running on port 8080");
    }
}