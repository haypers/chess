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
    ChessBoard boardData;
    String message;
    clientRole role;

    public enum ServerMessageType {
        LOAD_GAME,
        ERROR,
        NOTIFICATION
    }
    public enum clientRole{
        White,
        Black,
        Observer,
        non
    }

    public ServerMessage(ServerMessageType type) {
        this.serverMessageType = type;
    }

    public ServerMessage(ServerMessageType type, String messageForUser) {
        this.serverMessageType = type;
        this.message = messageForUser;
    }

    public ServerMessageType getServerMessageType() {
        return this.serverMessageType;
    }

    public ChessBoard getBoard(){
        return boardData;
    }

    public clientRole getRole(){
        return this.role;
    }

    public String getNotificationMessage(){
        return this.message;
    }

    public void setRole (clientRole newRole){
        this.role = newRole;
    }

    public void setBoard(ChessBoard board){
        this.boardData = board;
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
}
