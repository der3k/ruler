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

import org.slf4j.LoggerFactory
import javax.swing.*

import org.sikora.ruler.ui.AwtBroker
import org.sikora.ruler.model.input.InputDriver
import org.sikora.ruler.model.input.Input
import org.sikora.ruler.Hint
import org.sikora.ruler.model.input.BrokerListener
import org.sikora.ruler.model.input.Broker.UpdateEvent
import org.sikora.ruler.model.input.Broker.CompleteEvent
import org.sikora.ruler.model.input.Broker.CancelEvent
import org.sikora.ruler.model.input.Broker.SubmitEvent

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
    input = new InputFieldOld(this, hookListener)
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

class InputFieldOld extends JTextField implements InputDriver {
  def logger = LoggerFactory.getLogger(InputFieldOld.class)
  def result
  def whisperer
  def hookListener
  def static final List<Integer> ACTION_KEYS = [KeyEvent.VK_ESCAPE, KeyEvent.VK_TAB, KeyEvent.VK_ENTER]
  AwtBroker broker
  def input

  InputFieldOld(input, hookListener) {
    super("")
    this.input = input
    this.hookListener = hookListener
    setFont(new Font('Candara', Font.PLAIN, 35))
    setFocusTraversalKeysEnabled(false)
    broker = new AwtBroker(this)
    def listener = new BrokerListener() {
      @Override
      void onChange(UpdateEvent updateEvent) {
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
      }

      @Override
      void onComplete(CompleteEvent completeEvent) {
        def text = completeEvent.context().input().text() + "COMPLTED "
        completeEvent.context().broker().setInput(Input.of(text))
      }

      @Override
      void onCancel(CancelEvent cancelEvent) {
        System.exit(1)
      }

      @Override
      void onSubmit(SubmitEvent submitEvent) {
        def command = submitEvent.context().input().text()
        submitEvent.context().broker().setInput(Input.EMPTY)
        input.hide()
        whisperer.hide()
        if ('now' == command)
          command = new Date().format('hh:mm dd.MM.yyyy')
        result = new ResultWindow(command)
        hookListener.result = result
        result.show()
      }

    }

    broker.addListener(listener)
    addKeyListener(broker)
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

  Hint[] hints() {
    new Hint[0]  //To change body of implemented methods use File | Settings | File Templates.
  }

  void setHints(Hint[] hints) {
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
