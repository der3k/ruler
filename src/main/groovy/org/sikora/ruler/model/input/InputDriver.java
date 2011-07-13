package org.sikora.ruler.model.input;

/**
 * User: sikorric
 * Date: 12.7.11
 * Time: 12:49
 */

public interface InputDriver {
  public void setInput(Input input);

  public void setHints(Hints hints);

  public void addListener(Listener listener);

  public void removeListener(Listener listener);

  public class Update {
    private final Input.Update inputUpdate;
    private final Action action;

    public Update(Input.Update inputUpdate, Action action) {
      this.inputUpdate = inputUpdate;
      this.action = action;
    }

    public Input.Update inputUpdate() {
      return inputUpdate;
    }

    public Action action() {
      return action;
    }

    @Override
    public String toString() {
      return String.format("InputDriver.Update{%s, %s}", inputUpdate, action);
    }
  }

  public enum Action {
    RESET, UPDATE, HINTS_UPDATE, CANCEL, COMPLETE, SUBMIT
  }

  public interface Listener {
    void onChange(Update update);
  }

}
