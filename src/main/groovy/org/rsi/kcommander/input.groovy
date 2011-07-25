package org.rsi.kcommander

import com.melloware.jintellitype.HotkeyListener
import com.melloware.jintellitype.JIntellitype
import org.sikora.ruler.model.input.InputField
import org.sikora.ruler.ui.AwtInputDriver
import org.sikora.ruler.ui.AwtInputWindow
import org.sikora.ruler.ui.AwtResultWindow

class HookHotKeyListener implements HotkeyListener {
  InputField input
  AwtResultWindow result = new AwtResultWindow()

  void onHotKey(int hookId) {
    switch (hookId) {
      case 1:
        input?.focus()
        result.hide()
        break;
      case 2:
        result.show()
        break;
      default:
        break;
    }
  }
}

def input = new AwtInputWindow()
def driver = new AwtInputDriver(input)
input.addKeyListener(driver)

def hookListener = new HookHotKeyListener()
def driverListener = new InputDriverListener(hookListener)
driver.addListener(driverListener)
hookListener.input = input

JIntellitype.setLibraryLocation('../lib/JIntellitype64.dll')
def hook = JIntellitype.getInstance()
hook.addHotKeyListener(hookListener)
hook.registerHotKey(1, JIntellitype.MOD_CONTROL, (int) ' ')
hook.registerHotKey(2, JIntellitype.MOD_CONTROL + JIntellitype.MOD_SHIFT, (int) ' ')
