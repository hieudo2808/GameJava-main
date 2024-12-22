// Character.java
package character;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import mapboard.Board;
import things.Wall;

public abstract class Character {
    protected BufferedImage image;
    protected Point pos;
    protected final ArrayList<Wall> walls;

    protected Character(ArrayList<Wall> walls, int x, int y, String imagePath) {
        this.walls = walls;
        this.pos = new Point(x, y);
        loadImage(imagePath);
    }

    protected void loadImage(String imagePath) {
        try {
            image = ImageIO.read(new File(imagePath));
        } catch (IOException exc) {
            System.out.println("Error opening image file: " + exc.getMessage());
        }
    }

    public void draw(Graphics g, ImageObserver observer) {
        g.drawImage(
                image,
                pos.x * Board.TILE_SIZE,
                pos.y * Board.TILE_SIZE,
                observer
        );
    }

    public Point getPos() {
        return pos;
    }

    protected boolean isCollidingWithWall(Point newPos) {
        return walls.stream().anyMatch(wall -> wall.getPos().equals(newPos));
    }

    public abstract void tick();
}