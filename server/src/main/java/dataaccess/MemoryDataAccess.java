package dataaccess;

import java.util.HashMap;
import java.util.Map;

public class MemoryDataAccess{

    private Map<String, String> dataAccess = new HashMap<>();;

    public boolean CheckIfUsersExists(String userName) {
        if (dataAccess.containsKey(userName)){
            System.out.println("username is found");
            return true;
        }
        else{
            return false;
        }
    }
    public void addUser(String userName, String password) {
        dataAccess.put(userName, password);
    }
}
