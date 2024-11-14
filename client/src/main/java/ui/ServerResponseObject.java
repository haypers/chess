package ui;

import chess.ChessGame;
import model.PublicGameData;

import java.util.ArrayList;

public class ServerResponseObject {
    public String username;
    public String authToken;
    public String message;
    public Integer gameID;
    public ArrayList<PublicGameData> games;

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

    public ArrayList<PublicGameData> games(){
        return games;
    }
}
