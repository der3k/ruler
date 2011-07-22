package org.rsi.kcommander

import org.sikora.ruler.model.input.InputDriver
import org.sikora.ruler.model.input.InputDriver.Event
import org.sikora.ruler.model.input.Hints
import com.melloware.jintellitype.JIntellitype
import org.sikora.ruler.model.input.Input

/**
 * User: sikorric
 * Date: 22.7.11
 * Time: 23:15
 */
 
class InputDriverListener implements InputDriver.Listener {
  static def Random RANDOM = new Random()
  final hookListener
  final input

  InputDriverListener(hookListener, input) {
    this.hookListener = hookListener
    this.input = input
  }

  void dispatch(Event event) {
    final String text = event.input().text()
    switch (event.command()) {
      case InputDriver.Command.UPDATE_INPUT:
        Hints hints = Hints.NONE
        if (!text.isEmpty()) {
          def items = (1..RANDOM.nextInt(10)).collect {new Hints.Item("$text:$it")}
          hints = new Hints(items)
        }
        event.driver().set(hints)
        break
      case InputDriver.Command.COMPLETE_INPUT:
        if (event.hint() != Hints.Item.NONE)
          event.driver().set(Input.of(text + event.hint()))
        break
      case InputDriver.Command.SUBMIT_INPUT:
        if ('now' == text) {
          event.driver().set(Input.EMPTY)
          input.hide()
          String now = new Date().format('hh:mm dd.MM.yyyy')
          hookListener.result = new ResultWindow(now)
        }
        break
      case InputDriver.Command.CANCEL:
        JIntellitype.getInstance().cleanUp()
        System.exit(1)
        break
    }
  }
}
