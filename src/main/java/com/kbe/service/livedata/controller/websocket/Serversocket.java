package com.kbe.service.livedata.controller.websocket;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value = "/server")

public class Serversocket {

    private Session session;
    private static CryptoSocket cryptoSocket = new CryptoSocket();
    private static Set<Serversocket> clients = new CopyOnWriteArraySet<>();

    private boolean expectsMSG = false;

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        clients.add(this);
        System.out.println("Client: " + this.session.getId() + " has connected!");
        cryptoSocket.getCryptoData();
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        this.expectsMSG = true;
    }

    @OnClose
    public void onClose(Session session) {
        clients.remove(this);
        if (getNumberOfClients() == 0) {
            cryptoSocket.closeCryptoSocket();
        }
        System.out.println("Client: " + this.session.getId() + " disconnected!");
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
    }

    public static void broadcast(String message) {

        clients.forEach(client -> {
            synchronized (client) {
                try {
                    if(true){
                        client.session.getBasicRemote().
                                sendObject(message);
                    }
                } catch (IOException | EncodeException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private int getNumberOfClients() {
        return clients.size();
    }
}
