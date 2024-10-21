package dataaccess;

import exception.ResponseException;
import model.GameData;
import model.PublicGameData;
import model.UserData;
import java.util.ArrayList;

public interface DataAccess {

    boolean checkIfUsersExists(String userName) throws ResponseException;

    boolean checkIfGameExists(int gameID) throws ResponseException;

    boolean checkIfHashExists(String hash) throws ResponseException;

    void addUser(UserData data) throws ResponseException;

    boolean saveAuthToken(String userName, String authToken) throws ResponseException;

    boolean saveGameData(int gameID, GameData gameData) throws ResponseException;

    ArrayList<PublicGameData> getAllGames() throws ResponseException;

    GameData getGame(int gameID) throws ResponseException;

    boolean clearDatabase() throws ResponseException;

    String getPassHash(String userName) throws ResponseException;

    String getUserFromToken(String authToken) throws ResponseException;

    boolean logoutUser(String token) throws ResponseException;
}
