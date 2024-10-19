package service;
import dataaccess.MemoryDataAccess;
import model.UserData;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

public class Service {

    private MemoryDataAccess m = new MemoryDataAccess();

    public Boolean registerUser(UserData newUser){
        if(!m.CheckIfUsersExists(newUser.getUsername())){
            m.addUser(newUser);
            return true;
        }
        else{
            return false;
        }
    }

    public String loginUser(UserData credentials){
        if (!m.isUser(credentials.getUsername())){
            return "";
        }
        String oldHash = m.getPassHash(credentials.username());
        String currentHash = "";
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(credentials.getPassword().getBytes(StandardCharsets.UTF_8));
            currentHash = Base64.getEncoder().encodeToString(hashBytes);
        }
        catch(Exception e){
            System.out.println("error hashing new session password");
        }
        if (oldHash == currentHash){
            return makeAuthToken(credentials.username());
        }
        else{
            return "";
        }
    }

    public String makeAuthToken(String userName){
        return m.makeAuthToken(userName);
    }


    public Boolean clearDatabase(){
        System.out.println("clearing database");
        return m.clearDatabase();
    }


}
