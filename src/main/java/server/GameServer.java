

package server;

import java.io.*;
import java.net.*;
import java.util.*;

public class GameServer {
    private static final int PORT = 6000;
    private static final Queue<Socket> waitingClients = new LinkedList<>();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Server started on port " + PORT);

        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("New client connected: " + socket.getInetAddress());

            synchronized (waitingClients) {
                waitingClients.add(socket);

                if (waitingClients.size() >= 2) {
                    Socket player1 = waitingClients.poll();
                    Socket player2 = waitingClients.poll();

                    // Start a new game for these two players
                    new Thread(() -> startGame(player1, player2)).start();
                }
            }
        }
    }

    private static void startGame(Socket s1, Socket s2) {
        try {
            GameState gameState = new GameState();
            GameClientHandler p1 = new GameClientHandler(s1, 0, gameState);
            GameClientHandler p2 = new GameClientHandler(s2, 1, gameState);
            p1.setOpponent(p2);
            p2.setOpponent(p1);
            p1.start();
            p2.start();

            System.out.println("New game started between Player 0 and Player 1");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}