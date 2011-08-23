package org.sikora.ruler.context;

import org.sikora.ruler.model.input.Hints;
import org.sikora.ruler.model.input.Input;
import org.sikora.ruler.model.input.InputDriver;
import org.sikora.ruler.ui.awt.AwtResultWindow;

/**
 * Value object encapsulating input driver inputEvent and context.
 */
public final class InputEventInContext implements Context {
  private final InputDriver.InputEvent inputEvent;
  private final Context context;

  public InputEventInContext(final InputDriver.InputEvent inputEvent, final Context context) {
    this.inputEvent = inputEvent;
    this.context = context;
  }

  public InputDriver.Event event() {
    return inputEvent.event();
  }

  public Input input() {
    return inputEvent.input();
  }

  public Hints.Item hint() {
    return inputEvent.hint();
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
