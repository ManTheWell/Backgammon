package backgammon.buttons;

public interface InputField extends Button {
    String value();
    void addToValue(String addition);
    void setValue(String value);
}
