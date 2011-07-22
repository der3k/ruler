package org.rsi.kcommander

import com.melloware.jintellitype.HotkeyListener
import com.melloware.jintellitype.JIntellitype
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import javax.swing.JDialog
import javax.swing.JTextArea
import javax.swing.JTextField
import javax.swing.JWindow
import org.sikora.ruler.model.input.Hints
import org.sikora.ruler.model.input.Input
import org.sikora.ruler.model.input.InputField
import org.sikora.ruler.ui.AwtInputDriver
import org.sikora.ruler.ui.AwtInputField

class InputWindow extends JDialog implements InputField {
  final JTextField field = new JTextField()
  final WhispererWindow whisperer = new WhispererWindow()
  final HookHotKeyListener hookListener

  InputWindow(hookListener) {
    this.hookListener = hookListener
    AwtInputField.setLocationAndSize(this, 0, 0, 800, 50)
    AwtInputField.makeWindowOpaque(this)
    configureField()
    add(field)
  }

  def configureField() {
    field.setFocusTraversalKeysEnabled(false)
    AwtInputField.setFontAndColor(field, 35)
    AwtInputField.setEmptyBorder(field, 4)
    def driver = new AwtInputDriver(this)
    def listener = new InputDriverListener(hookListener)
    driver.addListener(listener)
    field.addKeyListener(driver)
  }

  Input input() {
    Input.of(field.getText(), field.getCaretPosition())
  }

  void set(Input input) {
    field.setText(input.text())
    field.setCaretPosition(input.marker())
  }

  void set(Hints hints) {
    whisperer.setHints(hints)
  }

  void focus() {
    this.show()
  }

  void hide() {
    whisperer.hide()
    super.hide()
  }

}

class WhispererWindow extends JWindow {
  def text

  WhispererWindow() {
    AwtInputField.setLocationAndSize(this, 0, 50, 600, 355)
    AwtInputField.makeWindowOpaque(this)
    text = new JTextArea(null, 5, 40)
    AwtInputField.setFontAndColor(text, 30)
    AwtInputField.setEmptyBorder(text, 10)
    add(text)
  }

  void setHints(Hints hints) {
    if (hints.size() == 0) {
      hide()
    } else {
      populateHints(hints)
      show()
    }
  }

  def populateHints(Hints hints) {
    def size = [9, hints.size()].min()
    def items = hints.items()
    def content = new StringBuilder()
    (1..size).each { content << "$it - ${items[it - 1]}\n"}
    this.text.setText(content.toString())
    setSize(600, size * 40)
  }
}

class ResultWindow extends JDialog {
  def text

  ResultWindow(content) {
    setSize(600, 300)
    setLocationRelativeTo(null)
    setAlwaysOnTop(true)
    AwtInputField.makeWindowOpaque(this)
    text = new JTextArea(content, 5, 40)
    AwtInputField.setFontAndColor(text, 30)
    AwtInputField.setEmptyBorder(text, 10)
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
        input?.show()
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

JIntellitype.setLibraryLocation('../lib/JIntellitype64.dll')
def hook = JIntellitype.getInstance()
def hookListener = new HookHotKeyListener()
def input = new InputWindow(hookListener)
hookListener.input = input
hook.addHotKeyListener(hookListener)
hook.registerHotKey(1, JIntellitype.MOD_CONTROL, (int) ' ')
hook.registerHotKey(2, JIntellitype.MOD_CONTROL + JIntellitype.MOD_SHIFT, (int) ' ')
