package org.sikora.ruler.ui.awt;


import java.awt.event.KeyEvent
import spock.lang.Specification

/**
 * User: sikorric
 * Date: 1.8.11
 * Time: 17:10
 */

public class AwtResultWindowTest extends Specification {

  def 'displays text'() {
    def resultWindow = new AwtResultWindow()
    resultWindow.display('result')
    expect:
    resultWindow.isVisible()
    resultWindow.textArea.getText() == 'result'
  }

  def 'key event hides window'() {
    def resultWindow = new AwtResultWindow()
    resultWindow.display('result')
    def keyEvent = new KeyEvent(resultWindow.textArea, KeyEvent.KEY_PRESSED, 0, 0, 32)
    resultWindow.dispatchEvent(keyEvent)
    expect:
    !resultWindow.isVisible()
  }
}
