package things;

import java.util.Random;

public class Coin extends GameObject {
    private final int point;

    public Coin(int x, int y) {
        super(x, y, "images/coin.png");
        Random random = new Random();
        point = random.nextInt(100) + 1;
    }

    public int getPoint() {
        return point;
    }
}