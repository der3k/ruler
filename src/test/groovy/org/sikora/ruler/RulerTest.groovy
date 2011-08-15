package org.sikora.ruler;


import org.sikora.ruler.context.ContextProvider
import org.sikora.ruler.model.input.InputDriver

import org.sikora.ruler.ui.awt.AwtResultWindow
import spock.lang.Specification
import org.sikora.ruler.context.Context
import org.sikora.ruler.model.input.InputDriver.Action

/**
 * User: der3k
 * Date: 1.8.11
 * Time: 21:58
 */
public class RulerTest extends Specification {

  def 'first global hotkey enables input window'() {
    def contextProvider = Mock(ContextProvider)
    def context = Mock(Context)
    def driver = Mock(InputDriver)
    contextProvider.currentContext() >> context
    context.inputDriver() >> driver
    def ruler = new Ruler(contextProvider, null)
  when:
    ruler.onHotKey(1)
  then:
    1 * driver.issue(Action.FOCUS_INPUT)
  }

  def 'second global hotkey shows result window'() {
    def contextProvider = Mock(ContextProvider)
    def context = Mock(Context)
    def result = Mock(AwtResultWindow)
    contextProvider.currentContext() >> context
    context.resultWindow() >> result
    def ruler = new Ruler(contextProvider, null)
  when:
    ruler.onHotKey(2)
  then:
    1 * result.display()
  }

}
