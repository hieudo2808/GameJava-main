package character;

import things.Wall;
import java.awt.*;
import java.util.ArrayList;

public class Monster extends Character {
    private final Player player;
    private long lastMoveTime;
    private static final long MOVE_DELAY = 500;

    public Monster(Player player, ArrayList<Wall> walls, int x, int y) {
        super(walls, x, y, "images/monster.png");
        this.player = player;
        this.lastMoveTime = System.currentTimeMillis();
    }

    @Override
    public void tick() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastMoveTime >= MOVE_DELAY) {
            moveTowardsPlayer();
            lastMoveTime = currentTime;
        }
    }

    private void moveTowardsPlayer() {
        Point newPos = new Point(pos.x, pos.y);

        if (pos.x < player.getPos().x) {
            newPos.x++;
        } else if (pos.x > player.getPos().x) {
            newPos.x--;
        }

        if (!isCollidingWithWall(newPos)) {
            pos.x = newPos.x;
        }

        newPos = new Point(pos.x, pos.y);
        if (pos.y < player.getPos().y) {
            newPos.y++;
        } else if (pos.y > player.getPos().y) {
            newPos.y--;
        }

        if (!isCollidingWithWall(newPos)) {
            pos.y = newPos.y;
        }
    }
}