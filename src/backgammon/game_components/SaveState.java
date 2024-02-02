package backgammon.game_components;

import java.util.LinkedList;

record SaveState(Bar[] bars, BoardSpace[] spaces, EndSpace[] ends, LinkedList<Dice> die,
                 LinkedList<Undo> undos, LinkedList<Drawable> drawables)
{}
