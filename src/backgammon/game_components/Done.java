package backgammon.game_components;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Done implements Drawable, Clickable {
    int x;
    int y;
    int width;
    int height;

    BufferedImage image;

    public Done(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.image = makeImage();
    }

    @Override
    public BufferedImage image() {
        return image;
    }

    private BufferedImage makeImage() {
        BufferedImage image = new BufferedImage(width, height, 1);
        Graphics2D g = (Graphics2D) image.getGraphics();

        int border = Math.min((width / 10), (height / 8));
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, width, height);

        g.setColor(new Color(150, 0, 0));
        g.fillRect(border, border, (width - (2 * border)), (height - (2 * border)));

        g.setFont(new Font("calibre", Font.PLAIN, (height / 2)));
        int sw = g.getFontMetrics().stringWidth("DONE");
        g.setColor(Color.WHITE);
        g.drawString("DONE", ((width/2) - (sw/2) - 1), ((height / 2) + ((2 * g.getFont().getSize()) / 5)));

        return image;
    }

    @Override
    public int x() {
        return x - (width / 2);
    }

    @Override
    public int y() {
        return y - (height / 2);
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
    public void update() { }
}
