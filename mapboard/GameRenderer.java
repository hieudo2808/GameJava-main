package mapboard;

import java.awt.*;
import javax.swing.*;
import character.*;
import things.*;
import java.util.ArrayList;

public class GameRenderer {
    private final int tileSize;
    private final int rows;
    private final int columns;

    public GameRenderer(int tileSize, int rows, int columns) {
        this.tileSize = tileSize;
        this.rows = rows;
        this.columns = columns;
    }

    public void drawBackground(Graphics g) {
        g.setColor(new Color(214, 214, 214));
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                if ((row + col) % 2 == 1) {
                    g.fillRect(col * tileSize, row * tileSize, tileSize, tileSize);
                }
            }
        }
    }

    public void drawGameState(Graphics g, JPanel panel,
                              ArrayList<Trap> traps, ArrayList<Coin> coins,
                              ArrayList<Potion> potions, Monster monster,
                              ArrayList<Wall> walls, Player player) {
        drawBackground(g);
        traps.forEach(trap -> trap.draw(g, panel));
        coins.forEach(coin -> coin.draw(g, panel));
        potions.forEach(potion -> potion.draw(g, panel));
        monster.draw(g, panel);
        walls.forEach(wall -> wall.draw(g, panel));
        player.draw(g, panel);
        drawScore(g, panel, player);
        drawLife(g, player);
    }

    public void drawGameOver(Graphics g, int width, int height) {
        drawEndGameMessage(g, "Game Over!",
                "Try to win games to get on the leaderboard", width, height);
    }

    public void drawGameWin(Graphics g, int width, int height, String score) {
        drawEndGameMessage(g, "You win!", "Score: " + score, width, height);
    }

    private void drawEndGameMessage(Graphics g, String title, String subtitle, int width, int height) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.RED);

        // Draw title
        g2d.setFont(new Font("Lato", Font.BOLD, 50));
        FontMetrics metrics = g2d.getFontMetrics();
        int x = (width - metrics.stringWidth(title)) / 2;
        int y = (height - metrics.getHeight()) / 2 + metrics.getAscent();
        g2d.drawString(title, x, y);

        // Draw subtitle
        g2d.setFont(new Font("Lato", Font.PLAIN, 30));
        FontMetrics scoreMetrics = g2d.getFontMetrics();
        int scoreX = (width - scoreMetrics.stringWidth(subtitle)) / 2;
        g2d.drawString(subtitle, scoreX, y + metrics.getHeight());
    }

    private void drawScore(Graphics g, JPanel panel, Player player) {
        String text = "Point: " + player.getScore();
        Graphics2D g2d = (Graphics2D) g;
        g2d.setFont(new Font("Lato", Font.BOLD, 25));
        FontMetrics metrics = g2d.getFontMetrics();

        int x = (panel.getWidth() - metrics.stringWidth(text)) / 2;
        int y = tileSize * (rows - 1) + ((tileSize - metrics.getHeight()) / 2) + metrics.getAscent();

        // Draw outline
        g2d.setColor(Color.BLACK);
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx != 0 || dy != 0) {
                    g2d.drawString(text, x + dx, y + dy);
                }
            }
        }

        // Draw gradient text
        GradientPaint gradient = new GradientPaint(x, y, Color.RED,
                x + metrics.stringWidth(text), y, Color.GREEN);
        g2d.setPaint(gradient);
        g2d.drawString(text, x, y);
    }

    private void drawLife(Graphics g, Player player) {
        String text = player.getName() + "   Life: " + player.getLife();
        Graphics2D g2d = (Graphics2D) g;
        g2d.setFont(new Font("Lato", Font.BOLD, 35));

        int x = tileSize;
        int y = tileSize - 10;

        // Draw outline
        g2d.setColor(Color.BLACK);
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx != 0 || dy != 0) {
                    g2d.drawString(text, x + dx, y + dy);
                }
            }
        }

        g2d.setColor(Color.WHITE);
        g2d.drawString(text, x, y);
    }
}