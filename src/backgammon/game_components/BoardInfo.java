package backgammon.game_components;

import backgammon.ScreenSizes;

import java.awt.*;

record BoardInfo() {
    static int SCREEN_WIDTH = ScreenSizes.GAME_BOARD_SCREEN_WIDTH;
    static int SCREEN_HEIGHT = ScreenSizes.GAME_BOARD_SCREEN_HEIGHT;
    static int SPACE_WIDTH = SCREEN_WIDTH / 15;
    static int SPACE_HEIGHT = SCREEN_HEIGHT / 3;

    static int START_X = ((SCREEN_WIDTH / 2) - (int) (SPACE_WIDTH * 6.5));
    static int MIDDLE_X = (int) (START_X + (6.5 * SPACE_WIDTH));
    static int START_Y = (SCREEN_HEIGHT / 12);
    static int BOTTOM_Y = SCREEN_HEIGHT - (SCREEN_HEIGHT / 10);
    static int MIDDLE_Y = START_Y + ((BOTTOM_Y - START_Y) / 2);
    static int STROKE = SCREEN_WIDTH / 125;

    static int UNDO_Y = START_Y + STROKE;
    static int DICE_Y = UNDO_Y + (SPACE_WIDTH / 4);


    public static Color background_color = new Color(255, 190, 150);
    public static Color dark_background_color = new Color(148, 114, 93);
    public static Color wood_color = new Color(75, 45, 0);

    public static Color[] base_colors = {
            null,
            new Color(100, 50, 10),
            new Color(200, 120, 50)
    };
}
