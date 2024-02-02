package backgammon.buttons;

import backgammon.settings.BoolSetting;
import backgammon.settings.ColorSetting;
import backgammon.settings.Setting;
import backgammon.settings.SettingTemplates;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SlideButton implements Button {
    String displayText;
    BufferedImage image;

    int x;
    int y;
    int width;
    int height;
    int option;
    Button owner;
    int og_option;

    String[] options;
    Button[] targets;
    Setting[] custom;

    public SlideButton(int x, int y, int w, int h, String dTxt, String[] options, int startOption, Button[] targets) {
        this.displayText = dTxt;

        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
        this.option = startOption;
        this.og_option = startOption;

        this.options = options;
        this.targets = targets;

        setImage();
        saveCustom();
    }

    @Override
    public BufferedImage image() {
        return image;
    }

    private void setImage() {
        BufferedImage t_image = new BufferedImage(width, height, 2);
        Graphics2D g = t_image.createGraphics();

        // display text
        g.setFont(new Font("calibre", Font.BOLD, (int) (height * .8)));
        g.setColor(new Color(230, 230, 230));
        int sw = g.getFontMetrics().stringWidth(displayText);
        int sx = Math.max((sw / 2), ((width) / 5)) - (sw / 2) - 3;
        g.drawString(displayText, sx, ((height / 2) + ((2 * g.getFont().getSize()) / 5)));

        // click to reset;
        String str = "< " + options[option] + " >";
        sw = g.getFontMetrics().stringWidth(str);
        sx = ((4 * width) / 5)  - (sw / 2) - 3;
        g.drawString(str, sx, ((height / 2) + ((2 * g.getFont().getSize()) / 5)));

        g.dispose();
        image = t_image;
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
        return new Rectangle(x + (width / 2), y, (width / 2), height);
    }

    @Override
    public void click() {
        if (options[option].equalsIgnoreCase("custom"))
            saveCustom();

        option = (option + 1) % options.length;
        setImage();
        updateChildren();
    }

    private void saveCustom() {
        custom = new Setting[targets.length];
        for (int i = 0;i < targets.length;i++)
            custom[i] = (targets[i].getSetting());
    }

    private void updateChildren() {
        String selectedOption = options[option];
        Setting[] settings = null;

        if (selectedOption.equalsIgnoreCase("custom"))
            settings = custom;
        else if (selectedOption.equalsIgnoreCase("easy"))
            settings = SettingTemplates.easy;
        else if (selectedOption.equalsIgnoreCase("medium"))
            settings = SettingTemplates.medium;
        else if (selectedOption.equalsIgnoreCase("hard"))
            settings = SettingTemplates.hard;
        else if (selectedOption.equalsIgnoreCase("standard player colors"))
            settings = SettingTemplates.standardPlayerColors;
        else if (selectedOption.equalsIgnoreCase("black and white"))
            settings = SettingTemplates.blackAndWhitePlayerColors;

        else
            System.out.println("Did not find template \"" + selectedOption + "\"");

        if (settings != null)
            set(settings);
    }

    private void set(Setting[] settings) {
        for (Setting setting : settings) {
            for (Button button : targets) {
                if (button.getAssociatedSetting().equalsIgnoreCase(setting.settingName())) {
                    if (button instanceof ToggleButton && setting instanceof BoolSetting)
                        ((ToggleButton) button).setValue(((BoolSetting) setting).defValue());

                    if (button instanceof ColorButton && setting instanceof ColorSetting)
                        ((ColorButton) button).setColor(((ColorSetting) setting).defValue());
                }
            }
        }
    }

    @Override
    public void reset() {
        option = og_option;
        setImage();
    }

    @Override
    public void tell() {
        for (int i = 0;i < options.length;i++)
            if (options[i].equalsIgnoreCase("custom"))
                option = i;
        setImage();
    }

    @Override
    public void setOwner(Button b) {
        owner = b;
    }

    @Override
    public void save() {
        og_option = option;
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
