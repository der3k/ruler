package cz.rsi.kdirector

import com.sun.awt.AWTUtilities
import java.awt.Color
import java.awt.Font
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.*
import com.melloware.jintellitype.JIntellitype
import com.melloware.jintellitype.HotkeyListener

class InputWindow extends JDialog {
  InputWindow() {
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent event) {
        JIntellitype.getInstance().unregisterHotKey(1)
        System.exit(1)
      }
    })
    setUndecorated(true)
    setSize(800, 50)
    setLocation(0, 0)
    getContentPane().setBackground(Color.BLACK)
    def input = new InputField(this)
    input.setBackground(Color.BLACK)
    input.setForeground(Color.YELLOW)
    input.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4))
    add(input)
    AWTUtilities.setWindowOpacity(this, 0.85f)
//    show()
  }
}

class InputField extends JTextField {
  def input
  def result
  def whisperer

  InputField(input) {
    super("")
    this.input = input
    setFont(new Font('Candara', Font.PLAIN, 35))
    setFocusTraversalKeysEnabled(false)
    addKeyListener(new KeyAdapter() {
      void keyPressed(KeyEvent event) {
        if (result != null) {
          result.hide()
          result = null
        }
        if (whisperer == null) {
          whisperer = new ResultWindow(null)
          whisperer.setLocation(0, 50)
          whisperer.setSize(600, 380)
          whisperer.show()
        }
        fillWhisperer(whisperer.text, getText() + event.getKeyChar())
        whisperer.show()

        def k = event.getKeyCode()
        println event.getKeyChar()
        if (k == KeyEvent.VK_ESCAPE) {
          event.consume()
          System.exit(1)
        }
        if (k == KeyEvent.VK_ENTER) {
          def command = getText()
          setText(null)
          input.hide()
          whisperer.hide()
          if ('now' == command)
            command = new Date().format('hh:mm dd.MM.yyyy')
          result = new ResultWindow(command)
          result.show()
          event.consume()
        }
        if (k == KeyEvent.VK_TAB) {
          setText(getText() + 'TAB')
          event.consume()
        }
      }
    })
  }

  def fillWhisperer(JTextArea whisperer, text) {
    def content = new StringBuilder()
    (1..10).each {
      content.append("$it - $text\n")
    }
    whisperer.setText(content.toString())
  }

}


class ResultWindow extends JWindow {
  def text

  ResultWindow(content) {
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent event) {
        System.exit(1)
      }
    })
    setSize(600, 300)
    getContentPane().setBackground(Color.BLACK)
    setLocationRelativeTo(null)
    setAlwaysOnTop(true)
    AWTUtilities.setWindowOpacity(this, 0.85f)
    text = new JTextArea(content, 5, 40)
    text.setBackground(Color.BLACK)
    text.setForeground(Color.YELLOW)
    text.setFont(new Font('Candara', Font.PLAIN, 30))
    text.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10))
    add(text)
  }
}

class HookHotKeyListener implements HotkeyListener {
  def input

  HookHotKeyListener(final input) {
    this.input = input
  }

  void onHotKey(int hookId) {
    switch (hookId) {
      case 1 :
          input.show()
        break;
      default:
        break;
    }
  }
}

def input = new InputWindow()
def hook = JIntellitype.getInstance()
hook.registerHotKey(1, JIntellitype.MOD_CONTROL, (int) ' ')
hook.addHotKeyListener(new HookHotKeyListener(input))

// TODO get real input text whisperer (hints), should evaluate text after key is applied
// TODO hide result window on global key down