package ui;

import chess.ChessGame;

public class ServerResponseObject {
    public String username;
    public String authToken;

    public String username(){
        return username;
    }

    public String authToken(){
        return authToken;
    }
}
