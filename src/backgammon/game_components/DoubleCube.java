package backgammon.game_components;

import backgammon.settings.GameSettings;

import java.awt.*;
import java.awt.image.BufferedImage;

public class DoubleCube implements Drawable, Clickable {
    BufferedImage image;

    int x;
    int y;

    int size;
    int player;
    int dotSize;

    GameBoardScreen board;

    int value;
    int[][] dots = new int[7][];

    public DoubleCube(int x, int y, int value, int size, int player, GameBoardScreen board) {
        this.x = x;
        this.y = y;

        this.size = size;
        this.value = value;
        this.board = board;
        this.player = player;

        this.dotSize = size / 4;
        int padding = (dotSize) / 2;
        int s2 = (size / 2) - padding;
        int s3 = (size / 3) - padding;
        int s4 = (size / 4) - padding;
        int s23 = ((2*size) / 3) - padding;
        int s34 = ((3*size) / 4) - padding;

        dots[1] = new int[]{s2, s2};
        dots[2] = new int[]{s3, s2, s23, s2};
        dots[3] = new int[]{s2, s3, s3, s23, s23, s23};
        dots[4] = new int[]{s3, s3, s23, s3, s3, s23, s23, s23};
        dots[5] = new int[]{s4, s4, s34, s4, s4, s34, s34, s34, s2, s2};
        dots[6] = new int[]{s3, s4, s3, s2, s3, s34, s23, s4, s23, s2, s23, s34};

        updateImage();
    }

    @Override
    public Rectangle bounds() {
        return new Rectangle(x - (size / 2), y - (size / 2), size, size);
    }

    @Override
    public void click() {

    }

    @Override
    public void offClick() {

    }

    @Override
    public BufferedImage image() {
        return image;
    }

    private void updateImage() {
        BufferedImage image = new BufferedImage(size, size, 2);

        // outside line on the dice
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
        g.setColor(Color.BLACK);
        g.fillRoundRect(0, 0, size, size, size / 2, size / 2);

        // inside coloring of the dice
        int stroke = size / 20;
        if (player > 0) g.setColor(GameSettings.playerColors[player]);
        else g.setColor(new Color(190, 190, 190));
        g.fillRoundRect(stroke, stroke, (size - (2 * stroke)), (size - (2 * stroke)), (size / 2), (size / 2));

        // dots
        if (player == 1) g.setColor(GameSettings.playerColors[2]);
        else if (player == 2) g.setColor(GameSettings.playerColors[1]);
        else g.setColor(Color.BLACK);
        for (int i = 0; i < dots[value].length; i += 2)
            g.fillArc(dots[value][i], dots[value][i + 1], dotSize, dotSize, 0, 360);

        g.dispose();
        this.image = image;
    }

    @Override
    public void update() { }

    @Override
    public int x() {
        return x - (size / 2);
    }

    @Override
    public int y() {
        return y - (size / 2);
    }
}
