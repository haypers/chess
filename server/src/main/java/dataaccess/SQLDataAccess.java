package dataaccess;

import com.google.gson.Gson;
import exception.ResponseException;
import model.GameData;
import model.PublicGameData;
import model.UserData;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SQLDataAccess implements DataAccess {

    public SQLDataAccess(){
        System.out.println("The SQL object has been made.");
        try {
            DatabaseManager.createDatabase();
        } catch (DataAccessException e) {
            System.out.println("Sever creation Failure!");
            System.out.println(e);

        }
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        }
        catch (Exception e){
            System.out.println("Sever Startup Failure!");
            System.out.println(e);
        }
    }

    private final String[] createStatements = {
            """
        CREATE TABLE IF NOT EXISTS auth (
          `id` int NOT NULL AUTO_INCREMENT,
          `username` varchar(256) NOT NULL,
          `password` TEXT NOT NULL,
          `email` TEXT,
          PRIMARY KEY (`id`),
          UNIQUE KEY unique_username (`username`),
          INDEX idx_username (`username`)
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
    """,
            """
        CREATE TABLE IF NOT EXISTS user_tokens (
          `token` varchar(256) NOT NULL,
          `username` varchar(256) NOT NULL,
          PRIMARY KEY (`token`),
          INDEX idx_username (`username`)
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
    """
    };





    public boolean checkIfUsersExists(String userName) {
        String query = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(query)) {
            ps.setString(1, userName);

            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0; // If count > 0, user exists
                }
            }
        } catch (Exception e) {
            System.out.println("Error checking if user exists: " + e.getMessage());
        }
        return false;
    }

    public boolean checkIfGameExists(int gameID) {
        return false;
    }

    public boolean checkIfHashExists(String hash) {
        return false;
    }

    public void addUser(UserData data) {
        String statement = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        int id = 0;
        try {
            id = executeUpdate(statement, data.username(), data.getPassword(), data.email());
            System.out.println("new user: " + id);
        } catch (DataAccessException e) {
            System.out.println("DataAccess Error! SQL error");
            System.out.println(e);
        }
    }

    public boolean saveAuthToken(String userName, String authToken) {
        return false;
    }

    public boolean saveGameData(int gameID, GameData gameData)  {
        return false;
    }

    public ArrayList<PublicGameData> getAllGames() {
        return null;
    }

    public GameData getGame(int gameID) {
        return null;
    }

    public boolean clearDatabase() {
        return false;
    }

    public String getPassHash(String userName) {
        return "";
    }

    public String getUserFromToken(String authToken) {
        return "";
    }

    public boolean logoutUser(String token)  {
        return false;
    }

    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p){
                        ps.setString(i + 1, p);
                    }
                    else if (param instanceof Integer p ){
                        ps.setInt(i + 1, p);
                    }
                    else if (param instanceof UserData u){
                        ps.setString(i + 1, u.toString());
                    }
                    else if (param == null){
                        ps.setNull(i + 1, NULL);
                    }
                }
                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
        return 0;
    }
}