package org.sikora.ruler.ui

import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import org.sikora.ruler.Hint
import org.sikora.ruler.model.input.Broker
import org.sikora.ruler.model.input.BrokerListener
import org.sikora.ruler.model.input.Context
import org.sikora.ruler.model.input.Input
import org.slf4j.LoggerFactory
import static java.awt.event.KeyEvent.*
import org.sikora.ruler.model.input.InputDriver

/**
 * User: der3k
 * Date: 9.7.11
 * Time: 13:05
 */
class AwtBroker implements KeyListener, Broker {
  static final logger = LoggerFactory.getLogger(AwtBroker)
  static final List<Character> ACTION_KEY_CHARS = [
      '1',
      '2',
      '3',
      '4',
      '5',
      '6',
      '7',
      '8',
      '9'
  ].collect { it as char }

  final InputDriver driver
  final InputDriverCache cache
  final List<BrokerListener> listeners = []

  AwtBroker(final InputDriver driver) {
    this.driver = driver
    this.cache = new InputDriverCache(driver)
  }

  void addListener(final BrokerListener listener) {
    if (listener == null)
      throw new IllegalArgumentException("listener cannot be null")
    listeners << listener
  }

  void removeListener(final BrokerListener listener) {
    if (listener == null)
      throw new IllegalArgumentException("listener cannot be null")
    listeners.remove(listener)
  }

  void setInput(Input input) {
    driver.setInput(input)
    updateCacheAndPropagateChangeIfNeeded()
  }

  void setHints(Hint[] hints) {
    driver.setHints(hints)
    updateCacheAndPropagateChangeIfNeeded()
  }

  Context context() {
    new Context(this, cache.input(), cache.hints())
  }

  @Override
  void keyPressed(KeyEvent event) {
    int key = event.keyCode
    logger.trace("keyPressed: {}, '{}'", key, getKeyText(key))
    switch (key) {
      case VK_ESCAPE:
        logger.debug("Canceling {} with '{}' key", cache.input(), getKeyText(key))
        event.consume()
        listeners*.onCancel(new Broker.CancelEvent(context()))
        break
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
        logger.debug("Completing {} with {} key", cache.input(), getKeyText(key))
        event.consume()
        listeners*.onComplete(new Broker.CompleteEvent(context(), hintFromKey(key)))
        break
      case VK_ENTER:
        logger.debug("Executing {} with '{}' key", cache.input(), getKeyText(key))
        event.consume()
        listeners*.onSubmit(new Broker.SubmitEvent(context()))
        break
    }
  }

  @Override
  void keyReleased(KeyEvent event) {
    updateCacheAndPropagateChangeIfNeeded()
  }

  @Override
  void keyTyped(KeyEvent event) {
    if (ACTION_KEY_CHARS.contains(event.keyChar)) {
      event.consume()
    }
  }

  Hint hintFromKey(final int key) {
    def i = hintIndexFromKey(key)
    i < cache.hints().length ? cache.hints[i] : Hint.NONE
  }

  int hintIndexFromKey(final int key) {
    if (VK_TAB == key)
      return 0
    if (VK_1 <= key && key <= VK_9)
      return key - VK_1
    throw new IllegalArgumentException("illegal complete key: " + key)
  }

  def updateCacheAndPropagateChangeIfNeeded() {
    cache.update()
    if (cache.lastUpdate().isNotVoid()) {
      logger.trace('Input changed {}', cache.lastUpdate())
      logger.debug('Calling onChange() on all listeners')
      def event = new Broker.UpdateEvent(context(), cache.lastUpdate())
      listeners*.onChange(event)
    }
  }

  class InputDriverCache {
    final InputDriver driver
    Input input = Input.EMPTY
    Hint[] hints
    Input.Update update

    InputDriverCache(final InputDriver driver) {
      this.driver = driver
      update()
    }

    Input input() {
      input
    }

    Hint[] hints() {
      hints
    }

    Input.Update lastUpdate() {
      update
    }

    Input.Update update() {
      update = input.updateTo(driver.input())
      if (!update.isVoid())
        input = update.newValue()
      hints = driver.hints()
      update
    }

  }
}
