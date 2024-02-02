package backgammon.buttons;

import backgammon.settings.Setting;

import java.awt.*;
import java.awt.image.BufferedImage;

public class StringField implements InputField {
    String value;
    Button owner;
    int max_length;

    int x;
    int y;
    int width;
    int height;

    public StringField(int x, int y, int width, int height, String default_string, int max_length, Button owner) {
        value = default_string;
        this.max_length = max_length;

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.owner = owner;
    }

    @Override
    public String value() {
        return value;
    }

    @Override
    public void addToValue(String addition) {
        if (addition.equalsIgnoreCase("\b"))
            value = value.substring(0, Math.max(0, value.length() - 1));
        else if ((!addition.equalsIgnoreCase("\uFFFF")) && (value.length() + 1) <= max_length)
            value += addition;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public BufferedImage image() {
        BufferedImage image = new BufferedImage(width, height, 1);
        Graphics2D g = (Graphics2D) image.getGraphics();

        g.setColor(new Color(200, 200, 200));
        g.fillRect(0, 0, width, height);

        g.setColor(new Color(10, 10, 10));
        g.setFont(new Font("calibre", Font.BOLD, (int) (height * .9)));
        int sw = g.getFontMetrics().stringWidth(value + "\"\"");
        g.drawString("\"" + value + "\"", width - 3 - sw, ((height / 2) + ((2 * g.getFont().getSize()) / 5)));

        g.dispose();
        return image;
    }

    @Override
    public int x() {
        return x;
    }

    @Override
    public int y() {
        return y;
    }

    @Override
    public Rectangle bounds() {
        return new Rectangle(x, y, width, height);
    }

    @Override
    public void click() {
        owner.tell();
    }

    @Override
    public void tell() { }

    @Override
    public void save() { }

    @Override
    public void reset() { }

    @Override
    public void setOwner(Button b) {
        owner = b;
    }

    @Override
    public String getAssociatedSetting() {
        return "";
    }

    @Override
    public Setting getSetting() {
        return null;
    }
}
