package org.sikora.ruler.ui

import java.awt.event.KeyEvent
import org.sikora.ruler.Draft
import org.sikora.ruler.DraftListener
import org.sikora.ruler.InputProviderOld
import spock.lang.Specification

/**
 * User: der3k
 * Date: 9.7.11
 * Time: 13:29
 */
class AwtInputBrokerKeyListenerTest extends Specification {
  def input = Mock(InputProviderOld)
  def draftListener = Mock(DraftListener)
  def keyListener = new AwtBroker(input, draftListener)
  def event = Mock(KeyEvent)

  def 'ESC cancels draft and consumes event'() {
    event.keyCode >> KeyEvent.VK_ESCAPE
  when:
    keyListener.keyPressed(event)
  then:
    1 * draftListener.onCancel(_)
    1 * event.consume()
    0 * draftListener.onChange(_)

  }

  def 'TAB completes draft with TAB key and consumes event'() {
    event.keyCode >> KeyEvent.VK_TAB
  when:
    keyListener.keyPressed(event)
  then:
    1 * draftListener.onComplete(_, KeyEvent.VK_TAB) >> Draft.EMPTY
    1 * event.consume()
    0 * draftListener.onChange(_)
  }

  def '1 completes draft with 1 key and consumes event'() {
    event.keyCode >> KeyEvent.VK_1
  when:
    keyListener.keyPressed(event)
  then:
    1 * draftListener.onComplete(_, KeyEvent.VK_1) >> Draft.EMPTY
    1 * event.consume()
    0 * draftListener.onChange(_)
  }

  def 'on complete draft and input is updated'() {
    event.keyCode >> KeyEvent.VK_TAB
  when:
    keyListener.keyPressed(event)
  then:
    1 * draftListener.onComplete(_, KeyEvent.VK_TAB) >> Draft.parse('a', 1)
    1 * event.consume()
    1 * draftListener.onChange(Draft.parse('a', 1))
    1 * input.setTextAndPosition('a', 1)
  }

  def 'ENTER executes draft and consumes event'() {
    event.keyCode >> KeyEvent.VK_ENTER
  when:
    keyListener.keyPressed(event)
  then:
    1 * draftListener.onExecute(_)
    1 * event.consume()
    0 * draftListener.onChange(_)
  }

  def 'text change triggers draft change'() {
    input.text() >> 'a'
    input.position() >> 1
  when:
    keyListener.keyReleased(event)
    keyListener.keyReleased(event)
  then:
    1 * draftListener.onChange(_)
  }
}
