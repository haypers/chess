package model;

public record PublicGameData(Integer gameID, String whiteUsername, String blackUsername, String gameName) {
    public PublicGameData(GameData game){
        this(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName());
    }
}
