package model;

import chess.ChessGame;

public record GameData(Integer gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
    @Override
    public Integer gameID() {
        return gameID;
    }

    @Override
    public String whiteUsername() {
        return whiteUsername;
    }

    @Override
    public String blackUsername() {
        return blackUsername;
    }

    @Override
    public String gameName() {
        return gameName;
    }

    @Override
    public ChessGame game() {
        return game;
    }
}
