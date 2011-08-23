package org.sikora.ruler.ui.awt;

import org.sikora.ruler.model.input.Hints;
import org.sikora.ruler.model.input.Input;
import org.sikora.ruler.model.input.InputDriver;
import org.sikora.ruler.model.input.InputField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static org.sikora.ruler.model.input.InputDriver.Event.*;

/**
 * AWT implementation of the input driver. It utilizes KeyListener to access AWT key events.
 */
public class AwtInputDriver implements InputDriver {
  private static final Logger LOGGER = LoggerFactory.getLogger(AwtInputDriver.class);

  private final InputField inputField;
  private Input input = Input.EMPTY;
  private Hints hints = Hints.NONE;
  private final List<Listener> listeners = new ArrayList<Listener>();

  /**
   * Creates new input driver. It uses AwtInputWindow for capturing key events.
   * @param inputWindow
   */
  public AwtInputDriver(final AwtInputWindow inputWindow) {
    inputWindow.addKeyListener(new AwtInputKeyListener(this));
    inputField = inputWindow;
  }

  public void issue(final InputCommand command) {
    LOGGER.debug("Issuing {}", command);
    switch (command.command()) {
      case UPDATE_FROM_FIELD:
        updateInput(inputField.input());
        break;
      case UPDATE:
        inputField.set(command.input());
        updateInput(command.input());
        break;
      case HINT:
        inputField.set(command.hints());
        hints = command.hints();
        break;
      case COMPLETE:
        hints.select(command.selectHint());
        dispatchEvent(COMPLETE_ISSUED);
        break;
      case SUBMIT:
        hints.select(command.selectHint());
        dispatchEvent(SUBMIT_ISSUED);
        break;
      case CANCEL:
        dispatchEvent(CANCEL_ISSUED);
        break;
      case FOCUS:
        inputField.focus();
        break;
      case HIDE:
        inputField.hide();
        break;
      case RESET:
        inputField.set(Input.EMPTY);
        inputField.set(Hints.NONE);
        hints = Hints.NONE;
        input = Input.EMPTY;
        break;
    }
  }

  public void addListener(final Listener listener) {
    if (listener == null)
      throw new IllegalArgumentException("listener cannot be null");
    listeners.add(listener);
  }

  private void dispatchEvent(final Event event) {
    final InputEvent inputEvent = new InputEvent(event, input, hints.selected());
    LOGGER.debug("Dispatching {}", inputEvent);
    for (Listener listener : listeners)
      listener.dispatch(inputEvent);
  }

  private void updateInput(Input newInput) {
    final Input.Update update = input.updateTo(newInput);
    if (update.isNotVoid()) {
      LOGGER.trace("Input changed: {}", update);
      input = update.newValue();
      dispatchEvent(CHANGED);
    }
  }

}
