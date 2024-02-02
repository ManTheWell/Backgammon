package backgammon;

import javax.swing.*;
import java.awt.image.BufferedImage;

public abstract class Screen extends JPanel {
    public abstract String getTitle();
    public abstract BufferedImage getIconImage();
}
