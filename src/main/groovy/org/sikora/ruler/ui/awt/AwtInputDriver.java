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
 * User: sikorric
 * Date: 13.7.11
 * Time: 11:59
 */

public class AwtInputDriver implements KeyListener, InputDriver {
  private static final Logger LOGGER = LoggerFactory.getLogger(AwtInputDriver.class);
  private static final char COMPLETE_KEY_MIN = '1';
  private static final char COMPLETE_KEY_MAX = '9';

  private final InputField inputField;
  private Hints hints = Hints.NONE;
  private final List<Handler> handlers = new ArrayList<Handler>();
  private Input input = Input.EMPTY;

  public AwtInputDriver() {
    final AwtInputWindow inputWindow = new AwtInputWindow();
    inputWindow.addKeyListener(this);
    inputWindow.set(input);
    inputField = inputWindow;
  }

  public void set(final Input input) {
    inputField.set(input);
    Input.Update update = this.input.updateTo(input);
    propagateInputUpdate(update);
  }

  public void set(final Hints hints) {
    this.hints = hints;
    inputField.set(hints);
    dispatchEventFor(UPDATE_HINTS);
  }

  public void set(final Command command) {
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

  public void addHandler(final Handler listener) {
    if (listener == null)
      throw new IllegalArgumentException("listener cannot be null");
    handlers.add(listener);
  }

  public void removeHandler(final Handler listener) {
    if (listener == null)
      throw new IllegalArgumentException("listener to remove cannot be null");
    handlers.remove(listener);
  }

  public void keyTyped(final KeyEvent event) {
    if (isCompleteKey(event.getKeyChar()))
      event.consume();
  }

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
    final InputCommand inputCommand = new InputCommand(command, input, hints.selected());
    return new Event(this, inputCommand);
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
