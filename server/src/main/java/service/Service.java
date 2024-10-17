package service;
import dataaccess.MemoryDataAccess;
import model.UserData;

public class Service {

    private MemoryDataAccess m = new MemoryDataAccess();

    public Boolean registerUser(UserData newUser){
        if(!m.CheckIfUsersExists(newUser.getUsername())){
            m.addUser(newUser.getUsername(), newUser.getPassword());
            return true;
        }
        else{
            return false;
        }
    }
}
