package com.kbe.service.livedata.controller.websocket;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@javax.websocket.ClientEndpoint
public class ClientEndpoint {

    private Session session;

    MessageHandler messageHandler;
    private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    public ClientEndpoint(URI uri) {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        try {
            this.session = container.connectToServer(this, uri);
        } catch (DeploymentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Callback hook for Connection open events.
     *
     * @param userSession the userSession which is opened.
     */
    @OnOpen
    public void onOpen(Session userSession) {
        System.out.println("opening cryptosocket");
        this.session = userSession;
        session.setMaxIdleTimeout(2099999999);
        /**executerService will dedicate a thread for pinging the server**/
        executorService.scheduleAtFixedRate(() -> {
            try {
                userSession.getBasicRemote().sendPing(ByteBuffer.wrap("pin".getBytes()));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }, 5, 5, TimeUnit.SECONDS);
        //PING PON, HEARTBEAT implementieren
    }

    /**
     * Callback hook for Connection close events.
     *
     * @param userSession the userSession which is getting closed.
     * @param reason      the reason for connection close
     */
    @OnClose
    public void onClose(Session userSession, CloseReason reason) {
        System.out.println("closing cryptosocket");
        this.session = null;
    }

    /**
     * Callback hook for Message Events. This method will be invoked when a client send a message.
     *
     * @param message The text message
     */
    @OnMessage
    public void onMessage(String message) {
        if (this.messageHandler != null) {
            this.messageHandler.handleMessage(message);
        }
    }

    /**
     * register message handler
     *
     * @param msgHandler
     */
    public void addMessageHandler(MessageHandler msgHandler) {
        this.messageHandler = msgHandler;
    }

    /**
     * Send a message.
     *
     * @param message
     */
    public void sendMessage(String message) {
        this.session.getAsyncRemote().sendText(message);
    }

    public Session getSession(){
        return this.session;
    }

    /**
     * Message handler.
     */
    public interface MessageHandler {

        void handleMessage(String message);
    }
}