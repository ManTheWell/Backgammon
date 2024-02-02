package backgammon.settings;

import java.awt.*;

public record GameSettings() {
    public static int number_of_settings = 5;

    public static boolean fixedSettings = false;
    public static void setFixedSettings(boolean value) {
        fixedSettings = value;
    }

    public static Color[] playerColors = { null,  new Color(80, 10, 10),  new Color(150, 150, 150) };
    public static void setPlayerColor(int player, Color color) {
        playerColors[player] = color;
    }
    public static void setPlayerColors(Color[] colors) {
        playerColors = new Color[]{
                null,
                colors[1],
                colors[2]
        };
    }

    public static String[] playerNames = { "",  "Player 1",  "Player 2" };
    public static void setPlayerName(int player, String name) {
        playerNames[player] = name;
    }
    public static void setPlayerNames(String[] names) {
        playerNames = new String[]{
                "",
                names[1],
                names[2],
        };
    }

    public static boolean doubleClick = true;
    public static void setDoubleClick(boolean value) {
        doubleClick = value;
    }

    public static boolean showMovablePieces = false;
    public static void setShowMovablePieces(boolean value) {
        showMovablePieces = value;
    }

    public static boolean showDestinationSpaces = true;
    public static void setShowDestinationSpaces(boolean value) {
        showDestinationSpaces = value;
    }

    public static final String[] toggleSettings = new String[] {
            "Double Click",
            "Show Destination Spaces",
            "Show Movable Pieces",
    };
}
