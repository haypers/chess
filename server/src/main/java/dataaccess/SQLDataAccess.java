package dataaccess;

import model.GameData;
import model.PublicGameData;
import model.UserData;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class SQLDataAccess implements DataAccess {

    /*public SQLDataAccess() throws DataAccessException{
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT 1+1")) {
                var rs = preparedStatement.executeQuery();
                rs.next();
                System.out.println(rs.getInt(1));
            }
        }
    }*/

    public SQLDataAccess(){
        System.out.println("The SQL object has been made.");
    }

    @Override
    public boolean checkIfUsersExists(String userName) {
        return false;
    }

    public boolean checkIfGameExists(int gameID) {
        return false;
    }

    public boolean checkIfHashExists(String hash) {
        return false;
    }

    public void addUser(UserData data) {

    }

    public boolean saveAuthToken(String userName, String authToken) {
        return false;
    }

    public boolean saveGameData(int gameID, GameData gameData)  {
        return false;
    }

    public ArrayList<PublicGameData> getAllGames() {
        return null;
    }

    public GameData getGame(int gameID) {
        return null;
    }

    public boolean clearDatabase() {
        return false;
    }

    public String getPassHash(String userName) {
        return "";
    }

    public String getUserFromToken(String authToken) {
        return "";
    }

    public boolean logoutUser(String token)  {
        return false;
    }
}