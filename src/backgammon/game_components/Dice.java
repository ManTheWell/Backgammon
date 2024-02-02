package backgammon.game_components;

import java.awt.*;
import java.util.Random;
import java.awt.image.BufferedImage;

import backgammon.settings.GameSettings;

public class Dice implements Drawable {
    int x;
    int y;

    int size;
    int player;
    int dotSize;

    int tValue = 6;
    int value;
    int time = 0;
    int[][] dots = new int[7][];

    boolean used;

    Dice(int x, int y, int value, int size, int player) {
        this.x = x;
        this.y = y;

        this.size = size;
        this.value = value;
        this.player = player;

        this.dotSize = size / 4;
        int p = (dotSize) / 2;
        int s2 = (size / 2) - p;
        int s3 = (size / 3) - p;
        int s4 = (size / 4) - p;
        int s23 = ((2*size) / 3) - p;
        int s34 = ((3*size) / 4) - p;

        dots[1] = new int[]{s2, s2};
        dots[2] = new int[]{s3, s2, s23, s2};
        dots[3] = new int[]{s2, s3, s3, s23, s23, s23};
        dots[4] = new int[]{s3, s3, s23, s3, s3, s23, s23, s23};
        dots[5] = new int[]{s4, s4, s34, s4, s4, s34, s34, s34, s2, s2};
        dots[6] = new int[]{s3, s4, s3, s2, s3, s34, s23, s4, s23, s2, s23, s34};
    }

    @Override
    public BufferedImage image() {
        return makeImage();
    }

    private BufferedImage makeImage() {
        BufferedImage image = new BufferedImage(size, size, 2);

        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
        g.setColor(Color.BLACK);
        g.fillRoundRect(0, 0, size, size, size / 2, size / 2);

        if (!used) g.setColor(GameSettings.playerColors[player]);
        else g.setColor(Color.DARK_GRAY);
        int stroke = size / 20;
        g.fillRoundRect(stroke, stroke, (size - (2 * stroke)), (size - (2 * stroke)), (size / 2), (size / 2));

        if (player == 1) g.setColor(GameSettings.playerColors[2]);
        if (player == 2) g.setColor(GameSettings.playerColors[1]);
        for (int i = 0; i < dots[tValue].length; i += 2)
            g.fillArc(dots[tValue][i], dots[tValue][i + 1], dotSize, dotSize, 0, 360);

        g.dispose();

        return image;
    }

    public void use() {
        used = true;
    }

    public Dice copy() {
        Dice d = new Dice(x, y, value, size, player);
        d.used = used;
        d.time = 100;
        return d;
    }

    @Override
    public void update() {
        if (time >= 20)
            tValue = value;
        else {
            tValue = new Random().nextInt(6) + 1;
            time++;
        }
    }

    @Override
    public int x() {
        return x;
    }

    @Override
    public int y() {
        return y;
    }
}
