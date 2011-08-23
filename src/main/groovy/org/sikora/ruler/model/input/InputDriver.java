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
  public void issue(final InputCommand command);

  /**
   * Adds listener that would dispatch driver events.
   *
   * @param listener
   */
  public void addListener(Listener listener);

  /**
   * Callback interface used to dispatch input driver events.
   */
  public interface Listener {
    /**
     * Method to be called for every input driver event.
     *
     * @param event
     */
    void dispatch(final InputEvent event);
  }


  /**
   * Input driver actions.
   */
  public enum Command {
    RESET, FOCUS, UPDATE, UPDATE_FROM_FIELD, HINT, HIDE, SUBMIT, COMPLETE, CANCEL
  }

  /**
   * Input driver events.
   */
  public enum Event {
    CHANGED, COMPLETE_ISSUED, SUBMIT_ISSUED, CANCEL_ISSUED
  }


  /**
   * Value object encapsulating input command.
   */
  public final class InputCommand {
    public static final int SELECT_NO_HINT = -1;
    private final Command command;
    private final Input input;
    private final Hints hints;
    private final int selectHint;

    /**
     * Creates new driver command.
     *
     * @param command
     * @return
     * @throws IllegalArgumentException when command is null
     */
    public static InputCommand of(final Command command) {
      return new InputCommand(command, null, null, SELECT_NO_HINT);
    }

    /**
     * Creates new driver command.
     *
     * @param command
     * @return
     * @throws IllegalArgumentException when command is null
     */
    public static InputCommand of(final Command command, final int selectHint) {
      return new InputCommand(command, null, null, selectHint);
    }

    /**
     * Creates new input command.
     *
     * @param command
     * @param input
     * @return
     * @throws IllegalArgumentException when command is null
     */
    public static InputCommand of(final Command command, final Input input) {
      return new InputCommand(command, input, null, SELECT_NO_HINT);
    }

    /**
     * Creates new input command.
     *
     * @param command
     * @param hints
     * @return
     * @throws IllegalArgumentException when command is null
     */
    public static InputCommand of(final Command command, final Hints hints) {
      return new InputCommand(command, null, hints, SELECT_NO_HINT);
    }

    /**
     * Creates new input command.
     *
     * @param command
     * @param input
     * @param hints
     * @return
     * @throws IllegalArgumentException when command is null
     */
    public static InputCommand of(final Command command, final Input input, final Hints hints) {
      return new InputCommand(command, input, hints, SELECT_NO_HINT);
    }

    private InputCommand(final Command command, final Input input, final Hints hints, final int selectHint) {
      if (command == null)
        throw new IllegalArgumentException("command cannot be null");
      this.command = command;
      this.input = input;
      this.hints = hints;
      this.selectHint = selectHint;
    }

    public Command command() {
      return command;
    }

    public boolean hasInput() {
      return input != null;
    }

    public Input input() {
      if (!hasInput())
        throw new IllegalStateException("this command does not have input");
      return input;
    }

    public boolean hasHints() {
      return hints != null;
    }

    public Hints hints() {
      if (!hasHints())
        throw new IllegalStateException("this command does not have hints");
      return hints;
    }

    public int selectHint() {
      return selectHint;
    }

    @Override
    public String toString() {
      final StringBuilder builder = new StringBuilder("InputCommand{");
      builder.append(command).append("[");
      if (input != null)
        builder.append("'").append(input()).append("'");
      if (hints != null)
        builder.append(" << ").append(hints.size()).append(" hint(s)");
      if (selectHint != SELECT_NO_HINT)
        builder.append(":").append(selectHint);
      return builder.append("]}").toString();
    }
  }

  /**
   * Value object encapsulating driver event. It would be passed to all
   * registered handlers after input command occurs.
   */
  public final class InputEvent {
    private final Event event;
    private final Input input;
    private final Hints.Item hint;

    /**
     * Creates new event from event, input and hint.
     *
     * @param event
     * @param input
     * @param hint
     * @throws IllegalArgumentException when event, command or hint is null
     */
    public InputEvent(final Event event, final Input input, final Hints.Item hint) {
      if (event == null)
        throw new IllegalArgumentException("event cannot be null");
      if (input == null)
        throw new IllegalArgumentException("input cannot be null");
      if (hint == null)
        throw new IllegalArgumentException("hint cannot be null");
      this.event = event;
      this.input = input;
      this.hint = hint;
    }

    public Event event() {
      return event;
    }

    public Input input() {
      return input;
    }

    public Hints.Item hint() {
      return hint;
    }

    @Override
    public String toString() {
      return String.format("InputEvent{%s['%s' << '%s']}", event, input, hint);
    }
  }

}
