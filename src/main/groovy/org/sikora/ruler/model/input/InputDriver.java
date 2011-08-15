package org.sikora.ruler.model.input;

/**
 * Defines behavior of input data device.
 */
public interface InputDriver {
  /**
   * Issues action to the input device.
   *
   * @param action action to be issued
   */
  public void issue(final Action action);

  public void issue(final Action action, final Input input);

  public void issue(final Action action, final Hints hints);

  /**
   * Adds handler that would dispatch driver events.
   *
   * @param handler
   */
  public void addHandler(Handler handler);

  /**
   * Removes previously added handler.
   *
   * @param handler
   */
  public void removeHandler(Handler handler);

  /**
   * Value object encapsulating driver event. It would be passed to all
   * registered handlers after input action occurs.
   */
  public class Event {
    private final Action action;
    private final Input input;
    private final Hints.Item hint;

    /**
     * Creates new event from action, input and hint.
     *
     * @param action driver action
     * @param input  input value
     * @param hint   input hint
     */
    public Event(final Action action, final Input input, final Hints.Item hint) {
      this.action = action;
      this.input = input;
      this.hint = hint;
    }

    /**
     * Returns action that caused the event.
     *
     * @return command
     */
    public Action action() {
      return action;
    }

    /**
     * Returns driver input when the action was issued.
     *
     * @return input
     */
    public Input input() {
      return input;
    }

    /**
     * Returns hint that was selected when the action was issued.
     *
     * @return hint
     */
    public Hints.Item hint() {
      return hint;
    }

    @Override
    public String toString() {
      return String.format("Event{%s['%s' << '%s']}", action, input, hint);
    }
  }

  /**
   * Input driver actions.
   */
  public enum Action { // TODO rename Action => Action
    RESET_INPUT, FOCUS_INPUT, UPDATE_INPUT, UPDATE_HINTS, CANCEL, COMPLETE_INPUT, SUBMIT_INPUT, HIDE_INPUT
  }

  /**
   * Input driver handler. Callback interface used to dispatch driver events.
   */
  public interface Handler {
    /**
     * Method to be called for every input driver event.
     *
     * @param event
     */
    void dispatch(Event event);
  }

}
