package backgammon.buttons;

import java.util.LinkedList;

public interface InputButton extends Button {
    LinkedList<InputField> getInputFields();
    void tell();
}
