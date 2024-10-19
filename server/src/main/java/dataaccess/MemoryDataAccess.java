package dataaccess;

import model.GameData;
import model.UserData;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class MemoryDataAccess{

    private Map<String, UserData> dataAccess = new HashMap<>();
    private Map<Integer, GameData> gameAccess = new HashMap<>();
    private Map<String, String> authAccess = new HashMap<>();

    public boolean CheckIfUsersExists(String userName) {
        if (dataAccess.containsKey(userName)){
            System.out.println("username is found");
            return true;
        }
        else{
            return false;
        }
    }


    public void addUser(UserData data) {
        dataAccess.put(data.getUsername(), data);
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
        return true;
    }

}
