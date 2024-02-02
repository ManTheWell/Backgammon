package backgammon.game_components;

import java.awt.*;

interface Clickable {
    Rectangle bounds();
    void click();
    void offClick();
}
