package backgammon.buttons;

import backgammon.settings.BoolSetting;
import backgammon.settings.ColorSetting;
import backgammon.settings.GameSettings;
import backgammon.settings.Setting;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.LinkedList;

public class ColorButton implements InputButton {
    String displayText;
    Button owner;

    int x;
    int y;
    int width;
    int height;
    String var_name;

    IntField[] inputs;

    public ColorButton(int x, int y, int width, int height, String dTxt, String var_name) {
        this.displayText = dTxt;

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.var_name = var_name;

        int ifWidth = width / 20;
        int ifHeight = (int) (height * 0.8);
        int ifBuffer = ifWidth + (width / 16);
        int ifX = width - ifWidth - 3;
        int ifY = y + (int) (height * 0.2);
        inputs = new IntField[]{
                new IntField((x + (ifX - (2 * ifBuffer))), ifY, ifWidth, ifHeight, (0), (255)),
                new IntField((x + (ifX - ifBuffer)), ifY, ifWidth, ifHeight, (0), (255)),
                new IntField((x + ifX), ifY, ifWidth, ifHeight, (0), (255)),
        };

        for (InputField in : inputs)
            in.setOwner(this);

        reset();
    }

    @Override
    public BufferedImage image() {
        BufferedImage image = new BufferedImage(width, height, 2);
        Graphics2D g = image.createGraphics();

        // fill with color currently shown
        Color color = new Color(inputs[0].value, inputs[1].value, inputs[2].value);
        g.setColor(color);
        g.fillRect(1, 2, width - 2, height);

        // name
        g.setFont(new Font("calibre", Font.BOLD, (int) (height * .8)));
        g.setColor(new Color(230, 230, 230));
        if (inputs[0].value > 200 || inputs[1].value > 200)  // || inputs[2].value > 200)
            g.setColor(new Color(10, 10, 10));
        g.drawString(displayText, 3, ((height / 2) + ((2 * g.getFont().getSize()) / 5)));

        // click to reset
        int sw = g.getFontMetrics().stringWidth(displayText);
        g.setFont(new Font("calibre", Font.BOLD, (int) (height * .4)));
        g.drawString("  (click to reset to last save)", sw, (int) (height * 0.8));

        // draw input fields
        g.setFont(new Font("calibre", Font.BOLD, (int) (height * .7)));
        String[] colors = new String[]{"r: ", "g: ", "b: "};
        for (int i = 0;i < 3;i++) {
            IntField in = inputs[i];
            g.drawImage(in.image(), in.x() - x, in.y() - y, null);
            sw = g.getFontMetrics().stringWidth(colors[i]);
            g.drawString(colors[i], in.x() - x - sw, ((height / 2) + ((2 * g.getFont().getSize()) / 5)));
        }

        g.dispose();
        return image;
    }

    @Override
    public void reset() {
        Color color = null;
        if (var_name.equalsIgnoreCase("player_colors_1"))
            color = GameSettings.playerColors[1];
        else if (var_name.equalsIgnoreCase("player_colors_2"))
            color = GameSettings.playerColors[2];

        else
            System.out.println("Unable to find setting \"" + var_name + "\"");

        if (color == null) return;

        inputs[0].setValue(color.getRed() + "");
        inputs[1].setValue(color.getGreen() + "");
        inputs[2].setValue(color.getBlue() + "");
    }

    public void setColor(Color color) {
        inputs[0].setValue(color.getRed() + "");
        inputs[1].setValue(color.getGreen() + "");
        inputs[2].setValue(color.getBlue() + "");
    }

    @Override
    public void click() {
        reset();
    }

    @Override
    public void save() {
        Color newColor = new Color(inputs[0].int_value(), inputs[1].int_value(), inputs[2].int_value());

        if (var_name.equalsIgnoreCase("player_colors_1"))
            GameSettings.setPlayerColor(1, newColor);
        else if (var_name.equalsIgnoreCase("player_colors_2"))
            GameSettings.setPlayerColor(2, newColor);

        else
            System.out.println("Unable to find setting \"" + var_name + "\"");
    }

    @Override
    public LinkedList<InputField> getInputFields() {
        return new LinkedList<>(Arrays.stream(inputs).toList());
    }

    public Rectangle bounds() {
        return new Rectangle(x, y, (width / 2), height);
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
    public void tell() {
        if (owner != null) owner.tell();
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
        return new ColorSetting(var_name, new Color(inputs[0].value, inputs[1].value, inputs[2].value));
    }
}
