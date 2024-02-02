package backgammon.game_components;

public abstract class Space {
    public abstract int color();
    public abstract void setDice(Dice dice);
    public abstract void addPiece(int color);
}
