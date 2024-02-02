package backgammon.buttons;

import backgammon.settings.BoolSetting;
import backgammon.settings.GameSettings;
import backgammon.settings.Setting;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ToggleButton implements Button {
    BufferedImage image;
    Button owner;

    String var_name;
    boolean value;

    int x;
    int y;
    int width;
    int height;

    public ToggleButton(int x, int y, int width, int height, String name) {
        this(x, y, width, height, name, null);
    }

    public ToggleButton(int x, int y, int width, int height, String name, Button owner) {
        this.var_name = name;

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.owner = owner;

        reset();
        makeImage();
    }

    @Override
    public BufferedImage image() {
        return image;
    }

    public void makeImage() {
        BufferedImage temp_image = new BufferedImage(width, height, 2);
        Graphics2D g = (Graphics2D) temp_image.getGraphics();

        g.setColor(new Color(230, 230, 230));
        g.setFont(new Font("calibre", Font.BOLD, (int) (height * 0.8)));
        g.drawString(var_name, 3, ((height / 2) + ((2 * g.getFont().getSize()) / 5)));

        int border = height / 6;
        int out_width = (int) (1.5 * height);
        int in_width = out_width - (2 * border);
        int in_height = height - (2 * border);
        int toggleX = (width - out_width) - 3;
        int toggleXb = (toggleX + border);

        g.setColor(Color.BLACK);
        g.fillRoundRect(toggleX, 1, out_width, height, out_width, height);

        g.setColor((value) ? new Color(100, 250, 100) : new Color(140, 10, 10));
        g.fillRoundRect(toggleXb, border, in_width, in_height, in_width, in_height);

        int ballSize = (int) (height * 0.8);
        int ballX = (value) ? width - ballSize - 4 : toggleXb - 1;
        g.setColor(Color.BLACK);
        g.fillOval(ballX, (int) (height * 0.1), ballSize, ballSize);

        g.dispose();
        image = temp_image;
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
    public void save() {
        if (var_name.equalsIgnoreCase("Double Click"))
            GameSettings.setDoubleClick(value);
        else if (var_name.equalsIgnoreCase("Show Movable Pieces"))
            GameSettings.setShowMovablePieces(value);
        else if (var_name.equalsIgnoreCase("Show Destination Spaces"))
            GameSettings.setShowDestinationSpaces(value);

        else
            System.out.println("Did not find setting \"" + var_name + "\"");
    }

    public void setValue(boolean value) {
        this.value = value;
        makeImage();
    }

    @Override
    public void reset() {
        if (var_name.equalsIgnoreCase("Double Click"))
            value = GameSettings.doubleClick;
        else if (var_name.equalsIgnoreCase("Show Movable Pieces"))
            value = GameSettings.showMovablePieces;
        else if (var_name.equalsIgnoreCase("Show Destination Spaces"))
            value = GameSettings.showDestinationSpaces;

        else
            System.out.println("Did not find setting \"" + var_name + "\"");

        makeImage();
    }

    @Override
    public void click() {
        this.value = !value;
        makeImage();
        if (owner != null)  owner.tell();
    }

    @Override
    public void tell() {

    }

    @Override
    public void setOwner(Button b) {
        owner = b;
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
