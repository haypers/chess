package websocket.commands;

import chess.ChessMove;
import websocket.messages.ServerMessage;

import java.util.Objects;

/**
 * Represents a command a user can send the server over a websocket
 *
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class UserGameCommand {

    private final CommandType commandType;

    private final String authToken;

    private final Integer gameID;
    private ServerMessage.clientRole requestedRole;

    private ChessMove move = null;

    public UserGameCommand(CommandType commandType, String authToken, Integer gameID) {
        this.commandType = commandType;
        this.authToken = authToken;
        this.gameID = gameID;
    }

    public UserGameCommand(CommandType commandType, String authToken, Integer gameID, ServerMessage.clientRole newRole) {
        this.commandType = commandType;
        this.authToken = authToken;
        this.gameID = gameID;
        this.requestedRole = newRole;
    }

    public UserGameCommand(CommandType commandType, String authToken, Integer gameID, ChessMove newMove) {
        this.commandType = commandType;
        this.authToken = authToken;
        this.gameID = gameID;
        this.move = newMove;
    }

    public enum CommandType {
        CONNECT,
        MAKE_MOVE,
        LEAVE,
        RESIGN
    }

    public CommandType getCommandType() {
        return commandType;
    }

    public String getAuthToken() {
        return authToken;
    }

    public Integer getGameID() {
        return gameID;
    }

    public ChessMove getMove(){
        return this.move;
    }

    public ServerMessage.clientRole getRequestedRole(){
        return this.requestedRole;
    }

    public void setRole(ServerMessage.clientRole role){
        this.requestedRole = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserGameCommand)) {
            return false;
        }
        UserGameCommand that = (UserGameCommand) o;
        return getCommandType() == that.getCommandType() &&
                Objects.equals(getAuthToken(), that.getAuthToken()) &&
                Objects.equals(getGameID(), that.getGameID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCommandType(), getAuthToken(), getGameID());
    }
}
