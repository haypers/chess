package dataaccess;

import model.GameData;
import model.PublicGameData;
import model.UserData;
import java.util.ArrayList;

public interface DataAccess {

    boolean checkIfUsersExists(String userName) throws DataAccessException;

    boolean checkIfGameExists(int gameID) throws DataAccessException;

    boolean checkIfHashExists(String hash) throws DataAccessException;

    void addUser(UserData data) throws DataAccessException;

    boolean saveAuthToken(String userName, String authToken) throws DataAccessException;

    boolean saveGameData(int gameID, GameData gameData) throws DataAccessException;

    ArrayList<PublicGameData> getAllGames() throws DataAccessException;

    GameData getGame(int gameID) throws DataAccessException;

    boolean clearDatabase() throws DataAccessException;

    String getPassHash(String userName) throws DataAccessException;

    String getUserFromToken(String authToken) throws DataAccessException;

    boolean logoutUser(String token) throws DataAccessException;
}
