
package client;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class BoardPanel extends JPanel {
    private int[] playerPositions;
    private final Map<Integer, Integer> snakes = new HashMap<>();
    private final Map<Integer, Integer> ladders = new HashMap<>();
    private int lastRoll = -1;

    public BoardPanel(int[] playerPositions) {
        this.playerPositions = playerPositions;

       snakes.put(74, 45);
        snakes.put(87, 24);
        snakes.put(99, 21);

        ladders.put(3, 22);
        ladders.put(20, 41);
        ladders.put(11, 56);

        setPreferredSize(new Dimension(600, 600));
    }

    public void setLastRoll(int roll) {
        this.lastRoll = roll;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int cellSize = getWidth() / 10;

        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                int x = col * cellSize;
                int y = (9 - row) * cellSize;

                g.setColor((row + col) % 2 == 0 ? new Color(210, 255, 230) : new Color(160, 230, 190));
                g.fillRect(x, y, cellSize, cellSize);

                int base = row * 10;
                int cellNum = base + (row % 2 == 0 ? col + 1 : 10 - col);
                g.setColor(Color.BLACK);
                g.drawString(String.valueOf(cellNum), x + 5, y + 15);
            }
        }

        g.setColor(Color.RED);
        snakes.forEach((from, to) -> drawArrow(g, from, to, cellSize));

       g.setColor(Color.BLUE);
        ladders.forEach((from, to) -> drawArrow(g, from, to, cellSize));

       for (int i = 0; i < playerPositions.length; i++) {
            Point p = getCellCoordinates(playerPositions[i], cellSize);
            g.setColor(i == 0 ? Color.RED : Color.BLUE);
            g.fillOval(p.x, p.y, 20, 20);
        }

        if (lastRoll != -1) {
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 18));
            g.drawString("🎲 Dice Rolled: " + lastRoll, 10, getHeight() - 10);
        }
    }

    private Point getCellCoordinates(int cell, int cellSize) {
        cell = Math.max(1, Math.min(cell, 100));
        int index = cell - 1;
        int row = index / 10;
        int col = index % 10;
        if (row % 2 == 1) col = 9 - col;
        int x = col * cellSize + cellSize / 2 - 10;
        int y = (9 - row) * cellSize + cellSize / 2 - 10;
        return new Point(x, y);
    }

    private void drawArrow(Graphics g, int from, int to, int cellSize) {
        Point p1 = getCellCoordinates(from, cellSize);
        Point p2 = getCellCoordinates(to, cellSize);
        g.drawLine(p1.x + 10, p1.y + 10, p2.x + 10, p2.y + 10);
    }
}