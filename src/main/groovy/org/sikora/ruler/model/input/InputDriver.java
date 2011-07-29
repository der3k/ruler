package org.sikora.ruler.model.input;

/**
 * User: sikorric
 * Date: 12.7.11
 * Time: 12:49
 */

public interface InputDriver {
  public void set(Input input);

  public void set(Hints hints);

  public void set(Command command);

  public void addHandler(Handler listener);

  public void removeHandler(Handler listener);

  public class Event {
    private final InputDriver driver;
    private final InputCommand command;

    public Event(final InputDriver driver, final InputCommand command) {
      this.driver = driver;
      this.command = command;
    }

    public InputDriver driver() {
      return driver;
    }

    public Command command() {
      return command.command();
    }

    public Input input() {
      return command.input();
    }

    public Hints.Item hint() {
      return command.hint();
    }

    @Override
    public String toString() {
      return String.format("Event{%s}", command);
    }
  }

  public class InputCommand {
    private final Command command;
    private final Input input;
    private final Hints.Item hint;

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

  public enum Command {
    RESET_INPUT, FOCUS_INPUT, UPDATE_INPUT, UPDATE_HINTS, CANCEL, COMPLETE_INPUT, SUBMIT_INPUT, HIDE_INPUT
  }

  public interface Handler {
    void dispatch(Event event);
  }

}
