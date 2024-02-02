package backgammon.buttons;

import backgammon.settings.GameSettings;
import backgammon.settings.Setting;
import backgammon.settings.StrSetting;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class StringButton implements InputButton {
    Button owner;
    String displayText;

    int x;
    int y;
    int width;
    int index;
    int height;
    String var_name;

    StringField input;

    public StringButton(int x, int y, int width, int height, String dTxt, int index, String var_name, int max_length) {
        this.displayText = dTxt;

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.index = index;
        this.var_name = var_name;

        int ifWidth = width / 3;
        int ifHeight = (int) (height * 0.8);
        int ifX = x + width - ifWidth - 3;
        int ifY = y + (int) (height * 0.2);
        this.input = new StringField(ifX, ifY, ifWidth, ifHeight, (""), max_length, this);

        reset();
    }

    @Override
    public BufferedImage image() {
        BufferedImage image = new BufferedImage(width, height, 2);
        Graphics2D g = image.createGraphics();

        // name
        g.setFont(new Font("calibre", Font.BOLD, (int) (height * .8)));
        g.setColor(new Color(230, 230, 230));
        g.drawString(displayText, 3, ((height / 2) + ((2 * g.getFont().getSize()) / 5)));

        // click to reset
        int sw = g.getFontMetrics().stringWidth(displayText);
        g.setFont(new Font("calibre", Font.BOLD, (int) (height * .4)));
        g.drawString("  (click to reset to last save)", sw, (int) (height * 0.8));

        g.drawImage(input.image(), input.x() - x, input.y() - y, null);

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
        return new Rectangle(x, y, (width / 2), height);
    }

    @Override
    public void click() {
        reset();
    }

    @Override
    public void reset() {
        String str = "";
        if (var_name.equalsIgnoreCase("player_names"))
            str = GameSettings.playerNames[index];

        else
            System.out.println("Unable to find setting \"" + var_name + "\"");

        if (!str.equalsIgnoreCase(""))
            input.setValue(str);
    }

    @Override
    public void save() {
        if (var_name.equalsIgnoreCase("player_names"))
            GameSettings.setPlayerName(index, input.value);

        else
            System.out.println("Unable to find setting \"" + var_name + "\"");
    }

    @Override
    public void tell() { }

    @Override
    public LinkedList<InputField> getInputFields() {
        LinkedList<InputField> input_list = new LinkedList<>();
        input_list.add(input);
        return input_list;
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
        return new StrSetting(var_name, input.value);
    }
}
