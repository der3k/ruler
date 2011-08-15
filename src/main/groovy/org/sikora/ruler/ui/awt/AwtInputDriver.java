package org.sikora.ruler.ui.awt;

import org.sikora.ruler.model.input.Hints;
import org.sikora.ruler.model.input.Input;
import org.sikora.ruler.model.input.InputDriver;
import org.sikora.ruler.model.input.InputField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import static java.awt.event.KeyEvent.*;
import static org.sikora.ruler.model.input.InputDriver.Command.*;

/**
 * AWT implementation of the input driver. It utilizes KeyListener to access AWT key events.
 */
public class AwtInputDriver implements KeyListener, InputDriver {
  private static final Logger LOGGER = LoggerFactory.getLogger(AwtInputDriver.class);
  private static final char COMPLETE_KEY_MIN = '1';
  private static final char COMPLETE_KEY_MAX = '9';

  private final InputField inputField;
  private Hints hints = Hints.NONE;
  private final List<Handler> handlers = new ArrayList<Handler>();
  private Input input = Input.EMPTY;

  /**
   * Creates new input driver. It uses AwtInputWindow for capturing key events.
   */
  public AwtInputDriver() {
    final AwtInputWindow inputWindow = new AwtInputWindow();
    inputWindow.addKeyListener(this);
    inputWindow.set(input);
    inputField = inputWindow;
  }

  public void issue(final Command command) {
    switch (command) {
      case FOCUS_INPUT:
        inputField.focus();
        break;
      case HIDE_INPUT:
        inputField.hide();
        break;
      case RESET_INPUT:
        input = Input.EMPTY;
        hints = Hints.NONE;
        inputField.set(input);
        inputField.set(hints);
        break;
    }
    dispatchEventFor(command);
  }

  public void issue(final Command command, final Input input) {
    inputField.set(input);
    Input.Update update = this.input.updateTo(input);
    propagateInputUpdate(update);
    if (UPDATE_INPUT != command)
      dispatchEventFor(command);
  }

  public void issue(final Command command, final Hints hints) {
    this.hints = hints;
    inputField.set(hints);
    dispatchEventFor(UPDATE_HINTS);
    if (UPDATE_HINTS != command)
      dispatchEventFor(command);
  }

  public void addHandler(final Handler handler) {
    if (handler == null)
      throw new IllegalArgumentException("listener cannot be null");
    handlers.add(handler);
  }

  public void removeHandler(final Handler handler) {
    if (handler == null)
      throw new IllegalArgumentException("listener to remove cannot be null");
    handlers.remove(handler);
  }

  /**
   * Consumes key event when complete key was typed (keys 1-9).
   *
   * @param event key event
   */
  public void keyTyped(final KeyEvent event) {
    if (isCompleteKey(event.getKeyChar()))
      event.consume();
  }

  /**
   * Dispatches driver events for functional keys (ESC, ENTER, TAB, 1-9).
   *
   * @param event key event
   */
  public void keyPressed(final KeyEvent event) {
    final int key = event.getKeyCode();
    LOGGER.trace("Key pressed: '{}'", eventKeys(event));
    if (isNotRecognizedEvent(event))
      return;
    switch (key) {
      case VK_ESCAPE:
        consumeKeyEventAndDispatchCommand(event, CANCEL);
        break;
      case VK_TAB:
      case VK_1:
      case VK_2:
      case VK_3:
      case VK_4:
      case VK_5:
      case VK_6:
      case VK_7:
      case VK_8:
      case VK_9:
        hints.select(hintIndexFromKey(key));
        consumeKeyEventAndDispatchCommand(event, COMPLETE_INPUT);
        break;
      case VK_ENTER:
        consumeKeyEventAndDispatchCommand(event, SUBMIT_INPUT);
        break;
    }
  }

  /**
   * Recognizes and propagates input updates.
   *
   * @param event key event
   */
  public void keyReleased(KeyEvent event) {
    Input.Update update = input.updateTo(inputField.input());
    propagateInputUpdate(update);
  }

  private void consumeKeyEventAndDispatchCommand(final KeyEvent event, final Command command) {
    event.consume();
    dispatchEventFor(command);
  }

  private void dispatchEventFor(final Command command) {
    final Event event = eventFor(command);
    LOGGER.debug("Dispatching {}", event);
    for (Handler handler : handlers)
      handler.dispatch(event);
  }

  private void propagateInputUpdate(Input.Update update) {
    if (update.isNotVoid()) {
      LOGGER.trace("Input changed: {}", update);
      input = update.newValue();
      dispatchEventFor(UPDATE_INPUT);
    }
  }

  private Event eventFor(Command command) {
    return new Event(command, input, hints.selected());
  }

  private boolean isCompleteKey(final char key) {
    return COMPLETE_KEY_MIN <= key && key <= COMPLETE_KEY_MAX;
  }

  private int hintIndexFromKey(final int key) {
    if (VK_TAB == key)
      return 0;
    if (VK_1 <= key && key <= VK_9)
      return key - VK_1;
    throw new IllegalArgumentException("unsupported complete key: " + getKeyText(key));
  }

  private boolean isNotRecognizedEvent(KeyEvent event) {
    return event.isShiftDown()
        || event.isAltDown()
        || event.isAltGraphDown()
        || event.isControlDown()
        || event.isMetaDown()
        || event.isConsumed();
  }

  private String eventKeys(final KeyEvent event) {
    final StringBuilder builder = new StringBuilder();
    if (event.getModifiersEx() != 0)
      builder.append(getModifiersExText(event.getModifiersEx())).append("+ ");
    builder.append(getKeyText(event.getKeyCode()));
    return builder.toString();
  }

}
