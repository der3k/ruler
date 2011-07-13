package org.sikora.ruler.ui;

import org.sikora.ruler.model.input.Hints;
import org.sikora.ruler.model.input.Input;
import org.sikora.ruler.model.input.InputDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import static java.awt.event.KeyEvent.*;

/**
 * User: sikorric
 * Date: 13.7.11
 * Time: 11:59
 */

public class AwtInputDriver implements KeyListener, InputDriver {
  private static final Logger LOGGER = LoggerFactory.getLogger(AwtInputDriver.class);
  private static final char COMPLETE_KEY_MIN = '1';
  private static final char COMPLETE_KEY_MAX = '9';

  private final AwtInputField field;
  private Hints hints = Hints.NONE;
  private final List<Listener> listeners = new ArrayList<Listener>();
  private Input input = Input.EMPTY;

  public AwtInputDriver(final AwtInputField field) {
    this.field = field;
    field.setInput(input);
  }

  public void setInput(final Input input) {
    field.setInput(input);
    Input.Update update = this.input.updateTo(input);
    propagateInputUpdate(update, Action.UPDATE);
  }

  public void setHints(Hints hints) {
    this.hints = hints;
    field.setHints(hints);
    dispatchAction(Action.HINTS_UPDATE);
  }

  public void addListener(final Listener listener) {
    if (listener == null)
      throw new IllegalArgumentException("listener cannot be null");
    listeners.add(listener);
  }

  public void removeListener(final Listener listener) {
    if (listener == null)
      throw new IllegalArgumentException("listener to remove cannot be null");
    listeners.remove(listener);
  }

  public void keyTyped(final KeyEvent event) {
    if (isCompleteKey(event.getKeyChar()))
      event.consume();
  }

  private boolean isCompleteKey(final char key) {
    return COMPLETE_KEY_MIN <= key && key <= COMPLETE_KEY_MAX;
  }

  public void keyPressed(final KeyEvent event) {
    int key = event.getKeyCode();
    String keyText = KeyEvent.getKeyText(key);
    LOGGER.trace("Key pressed: '{}'", keyText);
    switch (key) {
      case VK_ESCAPE:
        event.consume();
        dispatchAction(Action.CANCEL);
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
        event.consume();
        hints.select(hintIndexFromKey(key));
        dispatchAction(Action.COMPLETE);
        break;
      case VK_ENTER:
        event.consume();
        dispatchAction(Action.SUBMIT);
        break;
    }
  }

  private void dispatchAction(final Action action) {
    Input.Update inputUpdate = input.updateTo(input, hints.selected());
    Update update = new Update(inputUpdate, action);
    dispatchUpdate(update);
  }

  private int hintIndexFromKey(final int key) {
    if (VK_TAB == key)
      return 0;
    if (VK_1 <= key && key <= VK_9)
      return key - VK_1;
    throw new IllegalArgumentException("unsupported complete key: " + getKeyText(key));
  }

  public void keyReleased(KeyEvent event) {
    refreshFieldInputAndPropagate();
  }

  private void propagateInputUpdate(Input.Update update, Action action) {
    if (update.isNotVoid()) {
      LOGGER.trace("Input changed: {}", update);
      input = update.newValue();
      dispatchUpdate(new Update(update, action));
    }
  }

  private void dispatchUpdate(Update update) {
    LOGGER.debug("Dispatching onChange({})", update);
    for (Listener listener : listeners)
      listener.onChange(update);
  }

  private void refreshFieldInputAndPropagate() {
    Input.Update update = input.updateTo(field.input());
    propagateInputUpdate(update, Action.UPDATE);
  }
}
