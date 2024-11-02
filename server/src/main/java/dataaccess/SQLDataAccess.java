package dataaccess;

import com.google.gson.Gson;
import exception.ResponseException;
import model.GameData;
import model.PublicGameData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

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
            String[] createStatements = {
        """
        CREATE TABLE IF NOT EXISTS users (
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
        CREATE TABLE IF NOT EXISTS auth (
          `token` varchar(256) NOT NULL,
          `username` varchar(256) NOT NULL,
          PRIMARY KEY (`token`),
          INDEX idx_username (`username`)
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
    """,
                    """
        CREATE TABLE IF NOT EXISTS games (
          `id` int NOT NULL,
          `gameData` TEXT,
          PRIMARY KEY (`id`)
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
    """
            };
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
            clearAuthTable();
        }
        catch (Exception e){
            System.out.println("Sever Startup Failure!");
            System.out.println(e);
        }
    }

    public boolean checkIfUsersExists(String userName) {
        String query = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (var conn = DatabaseManager.getConnection();
             var prepStatement = conn.prepareStatement(query)) {
            prepStatement.setString(1, userName);

            try (var rs = prepStatement.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        } catch (Exception e) {
            System.out.println("Error checking if user exists: " + e.getMessage());
        }
        return false;
    }

    public boolean checkIfGameExists(int gameID) {
        String query = "SELECT COUNT(*) FROM games WHERE id = ?";
        try (var conn = DatabaseManager.getConnection();
            var prepStatement = conn.prepareStatement(query)) {
            prepStatement.setInt(1, gameID);

            try (var rs = prepStatement.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return (count > 0);
                }
            }
        } catch (Exception e) {
            System.out.println("Error checking if game exists: " + e.getMessage());
        }
        return false;
    }

    public boolean checkIfHashExists(String hash) {
        String query = "SELECT COUNT(*) FROM auth WHERE token = ?";
        try (var conn = DatabaseManager.getConnection();
            var prepStatement = conn.prepareStatement(query)) {
            prepStatement.setString(1, hash);

            try (var rs = prepStatement.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        } catch (Exception e) {
            System.out.println("Error checking if auth token exists: " + e.getMessage());
        }
        return false;
    }

    public void addUser(UserData data) {
        String statement = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        int id = 0;
        try {
            String hashedPassword = BCrypt.hashpw(data.password(), BCrypt.gensalt());
            id = executeUpdate(statement, data.username(), hashedPassword, data.email());
            System.out.println("new user: " + id);
        } catch (DataAccessException e) {
            System.out.println("DataAccess Error! SQL error");
            System.out.println(e);
        }
    }

    public boolean saveAuthToken(String userName, String authToken) {
        String insertQuery = "INSERT INTO auth (token, username) VALUES (?, ?)";
        try {
            executeUpdate(insertQuery, authToken, userName);
            return true;
        } catch (DataAccessException e) {
            System.out.println("DataAccess Error!!! Unable to save token");
            System.out.println(e);
            return false;
        }
    }

    public boolean saveGameData(int gameID, GameData gameData)  {
        String lookForOldGame = "SELECT COUNT(*) FROM games WHERE id = ?";
        try (var conn = DatabaseManager.getConnection();
             var prepStatement = conn.prepareStatement(lookForOldGame)) {
            prepStatement.setInt(1, gameID);
            try (var rs = prepStatement.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    String deleteQuery = "DELETE FROM games WHERE id = ?";
                    try (var deletePrepStatement = conn.prepareStatement(deleteQuery)) {
                        deletePrepStatement.setInt(1, gameID);
                        deletePrepStatement.executeUpdate();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error checking for existing game data: " + e.getMessage());
            return false;
        }
        String insertQuery = "INSERT INTO games (id, gameData) VALUES (?, ?)";
        try {
            executeUpdate(insertQuery, gameID, gameData.toString());
            return true;
        } catch (DataAccessException e) {
            System.out.println("DataAccess Error!!! Unable to save game");
            System.out.println(e);
            return false;
        }
    }

    public ArrayList<PublicGameData> getAllGames() {
        ArrayList<PublicGameData> gamesList = new ArrayList<>();
        String query = "SELECT id, gameData FROM games";

        try (var conn = DatabaseManager.getConnection();
             var prepStatement = conn.prepareStatement(query);
             var rs = prepStatement.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String gameData = rs.getString("gameData");
                Gson deserializer = new Gson();
                GameData data = deserializer.fromJson(gameData, GameData.class);
                System.out.println(data.toString());

                gamesList.add(new PublicGameData(data));
            }
        } catch (Exception e) {
            System.out.println("Error retrieving all games: " + e.getMessage());
        }

        return gamesList;
    }

    public GameData getGame(int gameID) {
        String query = "SELECT id, gameData FROM games WHERE id = ?";
        GameData gameData = null;

        try (var conn = DatabaseManager.getConnection();
             var prepStatement = conn.prepareStatement(query)) {
            prepStatement.setInt(1, gameID);

            try (var rs = prepStatement.executeQuery()) {
                if (rs.next()) {
                    String gameDataJson = rs.getString("gameData");
                    Gson deserializer = new Gson();
                    gameData = deserializer.fromJson(gameDataJson, GameData.class);
                    return gameData;
                }
            }
        } catch (Exception e) {
            System.out.println("Error retrieving game with ID " + gameID + ": " + e.getMessage());
        }
        return null;
    }

    public boolean clearAuthTable() {
        String statement = "DELETE FROM auth";
        try {
            executeUpdate(statement);
            return true;

        } catch (DataAccessException e) {
            System.out.println("Clear auth table Error! Unable to clear auth table");
            System.out.println(e);
            return false;
        }
    }

    public boolean clearDatabase() {
        try {
            executeUpdate("DELETE FROM auth");
            executeUpdate("DELETE FROM users");
            executeUpdate("DELETE FROM games");
            return true;

        } catch (DataAccessException e) {
            System.out.println("clear data Error! Unable to clear all tables");
            System.out.println(e);
            return false;
        }
    }

    public String getPassHash(String userName) {
        String query = "SELECT password FROM users WHERE username = ?";
        try (var conn = DatabaseManager.getConnection();
             var prepStatement = conn.prepareStatement(query)) {
            prepStatement.setString(1, userName);

            try (var rs = prepStatement.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("password");
                }
            }
        } catch (Exception e) {
            System.out.println("Error retrieving password hash: " + e.getMessage());
        }
        return "";
    }

    public String getUserFromToken(String authToken) {
        String query = "SELECT username FROM auth WHERE token = ?";
        try (var conn = DatabaseManager.getConnection();
             var prepStatement = conn.prepareStatement(query)) {
            prepStatement.setString(1, authToken);

            try (var rs = prepStatement.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("username");
                }
            }
        } catch (Exception e) {
            System.out.println("Error retrieving user from token: " + e.getMessage());
        }
        return "";
    }


    public boolean logoutUser(String token)  {
        String statement = "DELETE FROM auth WHERE token = ?";
        try {
            executeUpdate(statement, token);
            return true;
        } catch (DataAccessException e) {
            System.out.println("DataAccess Error! Unable to logout user");
            System.out.println(e);
            return false;
        }
    }

    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var prepStatement = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p){
                        prepStatement.setString(i + 1, p);
                    }
                    else if (param instanceof Integer p ){
                        prepStatement.setInt(i + 1, p);
                    }
                    else if (param instanceof UserData u){
                        prepStatement.setString(i + 1, u.toString());
                    }
                    else if (param == null){
                        prepStatement.setNull(i + 1, NULL);
                    }
                }
                prepStatement.executeUpdate();

                var rs = prepStatement.getGeneratedKeys();
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