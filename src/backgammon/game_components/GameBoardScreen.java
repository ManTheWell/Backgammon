package backgammon.game_components;

import backgammon.*;
import backgammon.settings.GameSettings;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.awt.image.BufferedImage;

public class GameBoardScreen extends Screen {
    final BufferedImage image;
    final GameFrame game_frame;

    final int REFRESH_TIMER = 30;

    int winner;
    int player;
    int selectedSpace;
    boolean waiting_on_turn;

    int click_time = 0;

    Win win;
    Done done;
    Bar[] bars;
    EndSpace[] endSpaces;
    BoardSpace[] boardSpaces;
    Rectangle settings_button;
    LinkedList<Dice> die = new LinkedList<>();
    LinkedList<Undo> undos = new LinkedList<>();
    LinkedList<Drawable> drawables = new LinkedList<>();

    public GameBoardScreen(GameFrame game_frame) {
        this.game_frame = game_frame;

        // set up screen
        this.setPreferredSize(new Dimension(BoardInfo.SCREEN_WIDTH, BoardInfo.SCREEN_HEIGHT));
        this.setFocusable(true);

        // set the image
        image = makeImage();
        repaint();

        // add listeners
        MyMouseAdapter mmA = new MyMouseAdapter();
        this.addMouseListener(mmA);
        this.addMouseMotionListener(mmA);

        setup();

        Timer game_timer = new Timer();
        game_timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                update();
            }
        }, 0, REFRESH_TIMER);
    }

    private BufferedImage makeImage() {
        BufferedImage tempImage = new BufferedImage(BoardInfo.SCREEN_WIDTH, BoardInfo.SCREEN_HEIGHT, 1);
        Graphics2D g = (Graphics2D) tempImage.getGraphics();

        // grey setting
        g.setColor(new Color(80, 80, 80));
        g.fillRect(0, 0, BoardInfo.SCREEN_WIDTH, BoardInfo.SCREEN_HEIGHT);


        int stroke = BoardInfo.STROKE;
        int boardHeight = BoardInfo.BOTTOM_Y - BoardInfo.START_Y;
        int space_width = BoardInfo.SPACE_WIDTH;
        int startX = BoardInfo.START_X;
        int startY = BoardInfo.START_Y;

        // border
        // start - end - stroke on either side
        int borderXStart = startX - (space_width / 2) - (2 * stroke);
        // size of board + end area + stroke
        int borderWidth = (space_width * 13) + (space_width / 2) + (3 * stroke);

        g.setColor(BoardInfo.wood_color);
        g.fillRect(borderXStart, (startY - stroke), borderWidth, (boardHeight + (2 * stroke)));

        // inner board color
        int innerBoardWidth = space_width * 6;
        int secondBoardX = startX + (space_width * 7);
        g.setColor(BoardInfo.background_color);

        g.fillRect(startX, startY, innerBoardWidth, boardHeight);
        g.fillRect(secondBoardX, startY, innerBoardWidth, boardHeight);

        // end piece area middle
        int squareEndSize = (boardHeight / 5) - (2 * stroke);
        g.setColor(BoardInfo.dark_background_color);
        g.fillRect(borderXStart + stroke, BoardInfo.MIDDLE_Y - (squareEndSize / 2), (space_width / 2), squareEndSize);

        g.dispose();
        return tempImage;
    }

    private void setup() {
        // reset all required game values
        winner = 0;
        selectedSpace = -1;
        waiting_on_turn = false;
        die = new LinkedList<>();
        undos = new LinkedList<>();
        drawables = new LinkedList<>();

        // get the required ints
        int width = BoardInfo.SPACE_WIDTH;
        int height = BoardInfo.SPACE_HEIGHT;
        int startX = BoardInfo.START_X;
        int middleX = BoardInfo.MIDDLE_X;
        int middleY = BoardInfo.MIDDLE_Y;
        int[] yValues = new int[]{BoardInfo.START_Y, BoardInfo.BOTTOM_Y};

        // create the first die
        firstDie();
        if (die.size() != 2) System.exit(1);
        int d1 = die.peek().value;
        int d2 = die.peekLast().value;

        // add the board spaces
        boardSpaces = new BoardSpace[]{
                null,

                new BoardSpace(startX, yValues, 1, 1, 2, width, height, (d1 > d2)),
                new BoardSpace(startX+(width), yValues, 2, 0, 0, width, height, (d1 > d2)),
                new BoardSpace(startX+(width*2), yValues, 1, 0, 0, width, height, (d1 > d2)),
                new BoardSpace(startX+(width*3), yValues, 2, 0, 0, width, height, (d1 > d2)),
                new BoardSpace(startX+(width*4), yValues, 1, 0, 0, width, height, (d1 > d2)),
                new BoardSpace(startX+(width * 5), yValues, 2, 2, 5, width, height, (d1 > d2)),

                new BoardSpace(startX+(width * 7), yValues, 1, 0, 0, width, height, (d1 > d2)),
                new BoardSpace(startX+(width * 8), yValues, 2, 1, 3, width, height, (d1 > d2)),
                new BoardSpace(startX+(width * 9), yValues, 1, 0, 0, width, height, (d1 > d2)),
                new BoardSpace(startX+(width * 10), yValues, 2, 0, 0, width, height, (d1 > d2)),
                new BoardSpace(startX+(width * 11), yValues, 1, 0, 0, width, height, (d1 > d2)),
                new BoardSpace(startX+(width * 12), yValues, 2, 2, 5, width, height, (d1 > d2)),

                new BoardSpace(startX+(width * 12), yValues, 1, 1, 5, width, height, (d1 < d2)),
                new BoardSpace(startX+(width * 11), yValues, 2, 0, 0, width, height, (d1 < d2)),
                new BoardSpace(startX+(width * 10), yValues, 1, 0, 0, width, height, (d1 < d2)),
                new BoardSpace(startX+(width * 9), yValues, 2, 0, 0, width, height, (d1 < d2)),
                new BoardSpace(startX+(width * 8), yValues, 1, 2, 3, width, height, (d1 < d2)),
                new BoardSpace(startX+(width * 7), yValues, 2, 0, 0, width, height, (d1 < d2)),

                new BoardSpace(startX+(width * 5), yValues, 1, 1, 5, width, height, (d1 < d2)),
                new BoardSpace(startX+(width * 4), yValues, 2, 0, 0, width, height, (d1 < d2)),
                new BoardSpace(startX+(width * 3), yValues, 1, 0, 0, width, height, (d1 < d2)),
                new BoardSpace(startX+(width * 2), yValues, 2, 0, 0, width, height, (d1 < d2)),
                new BoardSpace(startX+width, yValues, 1, 0, 0, width, height, (d1 < d2)),
                new BoardSpace(startX, yValues, 2, 2, 2, width, height, (d1 < d2)),
        };
        drawables.addAll(Arrays.asList(boardSpaces));

        // add the bars for both players
        bars = new Bar[]{
                null,

                new Bar(middleX, middleY, (height / 5), (width / 18), 1, (d1 < d2)),
                new Bar(middleX, middleY, (height / 5), (width / 18), 2, (d1 > d2)),
        };
        drawables.addAll(Arrays.asList(bars));

        // add the end spaces for both players
        int endX = startX - (width / 2) - BoardInfo.STROKE;
        endSpaces = new EndSpace[]{
                null,

                new EndSpace(endX, yValues, (width / 2), height, 1, (d1 < d2)),
                new EndSpace(endX, yValues, (width / 2), height, 2, (d1 > d2)),
        };
        drawables.addAll(Arrays.asList(endSpaces));

        // add the "done" button
        int done_width = (7 * width) / 8;
        done = new Done(middleX, (BoardInfo.MIDDLE_Y), done_width, (done_width / 2));

        // set the player value to the player going first
        if (d1 > d2) player = 1;
        else player = 2;
        // add the start icon with the correct player
        Start start = new Start(middleX, middleY, (width * 3), (width), player);
        drawables.add(start);

        drawables.removeAll(Collections.singleton(null));
    }

    private void update() {
        if (!this.isShowing()) {
            resetSpaces();
            return;
        }

        for (Drawable d : drawables)
            d.update();

        if (winner != 0) {
            win.update();
            repaint();
            return;
        }

        // check for a winner
        for (int i = 1; i < endSpaces.length; i++) {
            if (endSpaces[i] != null) {
                if (endSpaces[i].pieces == 15) {
                    // set the winner
                    winner = i;

                    // add the win+start over button
                    int space_width = BoardInfo.SPACE_WIDTH;
                    win = new Win(BoardInfo.MIDDLE_X, BoardInfo.MIDDLE_Y, (space_width * 4), (space_width * 2), winner);

                    return;
                }
            }
        }

        // check bar
        if (bars[player].numPieces > 0) {
            if (validMovesFromBar() <= 0)
                waiting_on_turn = true;
        }

        // check that player has remaining available moves
        int max = 0;
        for (int i = 1; i < boardSpaces.length; i++) {
            if (boardSpaces[i].color == player) {
                max = Math.max(max, validMoves(i, false).size());
                if (max > 0 && !GameSettings.showMovablePieces) break;
            }
        }
        if (max <= 0)
            waiting_on_turn = true;

        click_time++;

        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(image, 0, 0, null);

        // settings rect
        if (!GameSettings.fixedSettings)
            g.drawImage(makeSettingsButton(), settings_button.x, settings_button.y, null);

        for (Drawable d : drawables)
            g.drawImage(d.image(), d.x(), d.y(), null);

        if (waiting_on_turn)
            g.drawImage(done.image(), done.x(), done.y(), null);

        if (undos.size() > 0) {
            Undo undo = undos.peek();
            g.drawImage(undo.image(), undo.x(), undo.y(), null);
        }

        if (winner != 0)
            g.drawImage(win.image(), win.x(), win.y(), null);

        BufferedImage playerTurnStr = drawPlayerTurn();
        int x = (getWidth() / 2) - (playerTurnStr.getWidth() / 2);
        int y = (BoardInfo.START_Y / 2) - (playerTurnStr.getHeight() / 2);
        g.drawImage(playerTurnStr, x, y, null);

        Toolkit.getDefaultToolkit().sync();
    }

    private BufferedImage drawPlayerTurn() {
        Font font = new Font("calibre", Font.BOLD, (int) (0.4 * (BoardInfo.START_Y)));
        FontMetrics fontMetrics = getFontMetrics(font);

        String str = "It is " + GameSettings.playerNames[player] + "'s turn!";
        int width = fontMetrics.stringWidth(str) / 2;
        int height = (int) (0.5 * (BoardInfo.START_Y));
        int y = ((height / 2) + ((2 * font.getSize()) / 5));

        BufferedImage pImage = new BufferedImage((int) (width * 2.2), height, 2);
        Graphics2D g = pImage.createGraphics();

        g.setColor(new Color(127, 127, 127));
        g.fillRect(0, 0, (int) (width * 2.2), height);

        g.setFont(font);

        int x1 = (int) (width * 0.1);
        g.setColor(Color.BLACK);
        g.drawString("It is ", x1, y);

        int x2 = x1 + fontMetrics.stringWidth("It is ");
        g.setColor(GameSettings.playerColors[player]);
        g.drawString(GameSettings.playerNames[player], x2, y);

        int x3 = x2 + fontMetrics.stringWidth(GameSettings.playerNames[player]);
        g.setColor(Color.BLACK);
        g.drawString("'s turn!", x3, y);

        g.dispose();
        return pImage;
    }

    private BufferedImage makeSettingsButton() {
        int width = (int) (BoardInfo.SPACE_WIDTH * 1.25);
        int height = (BoardInfo.START_Y) / 2;
        int stroke = height / 6;

        int x = BoardInfo.MIDDLE_X + (int) (6.5 * BoardInfo.SPACE_WIDTH) + BoardInfo.STROKE - width;
        int by = BoardInfo.BOTTOM_Y;
        int y = by + ((BoardInfo.SCREEN_HEIGHT - by + BoardInfo.STROKE) / 2) - (height / 2);

        BufferedImage settings_temp_image = new BufferedImage(width, height, 1);
        Graphics2D g = (Graphics2D) settings_temp_image.getGraphics();

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, width, height);

        g.setColor(new Color(120, 120, 120));
        g.fillRect(stroke, stroke, width - (2 * stroke), height - (2 * stroke));

        g.setColor(new Color(30, 30, 30));
        g.setFont(new Font("calibre", Font.BOLD, (height / 2)));
        int sw = g.getFontMetrics().stringWidth("SETTINGS");
        g.drawString("SETTINGS", ((width / 2) - (sw / 2)), ((height / 2) + ((2 * g.getFont().getSize()) / 5)));

        g.dispose();

        settings_button = new Rectangle(x, y, width, height);

        return settings_temp_image;
    }

    public void firstDie() {
        drawables.removeAll(die);
        die.clear();

        int space_width = BoardInfo.SPACE_WIDTH;
        int middleX = BoardInfo.MIDDLE_X;

        int size = Math.min(((11 * BoardInfo.SPACE_WIDTH) / 24), (BoardInfo.SPACE_HEIGHT / 5));

        int x1 = middleX - (3 * space_width) - (int) (1.5 * size);
        int x2 = middleX + (3 * space_width) + (size / 2);

        int y = BoardInfo.MIDDLE_Y - (size / 2);

        int v1 = new Random().nextInt(6) + 1;
        int v2 = new Random().nextInt(6) + 1;

        while (v1 == v2) {
            v1 = new Random().nextInt(6) + 1;
            v2 = new Random().nextInt(6) + 1;
        }

        if (v1 > v2) {
            die.add(new Dice(x1, y, v1, size, 1));
            die.add(new Dice(x2, y, v2, size, 2));
        }
        else {
            die.add(new Dice(x1, y, v2, size, 1));
            die.add(new Dice(x2, y, v1, size, 2));
        }

        drawables.addAll(die);
    }

    private void newDie() {
        drawables.removeAll(die);
        die.clear();

        int size = Math.min(((11 * BoardInfo.SPACE_WIDTH) / 24), (BoardInfo.SPACE_HEIGHT / 5));
        int padding = size / 24;

        int x1 = BoardInfo.MIDDLE_X - size - padding;
        int x2 = BoardInfo.MIDDLE_X + padding;

        int y = BoardInfo.DICE_Y;
        int y2 = y + size + padding;

        int v1 = new Random().nextInt(6) + 1;
        int v2 = new Random().nextInt(6) + 1;

        // add the die
        die.add(new Dice(x1, y, v1, size, player));
        die.add(new Dice(x2, y, v2, size, player));

        // doubles
        if (die.size() >= 2 && v1 == v2) {
            die.add(new Dice(x1, y2, v1, size, player));
            die.add(new Dice(x2, y2, v2, size, player));
        }

        drawables.addAll(die);
    }

    private void turn() {
        if (player == 1) player = 2;
        else if (player == 2) player = 1;

        undos.clear();
        newDie();

        for (BoardSpace s : boardSpaces) if (s != null) s.down = !s.down;
        for (Bar b : bars) if (b != null) b.top = !b.top;
        for (EndSpace e : endSpaces) if (e != null) e.top = !e.top;
    }

    private LinkedList<Space> validMoves(int space_index, boolean show) {
        LinkedList<Space> valid_spaces = new LinkedList<>();
        if (allInHomeBase())
            return validMovesWithEnd(space_index, show);

        for (Dice d : die) {
            if (d.used) continue;
            int s = (player == 1) ? (space_index + d.value) : (space_index - d.value);
            if (s < 1 || s > 24) continue;
            if (boardSpaces[s].validMoveToSpace(player)) {
                if (show) {
                    boardSpaces[s].valid = true;
                    boardSpaces[s].setDice(d);
                }
                valid_spaces.add(boardSpaces[s]);
            }
        }

        if (GameSettings.showMovablePieces && valid_spaces.size() > 0 && !show && bars[player].numPieces <= 0)
            boardSpaces[space_index].canMoveFrom = true;

        return valid_spaces;
    }

    private int validMovesFromBar() {
        int validMoves = 0;
        for (Dice d : die) {
            if (d.used) continue;
            int s = (player == 1) ? d.value : (boardSpaces.length - d.value);
            if (s < 1 || s > 24) continue;
            if (boardSpaces[s].validMoveToSpace(player)) {
                boardSpaces[s].valid = true;
                boardSpaces[s].setDice(d);
                validMoves++;
            }
        }

        if (validMoves > 0)
            selectedSpace = -2;

        return validMoves;
    }

    private boolean allInHomeBase() {
        int pieces = 0;
        int start = (player == 1) ? (boardSpaces.length - 7) : 1;
        int end = start + 6;

        for (int i = start;i <= end;i++)
            pieces += boardSpaces[i].pieces;
        pieces += endSpaces[player].pieces;

        return pieces == 15;
    }

    private LinkedList<Space> validMovesWithEnd(int space, boolean show) {
        LinkedList<Space> valid_spaces = new LinkedList<>();
        int maxOccupiedDistance;

        if (player == 1) {
            maxOccupiedDistance = 24;
            for (int i = 24;i >= 19;i--)
                if (boardSpaces[i].color == player && boardSpaces[i].pieces > 0)
                    maxOccupiedDistance = Math.min(maxOccupiedDistance, i);
        }
        else {
            maxOccupiedDistance = 1;
            for (int i = 1;i <= 7;i++)
                if (boardSpaces[i].color == player && boardSpaces[i].pieces > 0)
                    maxOccupiedDistance = Math.max(maxOccupiedDistance, i);
        }

        for (Dice d : die) {
            if (d.used) continue;
            int s = (player == 1) ? (space + d.value) : (space - d.value);

            // piece lands on board
            if (s >= 1 && s <= (boardSpaces.length - 1) && boardSpaces[s].validMoveToSpace(player)) {
                if (show) {
                    boardSpaces[s].valid = true;
                    boardSpaces[s].setDice(d);
                }
                valid_spaces.addLast(boardSpaces[s]);
            }

            // player 1 piece in end
            else if (player == 1 && s == boardSpaces.length || (s > boardSpaces.length && space == maxOccupiedDistance)) {
                if (show) {
                    endSpaces[player].valid = true;
                    endSpaces[player].dice = d;
                }
                valid_spaces.addFirst(endSpaces[player]);
            }

            // player 2 piece may land in end
            else if (player == 2 && s == 0 || (s < 0 && space == maxOccupiedDistance)) {
                if (show) {
                    endSpaces[player].valid = true;
                    endSpaces[player].dice = d;
                }
                valid_spaces.addFirst(endSpaces[player]);
            }
        }

        return valid_spaces;
    }

    private void newUndoButton() {
        BoardSpace[] sps = new BoardSpace[boardSpaces.length];
        for (int i = 0; i < boardSpaces.length; i++)
            if (boardSpaces[i] != null)
                sps[i] = boardSpaces[i].copy();
        Bar[] b = new Bar[]{null, bars[1].copy(), bars[2].copy()};
        EndSpace[] e = new EndSpace[]{null, endSpaces[1].copy(), endSpaces[2].copy()};

        LinkedList<Dice> di = new LinkedList<>();
        for (Dice d : die) di.add(d.copy());

        LinkedList<Undo> u = new LinkedList<>(undos);
        LinkedList<Drawable> dr = new LinkedList<>(drawables);

        SaveState saveState = new SaveState(b, sps, e, di, u, dr);

        int w = (7 * BoardInfo.SPACE_WIDTH) / 8;
        undos.add(new Undo(BoardInfo.MIDDLE_X, (BoardInfo.UNDO_Y), w, w / 4, saveState));
    }

    private void restoreState(SaveState save) {
        die = save.die();
        bars = save.bars();
        undos = save.undos();
        endSpaces = save.ends();
        boardSpaces = save.spaces();

        drawables.clear();
        drawables.addAll(die);
        drawables.addAll(undos);
        for (Bar b : bars) if (b != null) drawables.add(b);
        for (EndSpace e : endSpaces) if (e != null) drawables.add(e);
        for (BoardSpace s : boardSpaces) if (s != null) drawables.add(s);

        repaint();
    }

    private void movePieceTo(Space to) {
        newUndoButton();

        if (to instanceof EndSpace)
            endSpaces[player].addPiece();

        if (to instanceof BoardSpace) {
            if (to.color() != 0 && to.color() != player) {
                bars[to.color()].numPieces++;
                ((BoardSpace) to).removePiece();
            }
            to.addPiece(player);
        }

        if (selectedSpace == -2)
            bars[player].numPieces--;
        else if (selectedSpace >= 1 && selectedSpace <= 24)
            boardSpaces[selectedSpace].removePiece();

        selectedSpace = -1;
    }

    private void resetSpaces() {
        // if nothing or invalid area was clicked, reset all the pieces
        for (BoardSpace s : boardSpaces)
            if (s != null) {
                s.offClick();
                s.setDice(null);
                s.valid = false;
                s.canMoveFrom = false;
            }

        for (EndSpace end : endSpaces)
            if (end != null) {
                end.dice = null;
                end.valid = false;
            }

        selectedSpace = -1;
    }

    public class MyMouseAdapter extends MouseAdapter {
        @Override
        public void mouseReleased(MouseEvent e) {
            Rectangle m = new Rectangle(e.getX() - 2, e.getY() - 2, 4, 4);

            if (settings_button != null && m.intersects(settings_button) && !GameSettings.fixedSettings)
                game_frame.setScreen("settings");

            // clicked the reset button on the win screen
            if (winner != 0 && m.intersects(win.bounds())) {
                setup();
                return;
            }

            // there is a winner: nothing else should be clickable
            if (winner != 0) return;

            // clicked on a undo button
            if (undos.size() > 0 && m.intersects(undos.peekLast().bounds())) {
                restoreState(undos.getLast().save);
                waiting_on_turn = false;
                return;
            }

            // clicked on done button
            if (waiting_on_turn && m.intersects(done.bounds())) {
                turn();
                waiting_on_turn = false;
                return;
            }

            // waiting on a turn to be confirmed: nothing else clickable
            if (waiting_on_turn) return;

            // registers a valid click, if did not click on a space resets all spaces
            boolean didClick = false;

            // clicked on an end space
            for (EndSpace end : endSpaces) {
                if (end == null) continue;

                if (selectedSpace == -1 || !m.intersects(end.bounds()) || !end.valid) {
                    end.dice = null;
                    continue;
                }

                movePieceTo(end);
            }

            // clicked on a space
            for (int space_index = 1; space_index < boardSpaces.length; space_index++) {
                BoardSpace space = boardSpaces[space_index];

                // clicked on a valid move to space
                if (selectedSpace != -1 && m.intersects(space.bounds()) && space.valid)
                    movePieceTo(space);

                    // clicked on a space to move from
                else if (m.intersects(space.bounds()) && space.color == player && bars[player].numPieces <= 0) {
                    if (space_index != selectedSpace)
                        resetSpaces();

                    LinkedList<Space> valid_spaces = validMoves(space_index, true);

                    if (valid_spaces.size() == 0) {
                        space.badClick();
                        continue;
                    }

                    // double-clicked
                    if (space.clicked && click_time < 10 && GameSettings.doubleClick) {
                        movePieceTo(valid_spaces.getFirst());
                        break;
                    }

                    didClick = true;
                    space.click();
                    selectedSpace = space_index;
                    click_time = 0;
                }

                else
                    boardSpaces[space_index].offClick();
            }

            if (!didClick)
                resetSpaces();
        }
    }

    @Override
    public String getTitle() {
        return "Backgammon";
    }

    @Override
    public BufferedImage getIconImage() {
        BufferedImage image = new BufferedImage(ScreenSizes.ICON_SIZE, ScreenSizes.ICON_SIZE, 2);
        Graphics2D g = (Graphics2D) image.getGraphics();

        g.setColor(Color.BLACK);
        g.fillOval(2, 2, 26, 26);

        g.setColor(GameSettings.playerColors[1]);
        g.fillArc(5, 5, 20, 20, 45, 180);

        g.setColor(GameSettings.playerColors[2]);
        g.fillArc(5, 5, 20, 20, 225, 180);

        g.dispose();
        return image;
    }
}
