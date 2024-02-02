package backgammon.game_components;

import java.awt.*;
import java.awt.image.BufferedImage;

import backgammon.settings.GameSettings;

public class BoardSpace extends Space implements Drawable, Clickable {
    int x;
    int width;
    int height;
    int pieces;
    int[] yValues;
    int image_height;

    int color;
    boolean down;
    int baseColor;

    int clickAlpha;
    boolean countingUp;
    boolean valid = false;
    boolean clicked = false;
    boolean badClick = false;
    boolean canMoveFrom = false;

    private Dice dice = null;

    public BoardSpace(int x, int[] yValues, int base_color, int color, int pieces, int width, int height, boolean down) {
        this.x = x;
        this.yValues = yValues;

        this.width = width;
        this.height = height;
        this.image_height = (7 * height) / 6;

        this.color = color;

        this.down = down;
        this.pieces = pieces;

        this.baseColor = base_color;
    }

    public boolean validMoveToSpace(int color) {
        return (pieces <= 1) || (color == this.color);
    }

    public BufferedImage makeImage() {
        BufferedImage image = new BufferedImage(width, image_height, 2);
        Graphics2D g = (Graphics2D) image.getGraphics();

        // triangle
        g.setColor(BoardInfo.base_colors[baseColor]);
        int[] trianglePoints = down ? new int[]{0, 0, height} : new int[]{image_height, image_height, (height / 6)};
        g.fillPolygon(new int[]{0, width, (width / 2)}, trianglePoints, 3);

        // show if clicked
        if (clicked)
            g.setColor(new Color(50, 130, 50 + clickAlpha));

        // show if a valid move to spot
        if (valid && GameSettings.showDestinationSpaces)
            g.setColor(new Color(110, 110 + clickAlpha, 30));

        // flash red if clicked and invalid
        if (badClick && GameSettings.showDestinationSpaces)
            g.setColor(new Color(170 + clickAlpha, 0, 0));

        g.fillPolygon(new int[]{0, width, (width / 2)}, trianglePoints, 3);

        // outline
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(width / 18f));
        g.drawPolygon(new int[]{0, width, (width / 2)}, trianglePoints, 3);

        // pieces. if none, return
        if (pieces <= 0) {
            g.dispose();
            return image;
        }

        // draw pieces
        int pieceSize = (height / Math.max(5, pieces));
        int piecesStartY = down ? 4 : (image_height - pieceSize - 4);
        for (int p = 0;p < pieces;p++) {
            int x = ((width / 2) - (pieceSize / 2));
            int y = down ? piecesStartY + (pieceSize * p) : piecesStartY - (pieceSize * p);

            Color playerColor = GameSettings.playerColors[color];
            // set the middle to be player's color
            g.setColor(playerColor);
            g.fillArc(x, y, pieceSize, pieceSize, 0, 360);

            // set the correct outline color
            if (p == pieces - 1 && canMoveFrom)
                g.setColor(new Color(60, 150, 0));
            else if (playerColor.getRed() < 50 && playerColor.getGreen() < 50 && playerColor.getBlue() < 50)
                g.setColor(new Color(200, 200, 200));
            else
                g.setColor(Color.BLACK);

            g.drawArc(x, y, pieceSize, pieceSize, 0, 360);
        }

        g.dispose();
        return image;
    }

    @Override
    public BufferedImage image() {
        return makeImage();
    }

    @Override
    public void update() {
        if (clicked || valid || badClick) {
            if (countingUp && clickAlpha < 80) clickAlpha += 2;
            if (!countingUp && clickAlpha > 40) clickAlpha -= 2;
            if (!countingUp && clickAlpha <= 40) badClick = false;
            if (clickAlpha <= 40 || clickAlpha >= 80) countingUp = !countingUp;
        }
        else clickAlpha = 80;
    }

    public void addPiece(int color) {
        pieces += 1;
        this.color = color;
        dice.use();
        dice = null;
    }

    public void removePiece() {
        pieces -= 1;
        if (pieces <= 0) this.color = 0;
    }

    public void click() {
        clicked = true;
        clickAlpha = 80;
        countingUp = true;
    }

    @Override
    public int color() {
        return color;
    }

    @Override
    public void setDice(Dice dice) {
        this.dice = dice;
    }

    @Override
    public void offClick() {
        clicked = false;
    }

    public void badClick() {
        badClick = true;
        clickAlpha = 40;
        countingUp = true;
    }

    @Override
    public int x() {
        return x;
    }

    @Override
    public int y() {
        return down ? yValues[0] : yValues[1] - image_height;
    }

    @Override
    public Rectangle bounds() {
        int y = down ? yValues[0] : yValues[1] - height;
        return new Rectangle(x, y, width, height);
    }

    public BoardSpace copy() {
        return new BoardSpace(x, yValues, baseColor, color, pieces, width, height, down);
    }

    public String toString() {
        return String.format("color:%s #pieces:%s", color, pieces);
    }
}
