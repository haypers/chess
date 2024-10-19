package service;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dataaccess.MemoryDataAccess;
import model.UserData;
import server.ResponseObject;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

public class Service {

    private MemoryDataAccess memory = new MemoryDataAccess();

    public ResponseObject registerUser(String body){
        String username = "";
        String password = "";
        String email = "";
        JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();

        if (jsonObject.has("username") && jsonObject.has("password") && jsonObject.has("email")) {
            username = jsonObject.get("username").getAsString();
            password = jsonObject.get("password").getAsString();
            email = jsonObject.get("email").getAsString();

            if(!username.isEmpty() && !password.isEmpty() && !email.isEmpty()){
                System.out.println("Good credentials sent for registration");

                if(!memory.checkIfUsersExists(username)) {
                    UserData user = new UserData(username, password, email);
                    memory.addUser(user);
                    String newAuthToken = makeAuthToken(username);
                    return new ResponseObject(200, "{\"username\":\"" + username + "\", \"authToken\":\"" + newAuthToken + "\"}" );
                }
                else{
                    return new ResponseObject(403,"""
                        {"message": "Error: already taken"}
                        """);
                }
            }
        }
        return new ResponseObject(400,"""
                        {"message": "Error: bad request"}
                        """);
    }

    public String loginUser(UserData credentials){
        if (!memory.checkIfUsersExists(credentials.getUsername())){
            return "";
        }
        String oldHash = memory.getPassHash(credentials.username());
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
        return memory.makeAuthToken(userName);
    }


    public Boolean clearDatabase(){
        System.out.println("clearing database");
        return memory.clearDatabase();
    }


}
