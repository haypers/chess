package service;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dataaccess.MemoryDataAccess;
import model.UserData;
import server.ResponseObject;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Objects;

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

    public ResponseObject loginUser(String body){
        String username = "";
        String password = "";
        JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();

        if (jsonObject.has("username") && jsonObject.has("password")) {
            username = jsonObject.get("username").getAsString();
            password = jsonObject.get("password").getAsString();

            if(!username.isEmpty() && !password.isEmpty()){
                System.out.println("Good credentials sent for login");

                if (memory.checkIfUsersExists(username)){
                    String oldHash = memory.getPassHash(username);
                    String currentHash = "";
                    try {
                        MessageDigest digest = MessageDigest.getInstance("SHA-256");
                        byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
                        currentHash = Base64.getEncoder().encodeToString(hashBytes);
                    }
                    catch(Exception e){
                        System.out.println("error hashing login password");
                        return new ResponseObject(500,"""
                        { "message": "Error: hashing algorithm failed" }
                        """);
                    }
                    if (Objects.equals(oldHash, currentHash)){
                        String newAuthToken = memory.makeAuthToken(username);
                        return new ResponseObject(200,"{\"username\":\""+ username + "\", \"authToken\":\""+ newAuthToken +"\"}");
                    }
                    else{
                        return new ResponseObject(401,"""
                        { "message": "Error: unauthorized" }
                        """);
                    }
                }
                else{
                    return new ResponseObject(401,"""
                        { "message": "Error: unauthorized" }
                        """);
                }
            }
        }
        return new ResponseObject(401,"""
            { "message": "Error: unauthorized" }
            """);
    }

    public String makeAuthToken(String userName){
        return memory.makeAuthToken(userName);
    }


    public Boolean clearDatabase(){
        System.out.println("clearing database");
        return memory.clearDatabase();
    }


}
