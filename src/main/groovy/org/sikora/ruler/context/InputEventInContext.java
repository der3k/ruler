package org.sikora.ruler.context;

import org.sikora.ruler.model.input.Hints;
import org.sikora.ruler.model.input.Input;
import org.sikora.ruler.model.input.InputDriver;
import org.sikora.ruler.ui.awt.AwtResultWindow;

/**
 * Value object encapsulating input driver event and context.
 */
public final class InputEventInContext implements Context {
  private final InputDriver.Event event;
  private final Context context;

  public InputEventInContext(final InputDriver.Event event, final Context context) {
    this.event = event;
    this.context = context;
  }

  public InputDriver.Command command() {
    return event.command();
  }

  public Input input() {
    return event.input();
  }

  public Hints.Item hint() {
    return event.hint();
  }

  public InputDriver inputDriver() {
    return context.inputDriver();
  }

  public AwtResultWindow resultWindow() {
    return context.resultWindow();
  }

  public Window foregroundWindow() {
    return context.foregroundWindow();
  }
}
