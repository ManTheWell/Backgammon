package backgammon.game_components;

import backgammon.settings.GameSettings;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Win implements Drawable, Clickable {
    int x;
    int y;

    int width;
    int height;

    int winner = 2;

    int alpha = 0;
    boolean countingUp = true;

    Color[] colors = new Color[]{null, Color.WHITE, Color.BLACK};

    public Win(int x, int y, int width, int height, int winner) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.winner = winner;
    }

    private BufferedImage makeImage() {
        BufferedImage i = new BufferedImage(width, height, 1);
        Graphics2D g = (Graphics2D) i.getGraphics();

        if (winner == 2) {
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, width, height);
        }

        g.setColor(colors[winner]);
        g.fillRect(2, 2, width - 4, height - 4);

        int stroke = Math.min(width, height) / 15;
        g.setColor(GameSettings.playerColors[winner]);
        g.fillRect(stroke, stroke, (width - (2 * stroke)), (height - (2 * stroke)));

        g.setColor(colors[winner]);

        String str;
        str = " " + GameSettings.playerNames[winner] + " WINS!";
        findMaxFontSize(g, str, (width - (2 * stroke)));
        addText(str, (height / 5), Font.BOLD, (2 * height / 5), g);

        str = "CONGRATULATIONS!";
        addText(str, (height / 8), Font.ITALIC, (3 * height / 5), g);

        if (winner == 1) g.setColor(new Color((255 - alpha), (255 - alpha), (255 - alpha)));
        else g.setColor(new Color(alpha, alpha, alpha));
        str = "Click To Play Again...";
        addText(str, (height / 12), Font.BOLD, (7 * height / 8), g);

        g.dispose();
        return i;
    }

    private void findMaxFontSize(Graphics2D g, String str, int width) {
        double div = 10;
        Font font = new Font("calibre", Font.BOLD, (int) (height / div));
        Font f = font;
        g.setFont(f);
        int sw = g.getFontMetrics().stringWidth(str);
        while (sw < width) {
            font = f;
            div -= 0.25;
            f = new Font("calibre", Font.BOLD, (int) (height / div));
            g.setFont(f);
            sw = g.getFontMetrics().stringWidth(str);
        }
        g.setFont(font);
    }

    private void addText(String str, int fontSize, int fontType, int y, Graphics g) {
        Font f = new Font("calibre", fontType, fontSize);
        g.setFont(f);
        int sw = g.getFontMetrics().stringWidth(str);
        g.drawString(str, (width/2) - (sw/2), (y + (g.getFont().getSize()) / 2));
    }

    @Override
    public Rectangle bounds() {
        return new Rectangle(x(), y(), width, height);
    }

    @Override
    public void click() { }

    @Override
    public void offClick() { }

    @Override
    public BufferedImage image() {
        return makeImage();
    }

    @Override
    public void update() {
        if (countingUp) alpha += 3;
        else alpha -= 3;

        if (alpha > 80 || alpha <= 0) countingUp = !countingUp;

    }

    @Override
    public int x() {
        return x - (width / 2);
    }

    @Override
    public int y() {
        return y - (height / 2);
    }
}
