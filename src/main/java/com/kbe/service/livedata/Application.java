package com.kbe.service.livedata;

import com.kbe.service.livedata.controller.websocket.CryptoSocket;
import com.kbe.service.livedata.controller.websocket.Serversocket;
import org.glassfish.tyrus.server.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.websocket.DeploymentException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@SpringBootApplication
public class Application {
	private static CryptoSocket cryptoSocket = new CryptoSocket();

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		serveServer();
	}

	private static void serveServer(){
		Server server = null;
		try {
			server = new Server("localhost", 8085, "/socket", null, Serversocket.class);
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			server.start();
			System.out.println("Please press a key to stop the server.");
			reader.readLine();
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (DeploymentException e) {
			throw new RuntimeException(e);
		} finally {
			if (server != null) {
				server.stop();
			}
		}
	}
}
