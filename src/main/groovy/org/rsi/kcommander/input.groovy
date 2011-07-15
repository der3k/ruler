package org.rsi.kcommander

import com.melloware.jintellitype.HotkeyListener
import com.melloware.jintellitype.JIntellitype
import com.sun.awt.AWTUtilities
import java.awt.Color
import java.awt.Font
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import org.sikora.ruler.model.input.Hints
import org.sikora.ruler.model.input.Input
import org.sikora.ruler.model.input.InputDriver

import org.slf4j.LoggerFactory
import javax.swing.*
import org.sikora.ruler.ui.AwtInputDriver
import org.sikora.ruler.ui.AwtInputField
import org.sikora.ruler.model.input.InputDriver.Event

class InputWindow extends JDialog {
  def hookListener
  def input

  InputWindow(hookListener) {
    this.hookListener = hookListener
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent event) {
        JIntellitype.getInstance().cleanUp()
        System.exit(1)
      }
    })
    setUndecorated(true)
    setSize(800, 50)
    setLocation(0, 0)
    getContentPane().setBackground(Color.BLACK)
    input = new InputField(this, hookListener)
    input.setBackground(Color.BLACK)
    input.setForeground(Color.YELLOW)
    input.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4))
    add(input)
    AWTUtilities.setWindowOpacity(this, 0.85f)
//    show()
  }

  void reset() {
//    input.listener.reset()
    input.setText('')
  }
}

class InputField extends JTextField implements AwtInputField {
  def logger = LoggerFactory.getLogger(InputField.class)
  def result
  def whisperer
  def hookListener
  def static final List<Integer> ACTION_KEYS = [KeyEvent.VK_ESCAPE, KeyEvent.VK_TAB, KeyEvent.VK_ENTER]
  def input

  InputField(input, hookListener) {
    super("")
    this.input = input
    this.hookListener = hookListener
    setFont(new Font('Candara', Font.PLAIN, 35))
    setFocusTraversalKeysEnabled(false)
    def driver = new AwtInputDriver(this)
    def listener = new InputDriver.Listener() {
      void dispatch(Event event) {
        switch (event.command()) {
          case InputDriver.Command.UPDATE_INPUT:
            if (whisperer == null) {
              whisperer = new WhispererWindow()
              whisperer.setLocation(0, 50)
              whisperer.setSize(600, 355)
            }
            if (!getText().isEmpty()) {
              fillWhisperer(whisperer.text, getText())
              whisperer.show()
            } else {
              whisperer.hide()
            }
            break
          case InputDriver.Command.COMPLETE_INPUT:
            def text = event.input().text() + event.hint()
            event.driver().set(Input.of(text))
            break
          case InputDriver.Command.SUBMIT_INPUT:
            def command = event.input().text()
            event.driver().set(Input.EMPTY)
            input.hide()
            whisperer.hide()
            if ('now' == command)
              command = new Date().format('hh:mm dd.MM.yyyy')
            result = new ResultWindow(command)
            hookListener.result = result
            result.show()
            break
          case InputDriver.Command.CANCEL:
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
  void setInput(Input input) {
    setText(input.text())
    setCaretPosition(input.marker())
  }

  @Override
  void setHints(Hints hints) {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  def fillWhisperer(JTextArea whisperer, text) {
    def content = new StringBuilder()
    (1..9).each {
      content.append("$it - $text\n")
    }
    whisperer.setText(content.toString())
  }

}

class WhispererWindow extends JWindow {
  def text

  WhispererWindow(content) {
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent event) {
        System.exit(1)
      }
    })
    setSize(600, 300)
    getContentPane().setBackground(Color.BLACK)
    AWTUtilities.setWindowOpacity(this, 0.85f)
    text = new JTextArea(content, 5, 40)
    text.setBackground(Color.BLACK)
    text.setForeground(Color.YELLOW)
    text.setFont(new Font('Candara', Font.PLAIN, 30))
    text.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10))
    add(text)
  }
}

class ResultWindow extends JDialog {
  def text

  ResultWindow(content) {
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent event) {
        System.exit(1)
      }
    })
    setUndecorated(true)
    setSize(600, 300)
    setLocationRelativeTo(null)
    setAlwaysOnTop(true)
    getContentPane().setBackground(Color.BLACK)
    text = new JTextArea(content, 5, 40)
    text.setBackground(Color.BLACK)
    text.setForeground(Color.YELLOW)
    text.setFont(new Font('Candara', Font.PLAIN, 30))
    text.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10))
    text.setEditable(false)
    text.addKeyListener(new KeyAdapter() {
      @Override
      void keyPressed(KeyEvent e) {
        hide()
      }
    })
    add(text)
    AWTUtilities.setWindowOpacity(this, 0.85f)
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
