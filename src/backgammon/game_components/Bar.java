package backgammon.game_components;

import backgammon.settings.GameSettings;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Bar implements Drawable {
    int x;
    int y;
    int size;
    int stroke;
    int player;
    boolean top;
    int numPieces;

    public Bar(int x, int y, int size, int stroke, int player, boolean top) {
        this.x = x;
        this.y = y;
        this.top = top;
        this.size = size;
        this.stroke = stroke;
        this.player = player;
    }

    @Override
    public BufferedImage image() {
        return makeImage();
    }

    private BufferedImage makeImage() {
        if (numPieces == 0) return null;

        int h = Math.min((size * numPieces), (size * 3)) + 4;
        int s = (numPieces > 3) ? ((size * 3) / numPieces) : size;

        BufferedImage image = new BufferedImage(s + 4, h, 2);

        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setColor(BoardInfo.wood_color);
        g.fillRect(0, 0, s + 4, h);

        g.setStroke(new BasicStroke(stroke));
        for (int i = 0;i < numPieces;i++) {
            g.setColor(GameSettings.playerColors[player]);
            int y = (s * i) + 2;
            g.fillArc(2, y, s, s, 0, 360);
            g.setColor(Color.BLACK);
            g.drawArc(2, y, s, s, 0, 360);
        }

        g.dispose();
        return image;
    }

    @Override
    public void update() { }

    @Override
    public int x() {
        return x - (size / 2);
    }

    @Override
    public int y() {
        BufferedImage i = image();
        if (i == null) return y;

        return y - ((top) ? i.getHeight() : 0);
    }

    public Bar copy() {
        Bar b = new Bar(x, y, size, stroke, player, top);
        b.numPieces = numPieces;
        return b;
    }
}
