package dataaccess;

import model.GameData;
import model.UserData;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class MemoryDataAccess{

    private Map<String, UserData> dataAccess = new HashMap<>();
    private Map<Integer, GameData> gameAccess = new HashMap<>();
    private Map<String, String> authAccess = new HashMap<>();

    public boolean checkIfUsersExists(String userName) {
        return dataAccess.containsKey(userName);
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

    public String makeAuthToken(String userName){
        try {
            long currentTimeMillis = Instant.now().toEpochMilli();
            String combinedInput = userName + currentTimeMillis;
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(combinedInput.getBytes(StandardCharsets.UTF_8));
            String authToken = Base64.getEncoder().encodeToString(hashBytes);
            authAccess.put(userName, authToken);
            return authToken;
        }
        catch(Exception e){
            return "";
        }
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

}
