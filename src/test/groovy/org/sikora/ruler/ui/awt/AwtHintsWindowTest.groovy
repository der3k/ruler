package org.sikora.ruler.ui.awt;


import org.sikora.ruler.model.input.Hints
import org.sikora.ruler.model.input.Hints.Item
import spock.lang.Specification

/**
 * User: sikorric
 * Date: 1.8.11
 * Time: 16:49
 */

public class AwtHintsWindowTest extends Specification {
  def 'shows hints'() {
    def hintsWindow = new AwtHintsWindow()
    hintsWindow.set(new Hints([new Item('item'), new Item('item2')]))
    expect:
    hintsWindow.window.isShowing()
    hintsWindow.textArea.getText() == '1 - item\n2 - item2'
  }

  def 'no hints hides window'() {
    def hintsWindow = new AwtHintsWindow()
    hintsWindow.window.show()
    when:
    hintsWindow.set(Hints.NONE)
    then:
    !hintsWindow.window.isShowing()
  }

}
