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

public class MemoryDataAccess implements DataAccess{

    private Map<String, UserData> dataAccess = new HashMap<>();
    private Map<Integer, GameData> gameAccess = new HashMap<>();
    private Map<String, String> authAccess = new HashMap<>();

    public boolean checkIfUsersExists(String userName) {
        return dataAccess.containsKey(userName);
    }
    public boolean checkIfGameExists(int gameID) {
        return gameAccess.containsKey(gameID);
    }
    public boolean checkIfHashExists(String hash) {
        return authAccess.containsKey(hash);
    }

    public void addUser(UserData data) {
        String passwordHashed = "";
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(data.getPassword().getBytes(StandardCharsets.UTF_8));
            passwordHashed = Base64.getEncoder().encodeToString(hashBytes);
        }
        catch(Exception e){
            System.out.println("error hashing new user password");
        }
        dataAccess.put(data.getUsername(), new UserData(data.getUsername(), passwordHashed, data.getEmail()));
    }

    public boolean saveAuthToken(String userName, String authToken){
        authAccess.put(authToken, userName);
        return true;
    }

    public boolean saveGameData(int gameID, GameData gameData){
        gameAccess.put(gameID, gameData);
        return true;
    }

    public ArrayList<PublicGameData> getAllGames(){
        ArrayList<PublicGameData> allGames = new ArrayList<>();
        for (Integer key : gameAccess.keySet()) {
            PublicGameData game = new PublicGameData(gameAccess.get(key));
            allGames.add(game);
        }
        System.out.println(allGames);
        return allGames;
    }

    public GameData getGame(int gameID){
        return gameAccess.get(gameID);
    }

    public boolean clearDatabase(){
        dataAccess = new HashMap<>();
        authAccess = new HashMap<>();
        gameAccess = new HashMap<>();
        return true;
    }

    public String getPassHash(String userName){
        return dataAccess.get(userName).password();
    }

    public void removeGame(Integer gameID){
        gameAccess.remove(gameID);
    }

    public String getUserFromToken(String authToken){
        return authAccess.getOrDefault(authToken, "");
    }

    public boolean logoutUser(String token){
        if (authAccess.containsKey(token)){
            authAccess.remove(token);
            return true;
        }
        else{
            return false;
        }
    }
}
