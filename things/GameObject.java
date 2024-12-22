package things;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import mapboard.Board;

public abstract class GameObject {
    protected BufferedImage image;
    protected final Point pos;

    protected GameObject(int x, int y, String imagePath) {
        pos = new Point(x, y);
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
}