package backgammon.game_components;


import java.awt.*;
import java.awt.image.BufferedImage;

import backgammon.settings.GameSettings;

public class Start implements Drawable {
    int x;
    int y;
    int width;
    int height;

    int timer;
    boolean show;

    int player = 0;
    Color[] colors = new Color[]{null, Color.WHITE, Color.BLACK};

    public Start(int x, int y, int width, int height, int player) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.player = player;

        timer = 125;
        show = true;
    }

    @Override
    public BufferedImage image() {
        if (!show) return null;

        BufferedImage image = new BufferedImage(width, height, 2);
        Graphics2D g = (Graphics2D) image.getGraphics();

        float alpha;
        if (timer > 100) alpha = 1;
        else alpha = timer / 100f;
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

        g.setColor(colors[player]);
        g.fillRect(0, 0, width, height);

        g.setColor(GameSettings.playerColors[player]);
        g.fillRect(2, 2, width - 4, height - 4);

        g.setColor(new Color(120, 150, 120));
        int stroke = Math.min(width, height) / 10;
        g.fillRect(stroke, stroke, (width - (2 * stroke)), (height - (2 * stroke)));

        g.setColor(Color.WHITE);
        String str = " " + GameSettings.playerNames[player] + " goes first. ";
        findMaxFontSize(g, str, (width - (2 * stroke)));
        int sw = g.getFontMetrics().stringWidth(str);
        g.drawString(str, ((width / 2) - (sw / 2)), ((height / 2) + ((2 * g.getFont().getSize()) / 5)));

        g.dispose();

        return image;
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

    @Override
    public void update() {
        if (timer > 0) timer--;
        else show = false;
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
