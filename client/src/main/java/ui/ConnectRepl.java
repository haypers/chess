package ui;

import java.util.Scanner;

import static java.awt.Color.BLUE;

public class ConnectRepl {


    public void run() {
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            System.out.print("♕>  ");
            String line = scanner.nextLine();

            try {
                //result = client.eval(line);
                result = line;
                System.out.println(result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
    }


}
