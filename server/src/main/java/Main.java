import server.Server;

public class Main {
    public static void main(String[] args) {
        System.out.println("♕ 240 Chess Server: Booting up");
        var server = new Server();
        var runningServer = server.run(8080);
        System.out.println("♕ 240 Chess Server: running on port:");
        System.out.println(runningServer);
    }
}