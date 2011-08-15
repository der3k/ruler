package org.sikora.ruler.context;

import org.sikora.ruler.model.input.Hints;
import org.sikora.ruler.model.input.Input;
import org.sikora.ruler.model.input.InputDriver;

/**
 * Value object encapsulating input driver event and context.
 */
public final class InputEventInContext {
  private final InputDriver.Event event;
  private final Context context;

  /**
   * Create new event in context.
   *
   * @param event
   * @param context
   */
  public InputEventInContext(final InputDriver.Event event, final Context context) {
    this.event = event;
    this.context = context;
  }

  /**
   * Returns event command.
   *
   * @return command
   */
  public InputDriver.Command command() {
    return event.command();
  }

  /**
   * Returns event input.
   *
   * @return input
   */
  public Input input() {
    return event.input();
  }

  /**
   * Returns input hint.
   *
   * @return hint
   */
  public Hints.Item hint() {
    return event.hint();
  }

  /**
   * Returns input context.
   *
   * @return context
   */
  public Context context() {
    return context;
  }
}
