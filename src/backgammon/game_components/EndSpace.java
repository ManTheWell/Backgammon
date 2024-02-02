package backgammon.game_components;

import java.awt.*;
import java.awt.image.BufferedImage;

import backgammon.settings.GameSettings;

public class EndSpace extends Space implements Clickable, Drawable {
    int x;
    int width;
    int height;
    int pieces;
    int[] yValues;

    int player;
    Color color;
    boolean top;

    int clickAlpha = 80;
    boolean countingUp;
    boolean valid = false;

    Dice dice = null;

    public EndSpace(int x, int[] yValues, int width, int height, int player, boolean top) {
        this.x = x;
        this.yValues = yValues;
        this.top = top;

        this.width = width;
        this.height = height;

        this.player = player;
        color = GameSettings.playerColors[player];
    }

    @Override
    public BufferedImage image() {
        return makeImage();
    }

    private BufferedImage makeImage() {
        BufferedImage image = new BufferedImage(width, height, 1);
        Graphics2D g = (Graphics2D) image.getGraphics();

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, width, height);

        int stroke = width / 18;
        // show if a valid move to spot
        g.setColor((valid) ? new Color(110, 110 + clickAlpha, 30) : BoardInfo.dark_background_color);
        g.fillRect(stroke, stroke, width - (2 * stroke), height - (2 * stroke));

        if (pieces <= 0) {
            g.dispose();
            return image;
        }

        g.setColor(color);
        int pieceHeight = height / 15;
        for (int i = 0;i < pieces;i++) {
            int y = (top) ? (i * pieceHeight) : (height - ((i + 1) * pieceHeight));
            g.setColor(Color.BLACK);
            g.fillRect(0, y, width, pieceHeight);
            g.setColor(color);
            g.fillRect(stroke, y + stroke, (width - (2*stroke)), (pieceHeight - (2*stroke)));
        }

        g.dispose();
        return image;
    }

    @Override
    public void update() {
        if (valid) {
            if (countingUp && clickAlpha < 80) clickAlpha += 2;
            if (!countingUp && clickAlpha > 40) clickAlpha -= 2;
            if (clickAlpha <= 40 || clickAlpha >= 80) countingUp = !countingUp;
        }
    }

    public void addPiece() {
        pieces += 1;
        dice.use();
        dice = null;
    }

    @Override
    public void setDice(Dice dice) {
        this.dice = dice;
    }

    @Override
    public int color() {
        return player;
    }

    public void addPiece(int color) {}

    @Override
    public int x() {
        return x;
    }

    @Override
    public int y() {
        return top ? yValues[0] : yValues[1] - height;
    }

    @Override
    public Rectangle bounds() {
        int y = top ? yValues[0] : yValues[1] - height;
        return new Rectangle(x, y, width, height);
    }

    public EndSpace copy() {
        EndSpace e = new EndSpace(x, yValues, width, height, player, top);
        e.pieces = pieces;
        return e;
    }

    public String toString() {
        return String.format("endspace c:%s #p:%s", player, pieces);
    }

    public void click() { }

    @Override
    public void offClick() { }
}
