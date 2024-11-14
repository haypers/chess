package ui;

public class GameRecord {
    private int index;
    private String name;
    private int gameId;
    private String whitePlayer;
    private String blackPlayer;

    // Constructor
    public GameRecord(int index, String name, int gameId, String white, String black) {
        this.index = index;
        this.name = name;
        this.gameId = gameId;
        this.whitePlayer = white;
        this.blackPlayer = black;
    }

    // Getters and setters
    public int getIndex(){
        return index;
    }

    public int getGameID(){
        return gameId;
    }

    public String getName(){
        return name;
    }

    public void setWhite(String userName){
        whitePlayer = userName;
    }
    public void setBlack(String userName){
        blackPlayer = userName;
    }
    public String getWhitePlayer(){
        return whitePlayer;
    }
    public String getBlackPlayer(){
        return blackPlayer;
    }


    @Override
    public String toString() {
        return index + " - " + name + "   White: " + whitePlayer + "    Black: " + blackPlayer;
    }
}
