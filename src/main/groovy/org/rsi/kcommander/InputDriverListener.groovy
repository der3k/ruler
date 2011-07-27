package org.rsi.kcommander

import com.melloware.jintellitype.JIntellitype
import org.sikora.ruler.model.input.Hints
import org.sikora.ruler.model.input.Input
import org.sikora.ruler.model.input.InputDriver
import org.sikora.ruler.model.input.InputDriver.Event
import static org.sikora.ruler.model.input.InputDriver.Command.*
import org.sikora.ruler.Ruler
import org.sikora.ruler.Result

/**
 * User: sikorric
 * Date: 22.7.11
 * Time: 23:15
 */

class InputDriverListener implements InputDriver.Listener {
  static def Random RANDOM = new Random()
  final Ruler ruler

  InputDriverListener(final Ruler ruler) {
    this.ruler = ruler
  }

  void dispatch(Event event) {
    final String text = event.input().text()
    switch (event.command()) {
      case UPDATE_INPUT:
        Hints hints = Hints.NONE
        if (!text.isEmpty()) {
          def items = (1..RANDOM.nextInt(10)).collect {new Hints.Item("$text:$it")}
          hints = new Hints(items)
        }
        event.driver().set(hints)
        break
      case COMPLETE_INPUT:
        if (event.hint() != Hints.Item.NONE)
          event.driver().set(Input.of(text + event.hint()))
        break
      case SUBMIT_INPUT:
        if ('now' == text) {
          event.driver().set(RESET_INPUT)
          String now = new Date().format('hh:mm dd.MM.yyyy')
          ruler.show(new Result(text: now))
        }
        break
      case CANCEL:
        JIntellitype.getInstance().cleanUp()
        System.exit(1)
        break
    }
  }
}
