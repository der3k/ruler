package org.sikora.ruler

import com.melloware.jintellitype.HotkeyListener
import com.melloware.jintellitype.JIntellitype
import org.sikora.ruler.model.input.Hints
import org.sikora.ruler.model.input.Input
import org.sikora.ruler.model.input.InputDriver
import org.sikora.ruler.model.input.InputDriver.Event
import org.sikora.ruler.model.input.InputField
import org.sikora.ruler.ui.awt.AwtInputWindow
import org.sikora.ruler.ui.awt.AwtResultWindow
import static org.sikora.ruler.model.input.InputDriver.Command.*
import org.sikora.ruler.ui.awt.AwtInputDriver

/**
 * User: sikorric
 * Date: 8.7.11
 * Time: 15:06
 */

class Ruler implements InputDriver.Listener, HotkeyListener {
  static def Random RANDOM = new Random()
  final InputField inputField
  final AwtResultWindow resultWindow

  public static void main(String[] args) {
    def input = new AwtInputWindow()
    def driver = new AwtInputDriver(input)
    input.addKeyListener(driver)

    def ruler = new Ruler(input, new AwtResultWindow())
    driver.addListener(ruler)

    JIntellitype.setLibraryLocation('../lib/JIntellitype64.dll')
    def hook = JIntellitype.getInstance()
    hook.addHotKeyListener(ruler)
    hook.registerHotKey(1, JIntellitype.MOD_CONTROL, (int) ' ')
    hook.registerHotKey(2, JIntellitype.MOD_CONTROL + JIntellitype.MOD_SHIFT, (int) ' ')
  }

  Ruler(final InputField inputField, final AwtResultWindow resultWindow) {
    this.inputField = inputField
    this.resultWindow = resultWindow
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
          resultWindow.display(now)
        }
        break
      case CANCEL:
        JIntellitype.getInstance().cleanUp()
        System.exit(1)
        break
    }
  }

  void onHotKey(int hook) {
    switch (hook) {
      case 1:
        inputField.focus()
        break;
      case 2:
        resultWindow.display()
        break;
      default:
        break;
    }
  }

}

