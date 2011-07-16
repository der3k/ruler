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
import org.sikora.ruler.model.input.InputDriver
import org.sikora.ruler.model.input.InputDriver.Event
import org.sikora.ruler.ui.AwtInputDriver
import org.sikora.ruler.ui.AwtInputField

class InputWindow extends JDialog {
  def input

  InputWindow(hookListener) {
    AwtInputField.setLocationAndSize(this, 0, 0, 800, 50)
    AwtInputField.makeWindowOpaque(this)
    input = new InputField(this, hookListener)
    input.setFocusTraversalKeysEnabled(false)
    AwtInputField.setFontAndColor(input, 35)
    AwtInputField.setEmptyBorder(input, 4)
    add(input)
  }

  void reset() {
    input.setText('')
  }
}

class InputField extends JTextField implements org.sikora.ruler.model.input.InputField {
  static def Random RANDOM = new Random()
  def whisperer = new WhispererWindow()

  InputField(input, hookListener) {
    super("")
    def driver = new AwtInputDriver(this)
    def listener = new InputDriver.Listener() {
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
    driver.addListener(listener)
    addKeyListener(driver)
  }

  @Override
  Input input() {
    Input.of(getText(), getCaretPosition())
  }

  @Override
  void set(Input input) {
    setText(input.text())
    setCaretPosition(input.marker())
  }

  @Override
  void set(Hints hints) {
    whisperer.setHints(hints)
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
        input?.reset()
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
