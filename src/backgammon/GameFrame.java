package backgammon;

import backgammon.settings.*;
import backgammon.game_components.*;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    GameBoardScreen game_board = new GameBoardScreen(this);
    SettingScreen settings_screen = new SettingScreen(this);

    Screen[] screens = new Screen[]{game_board, settings_screen};

    public static void main(String[] args) {
        new GameFrame();
    }

    public GameFrame() {
        for (Screen s : screens)
            this.add(s);

        setScreen("board");

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        showOnScreen(0, this);
        this.setVisible(true);
    }

    /**
     * Options:
     * board, settings
     * @param screen_name: string of the desired screen
     */
    public void setScreen(String screen_name) {
        Screen screen = null;
        if (screen_name.equalsIgnoreCase("board"))
            screen = game_board;
        if (screen_name.equalsIgnoreCase("settings")) {
            screen = settings_screen;
        }

        if (screen == null) {
            System.out.println("UNABLE TO LOCATE SCREEN \"" + screen_name + "\"");
            return;
        }

        this.setContentPane(screen);
        screen.setVisible(true);
        screen.requestFocus();

        this.setTitle(screen.getTitle());
        this.setIconImage(screen.getIconImage());
        this.pack();

        showOnScreen(0, this);
    }

    public void showOnScreen(int screen, JFrame frame ) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gd = ge.getScreenDevices();

        if (gd.length == 1)
            screen = 0;

        if (screen >= 0 && screen < gd.length) {
            int x = gd[screen].getDefaultConfiguration().getBounds().x +
                    ((gd[screen].getDefaultConfiguration().getBounds().width - frame.getWidth()) / 2);

            int y = ((gd[screen].getDefaultConfiguration().getBounds().height - frame.getHeight()) / 2);

            frame.setLocation(x, y);
        }
        else {
            System.out.printf("screen %d not found...", screen);
            System.exit(1);
        }
    }
}
