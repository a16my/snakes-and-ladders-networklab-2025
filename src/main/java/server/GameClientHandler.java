package server;

import java.io.*;
import java.net.*;

public class GameClientHandler extends Thread {
    private Socket socket;
    private int playerId;
    private GameState gameState;
    private BufferedReader in;
    private PrintWriter out;
    private GameClientHandler opponent;

    public GameClientHandler(Socket socket, int playerId, GameState gameState) {
        this.socket = socket;
        this.playerId = playerId;
        this.gameState = gameState;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setOpponent(GameClientHandler opp) {
        this.opponent = opp;
    }

    public void send(String msg) {
        out.println(msg);
    }

    @Override
    public void run() {
        try {
            send("PLAYER#" + playerId);
            send("START#2");

            if (playerId == 0) {
                send("TURN#0");
            }

            String line;
            while ((line = in.readLine()) != null) {
                if (line.startsWith("ROLL")) {
                    String result = gameState.handleRoll(playerId);
                    int roll = gameState.getLastRoll();

                    send("ROLL#" + roll);
                    if (opponent != null) opponent.send("ROLL#" + roll);

                    for (String msg : result.split("\n")) {
                        send(msg);
                        if (opponent != null) opponent.send(msg);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Player " + playerId + " disconnected.");
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}