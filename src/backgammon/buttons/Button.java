package backgammon.buttons;

import backgammon.settings.Setting;

import java.awt.*;
import java.awt.image.BufferedImage;

public interface Button extends Drawable {
    @Override
    BufferedImage image();

    @Override
    int x();

    @Override
    int y();

    Rectangle bounds();

    void click();

    void tell();

    void setOwner(Button b);

    String getAssociatedSetting();

    Setting getSetting();

    void reset();

    void save();
}
