package backgammon.settings;

import backgammon.GameFrame;
import backgammon.Screen;
import backgammon.ScreenSizes;
import backgammon.buttons.*;
import backgammon.buttons.Button;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

public class AdvancedSettingScreen extends Screen {
    BufferedImage image;
    GameFrame game_frame;
    InputField target = null;

    int num_settings = GameSettings.number_of_settings;

    public boolean saved = true;

    int width = ScreenSizes.SETTING_SCREEN_WIDTH;
    int height = ScreenSizes.SETTING_SCREEN_HEIGHT;

    int startY = height / 15;
    int startX = width / 20;
    int button_width = (width - (2 * startX)) - 4;
    int drawH = (height - (2 * startY));
    int button_height = (int) (drawH / (1.2 * Math.max(num_settings, 15)));
    int buffer = button_height / 5;

    ClickButton done;
    ClickButton save;
    LinkedList<Button> buttons = new LinkedList<>();
    LinkedList<InputField> inputs = new LinkedList<>();

    public AdvancedSettingScreen(GameFrame game_frame) {
        this.game_frame = game_frame;

        // set up screen
        this.setPreferredSize(new Dimension(width, height));
        this.setFocusable(true);

        setImage();
        setup();

        this.addMouseListener(new MyMouseAdapter());
        this.addKeyListener(new MyKeyAdapter());

        Timer game_timer = new Timer();
        game_timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                repaint();
            }
        }, 0, 30);
    }

    private void setImage() {
        BufferedImage temp_image = new BufferedImage(width, height, 1);
        Graphics2D g = (Graphics2D) temp_image.getGraphics();

        // background gray
        g.setColor(new Color(70, 70, 70));
        g.fillRect(0, 0, width, height);

        Color black = new Color(10, 10, 10);
        // settings text
        g.setFont(new Font("calibre", Font.BOLD, (int) (startY * 0.75)));
        g.setColor(black);
        g.drawString("SETTINGS:", startX - 2, ((startY / 2) + ((2 * g.getFont().getSize()) / 5)));
        // dividing line
        g.fillRect(startX - 2, startY - (height / 200) - 2, (width - (2 * startX)), (height / 200));

        // add background for each setting
        for (int i = 0;i < Math.max(15, num_settings);i++) {
            int y = startY + (i * button_height) + (i * buffer);
            g.setColor(new Color(110, 110, 110));
            g.fillRect(startX - 2, y, button_width + 4, button_height + (buffer / 2));
        }

        g.dispose();
        image = temp_image;
    }

    private void setup() {
        // toggle settings
        String[] toggle_settings = GameSettings.toggleSettings;
        int y = startY + button_height + buffer;

        Button[] dButtons = new Button[toggle_settings.length];
        for (int i = 0;i < toggle_settings.length;i++) {
            Button b = new ToggleButton(startX, y, button_width, button_height, toggle_settings[i]);
            buttons.add(b);
            dButtons[i] = b;
            y += button_height + buffer;
        }

        // toggle slide button
        String[] options = new String[]{"easy", "medium", "hard", "custom"};
        SlideButton toggleSB;
        toggleSB = new SlideButton(startX, startY, button_width, button_height, ("Difficulty"), options, (1), (dButtons));
        buttons.add(toggleSB);
        for (Button b : dButtons)
            b.setOwner(toggleSB);

        // player names
        StringButton sb;
        sb = new StringButton(startX, y, button_width, button_height, ("Player 1 Name: "), (1), ("player_names"), (15));
        buttons.add(sb);
        inputs.addAll(sb.getInputFields());
        y += buffer + button_height;

        sb = new StringButton(startX, y, button_width, button_height, ("Player 2 Name: "), (2), ("player_names"), (15));
        buttons.add(sb);
        inputs.addAll(sb.getInputFields());
        y += buffer + button_height;

        // player color buttons
        int togglePCBy = y;
        y += buffer + button_height;
        ColorButton[] pcButtons = new ColorButton[2];
        // color buttons
        ColorButton cb;
        cb = new ColorButton(startX, y, button_width, button_height, ("Player 1 Color: "), ("player_colors_1"));
        buttons.add(cb);
        inputs.addAll(cb.getInputFields());
        pcButtons[0] = cb;
        y += buffer + button_height;

        cb = new ColorButton(startX, y, button_width, button_height, ("Player 2 Color: "), ("player_colors_2"));
        buttons.add(cb);
        inputs.addAll(cb.getInputFields());
        pcButtons[1] = cb;

        SlideButton pcSB;
        options = new String[]{"standard player colors", "black and white", "custom"};
        pcSB = new SlideButton(startX, togglePCBy, button_width, button_height, ("Player Colors"), options, (0), pcButtons);
        buttons.add(pcSB);
        for (Button b : pcButtons)
            b.setOwner(pcSB);

        buttons.addAll(inputs);

        // button ints
        int h = (int) (startY * 0.8);
        int w = (h * 3);
        Font font = new Font("calibre", Font.BOLD, (int) (startY * 0.6));
        int doneX = width - startX - w;
        int saveX = width - startX - (int) (2.25 * w);
        int fixX = width - startX - (int) (3.5 * w);
        y = height - startY + (int) (startY * 0.1);
        // buttons
        buttons.add(new ClickButton(fixX, y, w, h, font, "FIX", "fix settings", true, true, false));
        save = new ClickButton(saveX, y, w, h, font, "SAVE", (true), (false), (false));
        done = new ClickButton(doneX, y, w, h, font, "DONE");
    }

    private void save() {
        for (Button b : buttons)
            b.save();
        saved = true;
        save.setValue(false);
        setImage();
    }

    private void reset() {
        for (Button b : buttons)
            b.reset();
    }

    private void done() {
        reset();
        game_frame.setScreen("board");
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(image, 0, 0, null);

        g.drawImage(save.image(), save.x(), save.y(), null);
        g.drawImage(done.image(), done.x(), done.y(), null);

        for (Button b : buttons)
            g.drawImage(b.image(), b.x(), b.y(), null);

        for (InputField in : inputs) {
            g.setColor(Color.BLACK);
            if (in == target) g.setColor(Color.RED);
            g.drawRect(in.bounds().x, in.bounds().y, in.bounds().width, in.bounds().height);
        }

        Toolkit.getDefaultToolkit().sync();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyReleased(KeyEvent e) {
            if (isShowing() && target != null)
                target.addToValue(e.getKeyChar() + "");
        }
    }

    public class MyMouseAdapter extends MouseAdapter {
        @Override
        public void mouseReleased(MouseEvent e) {
            Rectangle m = new Rectangle(e.getX() - 2, e.getY() - 2, 4, 4);

            target = null;

            if (m.intersects(save.bounds()))
                save();

            if (m.intersects(done.bounds()))
                done();

            for (Button b : buttons) {
                if (m.intersects(b.bounds())) {
                    saved = false;
                    b.click();
                    if (b instanceof InputField)
                        target = (InputField) b;
                }
            }

            save.setValue(!saved);
        }
    }

    @Override
    public String getTitle() {
        return "Backgammon Settings";
    }

    @Override
    public BufferedImage getIconImage() {
        BufferedImage image = new BufferedImage(ScreenSizes.ICON_SIZE, ScreenSizes.ICON_SIZE, 2);
        Graphics2D g = (Graphics2D) image.getGraphics();

        g.setStroke(new BasicStroke(3));
        g.setColor(new Color(10, 10, 10));
        g.drawOval(3, 3, 25, 25);

        g.setFont(new Font("calibre", Font.BOLD, 18));
        g.drawString("S", 10, 22);

        g.dispose();
        return image;
    }
}
