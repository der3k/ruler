package cz.rsi.kdirector

// TODO how to hide result window, options: global key down or mouse move, timeout, hot key

import com.melloware.jintellitype.HotkeyListener
import com.melloware.jintellitype.JIntellitype
import com.sun.awt.AWTUtilities
import java.awt.Color
import java.awt.Font
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.*

class InputWindow extends JDialog {
    def hookListener

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
        def input = new InputField(this, hookListener)
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
    def hookListener

    InputField(input, hookListener) {
        super("")
        this.input = input
        this.hookListener = hookListener
        setFont(new Font('Candara', Font.PLAIN, 35))
        setFocusTraversalKeysEnabled(false)
        addKeyListener(new KeyAdapter() {

            @Override
            void keyReleased(KeyEvent e) {
                if (whisperer == null) {
                    whisperer = new ResultWindow(null)
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
            void keyPressed(KeyEvent event) {
                if (result != null) {
                    result.hide()
                    result = null
                }

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
                    hookListener.result = result
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
        (1..9).each {
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

JIntellitype.setLibraryLocation('C:/Users/sikorric/IdeaProjects/kdirector/lib/JIntellitype64.dll')
def hook = JIntellitype.getInstance()
def hookListener = new HookHotKeyListener()
def input = new InputWindow(hookListener)
hookListener.input = input
hook.addHotKeyListener(hookListener)
hook.registerHotKey(1, JIntellitype.MOD_CONTROL, (int) ' ')
hook.registerHotKey(2, JIntellitype.MOD_CONTROL + JIntellitype.MOD_SHIFT, (int) ' ')

