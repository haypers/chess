package websocket.messages;

import chess.ChessBoard;

import java.util.Objects;

/**
 * Represents a Message the server can send through a WebSocket
 * 
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class ServerMessage {
    ServerMessageType serverMessageType;
    ChessBoard game;
    String errorMessage;
    ClientRole role;
    String message;

    public enum ServerMessageType {
        LOAD_GAME,
        ERROR,
        NOTIFICATION
    }
    public enum ClientRole {
        White,
        Black,
        Observer,
        non,
        noChange
    }

    public ServerMessage(ServerMessageType type) {
        this.serverMessageType = type;
    }

    public ServerMessage(ServerMessageType type, String messageForUser) {
        this.serverMessageType = type;
        this.errorMessage = messageForUser;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public ServerMessageType getServerMessageType() {
        return this.serverMessageType;
    }

    public ChessBoard getBoard(){
        return game;
    }

    public ClientRole getRole(){
        return this.role;
    }

    public String getNotificationMessage(){
        return this.message;
    }

    public String getErrorMessage(){
        return this.errorMessage;
    }

    public void setRole (ClientRole newRole){
        this.role = newRole;
    }

    public void setBoard(ChessBoard board){
        this.game = board;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServerMessage)) {
            return false;
        }
        ServerMessage that = (ServerMessage) o;
        return getServerMessageType() == that.getServerMessageType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getServerMessageType());
    }

    @Override
    public String toString() {
        return "ServerMessage{" +
                "serverMessageType=" + serverMessageType +
                ", game=" + game +
                ", errorMessage='" + errorMessage + '\'' +
                ", role=" + role +
                ", message='" + message + '\'' +
                '}';
    }
}



