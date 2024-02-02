package backgammon.settings;

import java.awt.*;

public record SettingTemplates() {
    public static Setting[] easy = new Setting[]{
            new BoolSetting("Double Click", true),
            new BoolSetting("Show Destination Spaces", true),
            new BoolSetting("Show Movable Pieces", true)
    };

    public static Setting[] medium = new Setting[]{
            new BoolSetting("Double Click", true),
            new BoolSetting("Show Destination Spaces", true),
            new BoolSetting("Show Movable Pieces", false)
    };

    public static Setting[] hard = new Setting[]{
            new BoolSetting("Double Click", false),
            new BoolSetting("Show Destination Spaces", false),
            new BoolSetting("Show Movable Pieces", false)
    };

    public static Setting[] standardPlayerColors = new Setting[]{
            new ColorSetting("player_colors_1", new Color(80, 10, 10)),
            new ColorSetting("player_colors_2", new Color(150, 150, 150)),
    };

    public static Setting[] blackAndWhitePlayerColors = new Setting[]{
            new ColorSetting("player_colors_1", new Color(20, 20, 20)),
            new ColorSetting("player_colors_2", new Color(240, 240, 240)),
    };
}
