package org.sikora.ruler;


import spock.lang.Specification
import org.sikora.ruler.model.input.InputDriver
import org.sikora.ruler.model.input.InputDriver.Command
import org.sikora.ruler.ui.awt.AwtResultWindow

/**
 * User: der3k
 * Date: 1.8.11
 * Time: 21:58
 */
public class RulerTest extends Specification {

  def 'first global hotkey enables input window'() {
    def driver = Mock(InputDriver)
    def ruler = new Ruler(driver, null)
  when:
    ruler.onHotKey(1)
  then:
    1 * driver.set(Command.FOCUS_INPUT)
  }

  def 'second global hotkey shows result window'() {
    def result = Mock(AwtResultWindow)
    def ruler = new Ruler(null, result)
  when:
    ruler.onHotKey(2)
  then:
    1 * result.display()
  }

}
