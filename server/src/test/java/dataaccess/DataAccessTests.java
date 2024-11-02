package dataaccess;

import chess.ChessGame;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Test;
import server.ResponseObject;
import service.Service;

import static org.junit.jupiter.api.Assertions.*;

public class DataAccessTests {

    @Test
    public void negativeUserCheck() {
        SQLDataAccess mem = new SQLDataAccess();
        mem.clearDatabase();
        assertFalse(mem.checkIfUsersExists("steve"));
    }

    @Test
    public void positiveUserCheck(){
        SQLDataAccess mem = new SQLDataAccess();
        mem.clearDatabase();

        mem.addUser(new UserData("test", "pass", "email"));

        assertTrue(mem.checkIfUsersExists("test"));
    }

    @Test
    public void negitiveGameCheck() {
        SQLDataAccess mem = new SQLDataAccess();
        mem.clearDatabase();

        assertFalse(mem.checkIfGameExists(3546));
    }

    @Test
    public void positiveGameCheck(){
        SQLDataAccess mem = new SQLDataAccess();
        mem.clearDatabase();

        mem.saveGameData(12312, new GameData(12312, null, null, "gameName", new ChessGame()));

        assertTrue(mem.checkIfGameExists(12312));
    }

    @Test
    public void negativeHashCheck() {
        SQLDataAccess mem = new SQLDataAccess();
        mem.clearDatabase();

        assertFalse(mem.checkIfHashExists("badhashexample"));
    }

    @Test
    public void positiveHashCheck(){
        SQLDataAccess mem = new SQLDataAccess();
        mem.clearDatabase();

        mem.saveAuthToken("bob", "randomhash");

        assertTrue(mem.checkIfHashExists("randomhash"));
    }

    @Test
    public void negativeAddUser() {
        SQLDataAccess mem = new SQLDataAccess();
        mem.clearDatabase();
        mem.addUser(new UserData("test4", "pass", "email"));

        assertFalse(mem.checkIfUsersExists("steve"));
    }

    @Test
    public void positiveAddUser(){
        SQLDataAccess mem = new SQLDataAccess();
        mem.clearDatabase();
        mem.addUser(new UserData("test", "pass", "email"));

        mem.addUser(new UserData("2", "pass", "email"));

        mem.addUser(new UserData("3", "pass", "email"));

        assertTrue(mem.checkIfUsersExists("2"));
    }

    @Test
    public void negativeAuth() {
        SQLDataAccess mem = new SQLDataAccess();
        mem.clearDatabase();

        mem.saveAuthToken("billy", "1234567890");

        assertFalse(mem.checkIfHashExists("badhashexample"));
    }

    @Test
    public void positiveAuth(){
        SQLDataAccess mem = new SQLDataAccess();
        mem.clearDatabase();
        mem.saveAuthToken("tom", "1234567890");
        assertTrue(mem.checkIfHashExists("1234567890"));
    }


}

