package org.sikora.ruler.ui.awt;


import org.sikora.ruler.model.input.Hints
import org.sikora.ruler.model.input.Hints.Item
import org.sikora.ruler.model.input.Input
import org.sikora.ruler.model.input.InputDriver
import org.sikora.ruler.model.input.InputDriver.InputCommand
import spock.lang.Specification
import static org.sikora.ruler.model.input.InputDriver.Command.*
import static org.sikora.ruler.model.input.InputDriver.Event.*

public class AwtInputDriverTest extends Specification {
  final def inputField = Mock(AwtInputWindow)
  def driver = new AwtInputDriver(inputField)
  def listener = Mock(InputDriver.Listener)

  def setup() {
    driver.addListener(listener)
  }

  // TODO write tests for all commands

  def 'sets input'() {
  when:
    driver.issue(InputCommand.of(UPDATE, Input.of('task')))
  then:
    1 * inputField.set(Input.of('task'))
    1 * listener.dispatch({ it.event() == CHANGED })
  }

  def 'void input change is not propagated'() {
    driver.issue(InputCommand.of(UPDATE, Input.of('task')))
  when:
    driver.issue(InputCommand.of(UPDATE, Input.of('task')))
  then:
    0 * listener.dispatch(_)
  }

  def 'sets hints'() {
    def hints = new Hints([new Item('item')])
  when:
    driver.issue(InputCommand.of(HINT, hints))
  then:
    1 * inputField.set(hints)
  }

  def 'propagates FOCUS command'() {
  when:
    driver.issue(InputCommand.of(FOCUS))
  then:
    1 * inputField.focus()
  }

  def 'propagates HIDE command'() {
  when:
    driver.issue(InputCommand.of(HIDE))
  then:
    1 * inputField.hide()
  }

  def 'propagates RESET command'() {
    driver.issue(InputCommand.of(UPDATE, Input.of('task')))
  when:
    driver.issue(InputCommand.of(RESET))
  then:
    driver.input == Input.EMPTY
    driver.hints == Hints.NONE
    1 * inputField.set(Input.EMPTY)
    1 * inputField.set(Hints.NONE)
  }
}
