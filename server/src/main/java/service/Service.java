package service;
import dataaccess.MemoryDataAccess;
import model.UserData;

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

    public String makeAuthToken(String userName){
        return m.makeAuthToken(userName);
    }


    public Boolean clearDatabase(){
        System.out.println("clearing database");
        return m.clearDatabase();
    }


}
