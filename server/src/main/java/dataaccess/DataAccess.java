package dataaccess;

import model.GameData;
import model.PublicGameData;
import model.UserData;
import java.util.ArrayList;

public interface DataAccess {

    boolean checkIfUsersExists(String userName);

    boolean checkIfGameExists(int gameID) ;

    boolean checkIfHashExists(String hash) ;

    void addUser(UserData data) ;

    boolean saveAuthToken(String userName, String authToken) ;

    boolean saveGameData(int gameID, GameData gameData);

    ArrayList<PublicGameData> getAllGames() ;

    GameData getGame(int gameID) ;

    boolean clearDatabase();

    String getPassHash(String userName) ;

    String getUserFromToken(String authToken) ;

    boolean logoutUser(String token) ;
}
