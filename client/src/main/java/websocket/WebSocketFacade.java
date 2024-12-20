package websocket;

import com.google.gson.Gson;
import ui.RenderBoard;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import static ui.EscapeSequences.*;
import static websocket.messages.ServerMessage.ClientRole.*;

//need to extend Endpoint for websocket to work properly
public class WebSocketFacade extends Endpoint {

    Session session;
    String userName = "err";
    private ui.Repl parentClass = null;
    public ServerMessage.ClientRole myRole = ServerMessage.ClientRole.noChange;

    public WebSocketFacade(String url, ui.Repl parentClass) {
        try {
            this.parentClass = parentClass;
            url = url.replace("http", "ws");
            URI socketURI = new URI(url);

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);
                    //System.out.println("message: " + notification.getServerMessageType());
                    process(notification);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            System.out.println("error in startup (");
            System.out.println(ex);
            System.out.println(") error in startup.");
        }
    }

    public void send(String msg){
        try {
            this.session.getBasicRemote().sendText(msg);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void setUsername(String name){
        this.userName = name;
    }

    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void process(ServerMessage sm){
        if (sm.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION){
            System.out.println(SET_TEXT_COLOR_YELLOW + sm.getNotificationMessage());
            System.out.print(RESET_TEXT_COLOR + userName + " > ");
            if (sm.getRole() == non){
                myRole = non;
                parentClass.isInGame = false;
                parentClass.board = null;
                parentClass.games = new ArrayList<>();
            }
        }
        else if (sm.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME){
            parentClass.board = sm.getBoard();
            if (sm.getRole() == White){
                myRole = White;
                parentClass.isInGame = true;
                System.out.println();
                System.out.println(new RenderBoard().getBoardRender(false, parentClass.board));
                if (sm.getNotificationMessage() != null){
                    System.out.println(sm.getNotificationMessage());
                }
                System.out.print(RESET_TEXT_COLOR + userName + " > ");

            }
            else if(sm.getRole() == ServerMessage.ClientRole.Black) {
                myRole = Black;
                parentClass.isInGame = true;
                System.out.println();
                System.out.println(new RenderBoard().getBoardRender(true, parentClass.board));
                if (sm.getNotificationMessage() != null){
                    System.out.println(sm.getNotificationMessage());
                }
                System.out.print(RESET_TEXT_COLOR + userName + " > ");
            }
            else if (sm.getRole() == ServerMessage.ClientRole.Observer){
                myRole = Observer;
                parentClass.isInGame = true;
                System.out.println();
                //System.out.println("White's view: ");
                System.out.println(new RenderBoard().getBoardRender(false, parentClass.board));
                //System.out.println("Blacks's view: ");
                //System.out.println(new RenderBoard().getBoardRender(true, parentClass.board));
                if (sm.getNotificationMessage() != null){
                    System.out.println(sm.getNotificationMessage());
                }
                System.out.print(RESET_TEXT_COLOR + userName + " > ");
            }
            else if (sm.getRole() == ServerMessage.ClientRole.noChange){

                System.out.println();
                System.out.println(new RenderBoard().getBoardRender(myRole == Black, parentClass.board));
                if (sm.getNotificationMessage() != null){
                    System.out.println(sm.getNotificationMessage());
                }
                System.out.print(RESET_TEXT_COLOR + userName + " > ");


            }
            else{
                System.out.println("You are not currently playing a game.");
                parentClass.isInGame = false;
                System.out.print(RESET_TEXT_COLOR + userName + " > ");
            }

        }
        else if (sm.getServerMessageType() == ServerMessage.ServerMessageType.ERROR){
            System.out.println(SET_TEXT_COLOR_RED + sm.getErrorMessage() + RESET_TEXT_COLOR);
            if (sm.getRole() == non){
                parentClass.isInGame = false;
                System.out.println("You are not currently playing a game.");
            }
            System.out.print(RESET_TEXT_COLOR + userName + " > ");
        }
    }

}

