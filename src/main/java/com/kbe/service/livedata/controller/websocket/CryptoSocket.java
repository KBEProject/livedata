package com.kbe.service.livedata.controller.websocket;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


public class CryptoSocket {

    private static String webSocketURI = "wss://www.bitforex.com/mkapi/coinGroup1/ws";
    private ClientEndpoint clientEndPoint;

    public void getCryptoData() {

        try {
            // open websocket
            clientEndPoint = new ClientEndpoint(new URI(webSocketURI));

            // add listener
            clientEndPoint.addMessageHandler(new ClientEndpoint.MessageHandler() {
                public void handleMessage(String message) {
                    System.out.println(message);
                    Serversocket.broadcast(message);
                }
            });

            clientEndPoint.sendMessage("[{\"type\":\"subHq\",\"event\":\"trade\",\"param\":{\"businessType\":\"coin-usdt-btc\", \"size\":2}}]");
            clientEndPoint.sendMessage("[{\"type\":\"subHq\",\"event\":\"trade\",\"param\":{\"businessType\":\"coin-usdt-eth\", \"size\":2}}]");
            clientEndPoint.sendMessage("[{\"type\":\"subHq\",\"event\":\"trade\",\"param\":{\"businessType\":\"coin-usdt-ltc\", \"size\":2}}]");
            clientEndPoint.sendMessage("[{\"type\":\"subHq\",\"event\":\"trade\",\"param\":{\"businessType\":\"coin-usdt-doge\", \"size\":2}}]");


        } catch (URISyntaxException ex) {
            System.err.println("URISyntaxException exception: " + ex.getMessage());
        }
    }

    public void closeCryptoSocket() {
        try {
            System.out.println("CLOSED");
            clientEndPoint.getSession().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
