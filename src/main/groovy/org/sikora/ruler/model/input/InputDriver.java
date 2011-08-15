package org.sikora.ruler.model.input;

/**
 * Defines behavior of input data device.
 */
public interface InputDriver {
  /**
   * Issues command to the input device.
   *
   * @param command command to be issued
   */
  public void issue(final Command command);
  public void issue(final Command command, final Input input);
  public void issue(final Command command, final Hints hints);

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
   * registered handlers after input command occurs.
   */
  public class Event {
    private final Command command;
    private final Input input;
    private final Hints.Item hint;

    /**
     * Creates new event from command, input and hint.
     *
     * @param command driver command
     * @param input   input value
     * @param hint    input hint
     */
    public Event(final Command command, final Input input, final Hints.Item hint) {
      this.command = command;
      this.input = input;
      this.hint = hint;
    }

    /**
     * Returns command that caused the event.
     *
     * @return command
     */
    public Command command() {
      return command;
    }

    /**
     * Returns driver input when the command was issued.
     *
     * @return input
     */
    public Input input() {
      return input;
    }

    /**
     * Returns hint that was selected when the command was issued.
     *
     * @return hint
     */
    public Hints.Item hint() {
      return hint;
    }

    @Override
    public String toString() {
      return String.format("Event{%s['%s' << '%s']}", command, input, hint);
    }
  }

  /**
   * Input driver commands.
   */
  public enum Command {
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
