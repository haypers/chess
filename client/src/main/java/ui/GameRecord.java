package ui;

public class GameRecord {
    private int index;
    private String name;
    private int gameId;

    // Constructor
    public GameRecord(int index, String name, int gameId) {
        this.index = index;
        this.name = name;
        this.gameId = gameId;
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

    @Override
    public String toString() {
        return index + " - " + name;
    }
}
