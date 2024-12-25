package mapboard;

import character.*;
import things.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Board extends JPanel implements ActionListener, KeyListener {
    // Constants
    private static final int DELAY = 25;
    public static final int TILE_SIZE = 50;
    public static final int ROWS = 12;
    public static final int COLUMNS = 18;
    public static final int NUM_COINS = 20;
    public static final int NUM_WALLS = 30;
    public static final int NUM_TRAPS = 5;
    public static final int NUM_POTIONS = 3;

    // Game state
    private boolean gameOver;
    private boolean gameWin;
    private final Timer timer;
    private final String playerName;

    // Game objects
    private Player player;
    private ArrayList<Coin> coins;
    private Monster monster;
    private ArrayList<Wall> walls;
    private ArrayList<Trap> traps;
    private ArrayList<Potion> potions;

    // UI components
    private final JButton restartButton;
    private final JButton btnExit;
    private final GameRenderer renderer;
    private final GameObjectSpawner spawner;

    public Board(String playerName) {
        this.playerName = playerName;
        this.renderer = new GameRenderer(TILE_SIZE, ROWS, COLUMNS);
        this.spawner = new GameObjectSpawner();

        setPreferredSize(new Dimension(TILE_SIZE * COLUMNS, TILE_SIZE * ROWS));
        setBackground(new Color(232, 232, 232));

        initializeGame();

        timer = new Timer(DELAY, this);
        timer.start();

        restartButton = createRestartButton();
        btnExit = createBtnExit();

        setupPanel();
    }

    private void initializeGame() {
        walls = spawner.spawnWalls(COLUMNS, ROWS, NUM_WALLS);
        player = new Player(playerName, walls);
        coins = spawner.spawnCoins(COLUMNS, ROWS, NUM_COINS);
        traps = spawner.spawnTraps(COLUMNS, ROWS, NUM_TRAPS);
        potions = spawner.spawnPotions(COLUMNS, ROWS, NUM_POTIONS);
        monster = spawner.spawnMonster(COLUMNS, ROWS, player, walls);

        gameOver = false;
        gameWin = false;
    }

    private JButton createRestartButton() {
        JButton button = new JButton("Restart");
        button.setBounds(300, 320, 100, 50);
        button.setVisible(false);
        button.addActionListener(_ -> restartGame());
        return button;
    }

    private JButton createBtnExit() {
        JButton button = new JButton("Exit");
        button.setBounds(500, 320, 100, 50);
        button.setVisible(false);
        button.addActionListener(_ -> System.exit(0));
        return button;
    }

    private void setupPanel() {
        setLayout(null);
        add(restartButton);
        add(btnExit);
        addKeyListener(this);
        setFocusable(true);
    }

    private void restartGame() {
        initializeGame();
        timer.start();
        restartButton.setVisible(false);
        btnExit.setVisible(false);
        spawner.deletePos();
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (coins.isEmpty()) {
            gameWin = true;
            handleGameEnd();
            return;
        }

        if (!gameOver && !gameWin) {
            updateGameState();
            repaint();
        }
    }

    private void updateGameState() {
        player.tick();
        monster.tick();
        checkCollisions();
    }

    private void checkCollisions() {
        checkCoinCollisions();
        checkTrapCollisions();
        checkPotionCollisions();
        checkMonsterCollision();
    }

    private void checkCoinCollisions() {
        coins.removeIf(coin -> {
            if (player.getPos().equals(coin.getPos())) {
                player.colisionWithCoin(coin);
                return true;
            }
            return false;
        });
    }

    private void checkTrapCollisions() {
        traps.removeIf(trap -> {
            if (player.getPos().equals(trap.getPos())) {
                player.collisionWithTrap();
                if (player.getLife() <= 0) {
                    gameOver = true;
                    handleGameEnd();
                }
                return true;
            }
            return false;
        });
    }

    private void checkPotionCollisions() {
        potions.removeIf(potion -> {
            if (player.getPos().equals(potion.getPos())) {
                player.addLife();
                return true;
            }
            return false;
        });
    }

    private void checkMonsterCollision() {
        if (player.getPos().equals(monster.getPos())) {
            gameOver = true;
            handleGameEnd();
        }
    }

    private void handleGameEnd() {
        player.saveScoreToFile();
        restartButton.setVisible(true);
        btnExit.setVisible(true);
        timer.stop();
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (gameOver) {
            renderer.drawGameOver(g, getWidth(), getHeight());
        } else if (gameWin) {
            renderer.drawGameWin(g, getWidth(), getHeight(), player.getScore());
        } else {
            renderer.drawGameState(g, this, traps, coins, potions, monster, walls, player);
        }
        Toolkit.getDefaultToolkit().sync();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        player.keyPressed(e);
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}