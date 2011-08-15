package org.sikora.ruler.ui.awt;


import java.awt.event.KeyEvent
import org.sikora.ruler.model.input.Hints
import org.sikora.ruler.model.input.Hints.Item
import org.sikora.ruler.model.input.Input
import org.sikora.ruler.model.input.InputDriver
import spock.lang.Ignore
import spock.lang.Specification
import static java.awt.event.KeyEvent.*
import static org.sikora.ruler.model.input.InputDriver.Command.*

/**
 * User: der3k
 * Date: 1.8.11
 * Time: 20:51
 */
public class AwtInputDriverTest extends Specification {
  def driver = new AwtInputDriver()
  def AwtInputWindow inputField = driver.inputField
  def AwtHintsWindow hintsWindow = inputField.hintsWindow
  def handler = Mock(InputDriver.Handler)

  def setup() {
    driver.addHandler(handler)
  }

  def 'sets input'() {
  when:
    driver.issue(UPDATE_INPUT, Input.of('task'))
  then:
    inputField.input() == Input.of('task')
    1 * handler.dispatch({ it.command() == UPDATE_INPUT })
  }

  def 'void input change is not propagated'() {
    driver.issue(UPDATE_INPUT, Input.of('task'))
  when:
    driver.issue(UPDATE_INPUT, Input.of('task'))
  then:
    0 * handler.dispatch(_)
  }

  def 'sets hints'() {
  when:
    driver.issue(UPDATE_HINTS, new Hints([new Item('item')]))
  then:
    hintsWindow.textArea.getText().contains('item')
    1 * handler.dispatch({ it.command() == UPDATE_HINTS })
  }

  def 'issues command'() {
  when:
    driver.issue(UPDATE_INPUT)
  then:
    1 * handler.dispatch({ it.command() == UPDATE_INPUT })
  }

  // TODO test the functionality without actual window
  @Ignore
  def 'propagates FOCUS_INPUT command'() {
  when:
    driver.issue(FOCUS_INPUT)
  then:
    inputField.window.isFocused()
  }

  @Ignore
  def 'propagates HIDE_INPUT command'() {
    driver.issue(FOCUS_INPUT)
  when:
    driver.issue(HIDE_INPUT)
  then:
    !inputField.window.isVisible()
  }

  def 'propagates RESET_INPUT command'() {
    driver.issue(UPDATE_INPUT, Input.of('task'))
  when:
    driver.issue(RESET_INPUT)
  then:
    driver.input == Input.EMPTY
    driver.hints == Hints.NONE
  }

  def 'ESC key propagates as CANCEL command'() {
    def event = Mock(KeyEvent)
  when:
    event.getKeyCode() >> VK_ESCAPE
    driver.keyPressed(event)
  then:
    1 * handler.dispatch({ it.command() == CANCEL })
  }

  def 'TAB and 1-9 keys propagates as INPUT_COMPLETE command'() {
    def event = Mock(KeyEvent)
  when:
    event.getKeyCode() >> code
    driver.keyPressed(event)
  then:
    1 * handler.dispatch({ it.command() == COMPLETE_INPUT })
  where:
    code << [VK_TAB, VK_1, VK_2, VK_3, VK_4, VK_5, VK_6, VK_7, VK_8, VK_9]
  }

  def '1-9 INPUT_COMPLETE command selects indexed hint'() {
    def item2 = new Item('item2')
    driver.issue(UPDATE_HINTS, new Hints([new Item('item1'), item2]))
    def event = Mock(KeyEvent)
  when:
    event.getKeyCode() >> VK_2
    driver.keyPressed(event)
  then:
    driver.hints.selected() == item2
    1 * handler.dispatch({ it.hint() == item2 })
  }

  def 'TAB INPUT_COMPLETE command selects the first hint'() {
    def item1 = new Item('item1')
    def item2 = new Item('item2')
    driver.issue(UPDATE_HINTS, new Hints([item1, item2]))
    def event = Mock(KeyEvent)
  when:
    event.getKeyCode() >> VK_TAB
    driver.keyPressed(event)
  then:
    driver.hints.selected() == item1
    1 * handler.dispatch({ it.hint() == item1 })
  }

  def 'ENTER key propagates SUBMIT_INPUT command'() {
    def event = Mock(KeyEvent)
  when:
    event.getKeyCode() >> VK_ENTER
    driver.keyPressed(event)
  then:
    1 * handler.dispatch({ it.command() == SUBMIT_INPUT })
  }

}
