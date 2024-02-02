package backgammon.buttons;

import backgammon.settings.BoolSetting;
import backgammon.settings.GameSettings;
import backgammon.settings.Setting;
import backgammon.settings.SettingScreen;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ClickButton implements Button {
    BufferedImage image;

    String text;
    String var_name;
    Button owner;

    Font font;
    boolean rg;
    boolean show;
    boolean value;

    int x;
    int y;
    int width;
    int height;

    public ClickButton(int x, int y, int w, int h) {
        this(x, y, w, h, null, "", "", false, false, false);
    }

    public ClickButton(int x, int y, int w, int h, Font f, String txt) {
        this(x, y, w, h, f, txt, "", false, false, false);
    }

    public ClickButton(int x, int y, int w, int h, Font f, String txt, boolean show, boolean rg, boolean value) {
        this(x, y, w, h, f, txt, "", show, rg, value);
    }

    public ClickButton(int x, int y, int w, int h, Font f, String txt, String var, boolean show, boolean rg, boolean val) {
        this.rg = rg;
        this.font = f;
        this.show = show;
        this.value = val;

        this.text = txt;
        this.var_name = var;

        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;

        this.owner = owner;

        reset();
        makeImage();
    }

    @Override
    public BufferedImage image() {
        return image;
    }

    private void makeImage() {
        BufferedImage temp_image = new BufferedImage(width, height, 2);
        Graphics2D g = (Graphics2D) temp_image.getGraphics();

        // border
        Color black = new Color(10, 10, 10);
        g.setColor(black);
        g.fillRect(0, 0, width, height);

        // inner square
        int border = (height / 8);

        if (show && rg)
            g.setColor((value) ? new Color(100, 250, 100) : new Color(140, 10, 10));
        else if (show)
            g.setColor((value) ? new Color(150, 150, 150) : new Color(100, 100, 100));
        else
            g.setColor(new Color(150, 150, 150));
        g.fillRect(border, border, (width - (2 * border)), (height - (2 * border)));

        // add text
        g.setColor(black);
        findMaxFontSize(g, text, width);
        int sw = g.getFontMetrics().stringWidth(text);
        int ty = (height / 2) + ((2 * g.getFont().getSize()) / 5);
        g.drawString(text, (width / 2) - (sw / 2), ty);

        g.dispose();
        image = temp_image;
    }

    private void findMaxFontSize(Graphics2D g, String str, int width) {
        if (font != null) {
            g.setFont(font);
            return;
        }

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
    public int x() {
        return x;
    }

    @Override
    public int y() {
        return y;
    }

    public Rectangle bounds() {
        return new Rectangle(x, y, width, height);
    }

    @Override
    public void save() {
        if (var_name.equalsIgnoreCase(""))
            return;

        if (var_name.equalsIgnoreCase("Fix Settings")) {
            GameSettings.setFixedSettings(value);
        }

        else
            System.out.println("Did not find setting \"" + var_name + "\"");
    }

    @Override
    public void reset() {
        if (var_name.equalsIgnoreCase(""))
            return;

        if (var_name.equalsIgnoreCase("Fix Settings"))
            value = GameSettings.fixedSettings;

        else
            System.out.println("Did not find setting \"" + var_name + "\"");

        makeImage();
    }

    public void click() {
        this.value = !value;
        makeImage();
        if (owner != null)
            owner.tell();
    }

    @Override
    public void tell() { }

    @Override
    public void setOwner(Button b) {
        owner = b;
    }

    public void setValue(boolean value) {
        this.value = value;
        makeImage();
    }

    @Override
    public String getAssociatedSetting() {
        return var_name;
    }

    @Override
    public Setting getSetting() {
        return new BoolSetting(var_name, value);
    }
}
