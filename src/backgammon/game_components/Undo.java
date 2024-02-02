package backgammon.game_components;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Undo implements Clickable, Drawable {
    int x;
    int y;
    int width;
    int height;

    SaveState save;
    BufferedImage image;

    public Undo(int x, int y, int width, int height, SaveState save) {
        this.x = x;
        this.y = y;

        this.width = width;
        this.height = height;

        this.save = save;

        image = makeImage();
    }

    @Override
    public BufferedImage image() {
        return image;
    }

    private BufferedImage makeImage() {
        BufferedImage image = new BufferedImage(width + 2, height + 2, 2);
        Graphics2D g = (Graphics2D) image.getGraphics();

        int stroke = (height / 7);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, width, height);

        g.setColor(new Color(100, 150, 120));
        g.fillRect(stroke, stroke, (width - (2 * stroke)), (height - (2 * stroke)));

        g.setColor(Color.BLACK);
        int fontSize = (int) (0.95 * height);
        Font f = new Font("calibre", Font.BOLD, fontSize);
        g.setFont(f);
        int sw = g.getFontMetrics().stringWidth("UNDO");
        g.drawString("UNDO", (width/2) - (sw/2) + 1, ((height / 2) + ((2 * g.getFont().getSize()) / 5)));

        g.dispose();
        return image;
    }

    @Override
    public Rectangle bounds() {
        return new Rectangle(x(), y(), width, height);
    }

    @Override
    public int x() {
        return x - (image.getWidth() / 2);
    }

    @Override
    public int y() {
        return y - (image.getHeight() / 2);
    }

    @Override
    public void update() { }

    @Override
    public void click() { }

    @Override
    public void offClick() { }
}
