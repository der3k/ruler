package org.sikora.ruler.model.input;

/**
 * Defines behavior of input data device.
 */
public interface InputDriver {
  /**
   * Sets input of the device.
   *
   * @param input new input value
   */
  public void set(Input input);

  /**
   * Sets hints of the input device.
   *
   * @param hints new hints
   */
  public void set(Hints hints);

  /**
   * Issues command to the input device.
   *
   * @param command command to be issued
   */
  public void issue(Command command);

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
    private final InputDriver driver;
    private final InputCommand command;

    /**
     * Creates new event for driver with given input command.
     *
     * @param driver
     * @param command
     */
    public Event(final InputDriver driver, final InputCommand command) {
      this.driver = driver;
      this.command = command;
    }

    /**
     * Returns originating input driver.
     *
     * @return driver
     */
    public InputDriver driver() {
      return driver;
    }

    /**
     * Returns command that caused the event.
     *
     * @return command
     */
    public Command command() {
      return command.command();
    }

    /**
     * Returns driver input when the command was issued.
     *
     * @return input
     */
    public Input input() {
      return command.input();
    }

    /**
     * Returns hint that was selected when the command was issued.
     *
     * @return hint
     */
    public Hints.Item hint() {
      return command.hint();
    }

    @Override
    public String toString() {
      return String.format("Event{%s}", command);
    }
  }

  /**
   * Value object encapsulating driver state and command.
   */
  public class InputCommand {
    private final Command command;
    private final Input input;
    private final Hints.Item hint;

    /**
     * Creates new input command from actual command, input and hint.
     *
     * @param command
     * @param input
     * @param hint
     */
    public InputCommand(final Command command, final Input input, final Hints.Item hint) {
      this.command = command;
      this.input = input;
      this.hint = hint;
    }

    public Command command() {
      return command;
    }

    public Input input() {
      return input;
    }

    public Hints.Item hint() {
      return hint;
    }

    @Override
    public String toString() {
      return String.format("%s['%s' << '%s']", command, input, hint);
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
