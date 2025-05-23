

package client;

import javax.swing.*;
import java.awt.*;

public class SnakesAndLaddersGame extends JFrame {
    private BoardPanel boardPanel;
    private JButton rollButton;
    private JLabel turnLabel;

    private int[] playerPositions = {1, 1};
    private int currentPlayer = 0;
    private int myPlayerId = -1;

    private GameClient client;

    public SnakesAndLaddersGame(String serverIp) {
        setTitle("Snakes and Ladders");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        boardPanel = new BoardPanel(playerPositions);
        add(boardPanel, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        rollButton = new JButton("Roll Dice");
        turnLabel = new JLabel("Waiting for game to start...");

        rollButton.addActionListener(e -> {
            if (myPlayerId == currentPlayer) {
                client.send("ROLL");
            }
        });

        controlPanel.add(turnLabel);
        controlPanel.add(rollButton);
        add(controlPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        try {
            client = new GameClient(serverIp, 6000, this::handleServerMessage);
            client.start();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Could not connect to server.");
            System.exit(1);
        }
    }

    private void handleServerMessage(String msg) {
        if (msg.startsWith("PLAYER#")) {
            myPlayerId = Integer.parseInt(msg.substring(7));
        } else if (msg.startsWith("MOVE#")) {
            String[] tokens = msg.substring(5).split(",");
            int id = Integer.parseInt(tokens[0]);
            int pos = Integer.parseInt(tokens[1]);
            playerPositions[id] = pos;
            boardPanel.repaint();
        } else if (msg.startsWith("TURN#")) {
            currentPlayer = Integer.parseInt(msg.substring(5));
            boolean myTurn = (myPlayerId == currentPlayer);
            turnLabel.setText(myTurn ? "Your Turn" : "Opponent's Turn");
            rollButton.setEnabled(myTurn);
        } else if (msg.startsWith("WIN#")) {
            int winner = Integer.parseInt(msg.substring(4));
            JOptionPane.showMessageDialog(this, "Player " + (winner + 1) + " wins!");
            rollButton.setEnabled(false);
        } else if (msg.startsWith("ROLL#")) {
            int rollValue = Integer.parseInt(msg.substring(5));
            boardPanel.setLastRoll(rollValue);
        }
    }

public static void main(String[] args) {
    String serverIp = (args.length > 0) ? args[0] : "127.0.0.1";
    SwingUtilities.invokeLater(() -> new SnakesAndLaddersGame(serverIp));
    System.out.println("Connecting to server at: " + serverIp);
}

}