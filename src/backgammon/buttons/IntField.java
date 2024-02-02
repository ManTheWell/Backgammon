package backgammon.buttons;

import backgammon.settings.BoolSetting;
import backgammon.settings.Setting;
import backgammon.settings.StrSetting;

import java.awt.*;
import java.awt.image.BufferedImage;

public class IntField implements InputField {
    int value;
    String store;
    Button owner;
    int max_value;

    int x;
    int y;
    int width;
    int height;

    public IntField(int x, int y, int width, int height, int default_int, int max_value) {
        this.value = default_int;
        this.max_value = max_value;
        this.store = default_int + "";

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public String value() {
        return store;
    }

    public int int_value() {
        return value;
    }

    @Override
    public void addToValue(String addition) {
        try {
            if (addition.equalsIgnoreCase("\b"))
                store = store.substring(0, Math.max(0, store.length() - 1));
            else if (Integer.parseInt(store + addition) <= max_value) {
                if (store.equalsIgnoreCase("0"))
                    store = addition;
                else
                    store += addition;
            }

            if (store.equalsIgnoreCase(""))
                store = "0";

            value = Integer.parseInt(store);

            if (owner != null) owner.tell();
        } catch (NumberFormatException ignored) { }
    }

    @Override
    public void setValue(String value) {
        try {
            if (Integer.parseInt(value) <= max_value)
                store = value;
            this.value = Integer.parseInt(store);
        } catch (NumberFormatException ignored) { }
    }

    @Override
    public BufferedImage image() {
        BufferedImage image = new BufferedImage(width, height, 1);
        Graphics2D g = (Graphics2D) image.getGraphics();

        g.setColor(new Color(200, 200, 200));
        g.fillRect(0, 0, width, height);

        g.setColor(new Color(10, 10, 10));
        g.setFont(new Font("calibre", Font.BOLD, (int) (height * .9)));
        int sw = g.getFontMetrics().stringWidth(store);
        g.drawString(store, width - 3 - sw, ((height / 2) + ((2 * g.getFont().getSize()) / 5)));

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
    public void click() { }

    @Override
    public void tell() {

    }

    @Override
    public void setOwner(Button b) {
        owner = b;
    }

    @Override
    public void save() { }

    @Override
    public void reset() { }

    @Override
    public String getAssociatedSetting() {
        return "";
    }

    @Override
    public Setting getSetting() {
        return null;
    }
}
