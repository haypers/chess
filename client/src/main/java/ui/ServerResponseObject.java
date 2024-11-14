package ui;

import chess.ChessGame;

public class ServerResponseObject {
    public String username;
    public String authToken;
    public String message;
    public Integer gameID;

    public String username(){
        return username;
    }

    public String message(){
        return message;
    }

    public String authToken(){
        return authToken;
    }

    public Integer gameID(){
        return gameID;
    }
}
