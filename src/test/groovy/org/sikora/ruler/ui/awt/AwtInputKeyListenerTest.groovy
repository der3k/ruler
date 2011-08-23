package org.sikora.ruler.ui.awt;


import java.awt.event.KeyEvent
import org.sikora.ruler.model.input.InputDriver
import spock.lang.Specification
import static java.awt.event.KeyEvent.*
import static org.sikora.ruler.model.input.InputDriver.Command.*

public class AwtInputKeyListenerTest extends Specification {
  def driver = Mock(InputDriver)
  def listener = new AwtInputKeyListener(driver)

  def 'ESC key propagates as CANCEL command'() {
    def event = Mock(KeyEvent)
  when:
    event.getKeyCode() >> VK_ESCAPE
    listener.keyPressed(event)
  then:
    1 * driver.issue({ it.command() == CANCEL})
  }

  def 'TAB and 1-9 keys propagates as COMPLETE command'() {
    def event = Mock(KeyEvent)
  when:
    event.getKeyCode() >> code
    listener.keyPressed(event)
  then:
    1 * driver.issue({ it.command() == COMPLETE})
  where:
    code << [VK_TAB, VK_1, VK_2, VK_3, VK_4, VK_5, VK_6, VK_7, VK_8, VK_9]
  }

  def '1-9 COMPLETE command selects indexed hint'() {
    def event = Mock(KeyEvent)
  when:
    event.getKeyCode() >> VK_2
    listener.keyPressed(event)
  then:
    1 * driver.issue({ it.selectHint() == 1 })
  }

  def 'TAB COMPLETE command selects the first hint'() {
    def event = Mock(KeyEvent)
  when:
    event.getKeyCode() >> VK_TAB
    listener.keyPressed(event)
  then:
    1 * driver.issue({ it.selectHint() == 0 })
  }

  def 'ENTER key propagates SUBMIT command'() {
    def event = Mock(KeyEvent)
  when:
    event.getKeyCode() >> VK_ENTER
    listener.keyPressed(event)
  then:
    1 * driver.issue({ it.command() == SUBMIT && it.selectHint() == 0 })
  }
}
