package mapboard;

import things.*;
import character.*;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

public class GameObjectSpawner {
    private final Random rand = new Random();
    private final ArrayList<Point> itemsPosition = new ArrayList<>();

    public ArrayList<Wall> spawnWalls(int columns, int rows, int numWalls) {
        ArrayList<Wall> walls = new ArrayList<>();
        for (int i = 0; i < numWalls; i++) {
            Point pos = getValidPosition(columns, rows);

            if (isValidWallPlacement(pos, walls)) {
                walls.add(new Wall(pos.x, pos.y));
                itemsPosition.add(pos);
            } else {
                i--;
            }
        }
        return walls;
    }

    private boolean isValidWallPlacement(Point newPos, ArrayList<Wall> existingWalls) {
        int adjacentWalls = 0;

        // Mảng chứa 4 cạnh để check
        Point[] adjacentPositions = {
                new Point(newPos.x - 1, newPos.y), // trái
                new Point(newPos.x + 1, newPos.y), // phải
                new Point(newPos.x, newPos.y - 1), // trên
                new Point(newPos.x, newPos.y + 1)  // dưới
        };

        for (Point pos : adjacentPositions) {
            if (isWallAt(pos, existingWalls)) {
                adjacentWalls++;
            }
        }

        boolean hasCornerWalls = false;
        Point[] cornerPositions = {
                new Point(newPos.x - 1, newPos.y - 1), // góc trên trái
                new Point(newPos.x + 1, newPos.y - 1), // góc trên phải
                new Point(newPos.x - 1, newPos.y + 1), // góc dưới trái
                new Point(newPos.x + 1, newPos.y + 1)  // góc dưới phải
        };

        for (Point corner : cornerPositions) {
            if (isWallAt(corner, existingWalls)) {
                hasCornerWalls = true;
                break;
            }
        }
        return adjacentWalls < 2 || (adjacentWalls == 2 && !hasCornerWalls);
        // Ít hơn 2 wall cạnh, nếu ằng 2 thì nếu không có góc
    }

    private boolean isWallAt(Point pos, ArrayList<Wall> walls) {
        for (Wall wall : walls) {
            if (wall.getPos().x == pos.x && wall.getPos().y == pos.y) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Coin> spawnCoins(int columns, int rows, int numCoins) {
        ArrayList<Coin> coins = new ArrayList<>();
        for (int i = 0; i < numCoins; i++) {
            Point pos = getValidPosition(columns, rows);
            coins.add(new Coin(pos.x, pos.y));
            itemsPosition.add(pos);
        }
        return coins;
    }

    public Monster spawnMonster(int columns, int rows, Player player, ArrayList<Wall> walls) {
        Point pos = getValidPosition(columns, rows);
        return new Monster(player, walls, pos.x, pos.y);
    }

    public ArrayList<Trap> spawnTraps(int columns, int rows, int numTraps) {
        ArrayList<Trap> traps = new ArrayList<>();
        for (int i = 0; i < numTraps; i++) {
            Point pos = getValidPosition(columns, rows);
            traps.add(new Trap(pos.x, pos.y));
            itemsPosition.add(pos);
        }
        return traps;
    }

    public ArrayList<Potion> spawnPotions(int columns, int rows, int numPotions) {
        ArrayList<Potion> potions = new ArrayList<>();
        for (int i = 0; i < numPotions; i++) {
            Point pos = getValidPosition(columns, rows);
            potions.add(new Potion(pos.x, pos.y));
            itemsPosition.add(pos);
        }
        return potions;
    }

    private Point getValidPosition(int columns, int rows) {
        Point pos;
        do {
            pos = new Point(rand.nextInt(columns), rand.nextInt(rows));
        } while (itemsPosition.contains(pos));
        return pos;
    }

    public void deletePos() {
        itemsPosition.clear();
    }
}