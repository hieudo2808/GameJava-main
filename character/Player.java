package character;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import logic.ScoreService;
import mapboard.Board;
import things.Coin;
import things.Wall;

public class Player extends Character {
    private int score;
    private final String playerName;
    private int life;

    public Player(String playerName, ArrayList<Wall> walls) {
        super(walls, 0, 0, "images/player.png");
        this.playerName = playerName;
        this.score = 0;
        this.life = 2;
    }

    public int getLife() {
        return life;
    }

    public String getName() {
        return playerName;
    }

    public void addLife() {
        life++;
    }

    public void collisionWithTrap() {
        life--;
    }

    public void keyPressed(KeyEvent e) {
        Point newPos = new Point(pos.x, pos.y);

        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> newPos.translate(0, -1);
            case KeyEvent.VK_RIGHT -> newPos.translate(1, 0);
            case KeyEvent.VK_DOWN -> newPos.translate(0, 1);
            case KeyEvent.VK_LEFT -> newPos.translate(-1, 0);
        }

        if (!isCollidingWithWall(newPos)) {
            pos = newPos;
        }
    }

    public void saveScoreToFile() {
        if (score > 0) {
            ScoreService.postScore(playerName, score);
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("scores.txt", true))) {
            bw.write(playerName + " : " + score + " points");
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void tick() {
        pos.x = Math.max(0, Math.min(pos.x, Board.COLUMNS - 1));
        pos.y = Math.max(0, Math.min(pos.y, Board.ROWS - 1));
    }

    public String getScore() {
        return String.valueOf(score);
    }

    public void addScore(int amount) {
        score += amount;
    }

    public void colisionWithCoin(Coin coin) {
        addScore(coin.getPoint());
    }
}