package model;

public record PublicGameData(Integer gameID, String whiteUsername, String blackUsername, String gameName) {

    public PublicGameData(GameData game){
        this(game.gameID(), game.whiteUsername() != null ? game.whiteUsername() : "", game.blackUsername() != null ? game.blackUsername() : "", game.gameName());
    }

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

}
