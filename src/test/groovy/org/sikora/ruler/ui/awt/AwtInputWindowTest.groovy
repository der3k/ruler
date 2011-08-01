package org.sikora.ruler.ui.awt;


import org.sikora.ruler.model.input.Hints
import org.sikora.ruler.model.input.Hints.Item
import org.sikora.ruler.model.input.Input
import spock.lang.Specification

/**
 * User: der3k
 * Date: 1.8.11
 * Time: 20:37
 */
public class AwtInputWindowTest extends Specification {
  def 'sets input'() {
    def inputWindow = new AwtInputWindow()
    inputWindow.set(Input.of('task'))
  expect:
    inputWindow.textField.getText() == 'task'
    inputWindow.textField.getCaretPosition() == 4
  }

  def 'sets hints'() {
    def inputWindow = new AwtInputWindow()
    def hints = new Hints([new Item('item')])
    inputWindow.set(hints)
  expect:
    inputWindow.hintsWindow.textArea.getText().contains('item')
  }

  def 'provides input'() {
    def inputWindow = new AwtInputWindow()
    inputWindow.set(Input.of('task'))
  expect:
    inputWindow.input() == Input.of('task')
  }


}
