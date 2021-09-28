package com.company;

import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) throws IOException {
        // if (args.length < 1) { System.out.println( "Expected port number"); return; }
        // int port = Integer.parseInt(args[0]);
        int port = 4000;
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                System.out.println("Waiting for a client to connect");
                Socket client1 = serverSocket.accept();
                System.out.println("Client 1 connected");
                Socket client2 = serverSocket.accept();
                System.out.println("Client 2 connected");
                ClientHandler clientHandler = new ClientHandler(client1, client2);
                Thread thread = new Thread(clientHandler);
                thread.start();
                System.out.println("Started a new game");
            }
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }

        System.exit(0);
    }

}


class ClientHandler implements Runnable {
    private final Socket client1Socket;
    private final Socket client2Socket;

    public ClientHandler(Socket socket1, Socket socket2) {
        this.client1Socket = socket1;
        this.client2Socket = socket2;
    }

    @Override
    public void run() {
        Game game = new Game(client1Socket, client2Socket);
        game.run();
    }
}


