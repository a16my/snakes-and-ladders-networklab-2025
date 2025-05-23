package server;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GameState {
    private final int[] playerPositions = {1, 1};
    private int currentPlayer = 0;
    private final Map<Integer, Integer> snakes = new HashMap<>();
    private final Map<Integer, Integer> ladders = new HashMap<>();
    private final Random rand = new Random();
    private int lastRoll = -1;

    public GameState() {
       
        ladders.put(3, 22);
        ladders.put(20, 41);
        ladders.put(11, 56);

        snakes.put(74, 45);
        snakes.put(87, 24);
        snakes.put(99, 21);
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public int[] getPlayerPositions() {
        return playerPositions;
    }

    public int getLastRoll() {
        return lastRoll;
    }

    public String handleRoll(int playerId) {
        if (playerId != currentPlayer) return "IGNORE";

        int roll = rand.nextInt(6) + 1;
        lastRoll = roll;
        int newPos = playerPositions[playerId] + roll;

        if (newPos > 100) newPos = playerPositions[playerId];

        if (ladders.containsKey(newPos)) {
            newPos = ladders.get(newPos);
        } else if (snakes.containsKey(newPos)) {
            newPos = snakes.get(newPos);
        }

        playerPositions[playerId] = newPos;

        boolean win = (playerPositions[playerId] == 100);
        String response = "MOVE#" + playerId + "," + playerPositions[playerId];

        if (win) {
            response += "\nWIN#" + playerId;
        } else {
            currentPlayer = (currentPlayer + 1) % 2;
            response += "\nTURN#" + currentPlayer;
        }

        return response;
    }
}
