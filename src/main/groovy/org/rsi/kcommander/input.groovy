package org.rsi.kcommander

import com.melloware.jintellitype.HotkeyListener
import com.melloware.jintellitype.JIntellitype
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import javax.swing.JDialog
import javax.swing.JTextArea
import org.sikora.ruler.ui.AwtInputDriver
import org.sikora.ruler.ui.AwtInputWindow
import org.sikora.ruler.ui.AwtUtil

class ResultWindow extends JDialog {
  def text

  ResultWindow(content) {
    setSize(600, 300)
    setLocationRelativeTo(null)
    setAlwaysOnTop(true)
    AwtUtil.makeWindowOpaque(this)
    text = new JTextArea(content, 5, 40)
    AwtUtil.setFontAndColor(text, 30)
    AwtUtil.setEmptyBorder(text, 10)
    text.setEditable(false)
    text.addKeyListener(new KeyAdapter() {
      @Override
      void keyPressed(KeyEvent e) {
        hide()
      }
    })
    add(text)
    show()
  }
}


class HookHotKeyListener implements HotkeyListener {
  def input
  def result

  void onHotKey(int hookId) {
    switch (hookId) {
      case 1:
        input?.focus()
        result?.hide()
        break;
      case 2:
        result?.show()
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
