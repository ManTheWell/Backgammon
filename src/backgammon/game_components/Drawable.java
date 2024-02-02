package backgammon.game_components;

import java.awt.image.BufferedImage;

public interface Drawable {
    BufferedImage image();
    void update();
    int x();
    int y();
}
