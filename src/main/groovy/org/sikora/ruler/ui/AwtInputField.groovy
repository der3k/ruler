package org.sikora.ruler.ui

import java.awt.event.KeyListener
import javax.swing.JDialog
import javax.swing.JTextField
import javax.swing.JWindow
import org.sikora.ruler.model.input.Hints
import org.sikora.ruler.model.input.Input
import org.sikora.ruler.model.input.InputField
import javax.swing.JTextArea
import javax.swing.text.JTextComponent
import java.awt.Color
import java.awt.Font
import com.sun.awt.AWTUtilities
import java.awt.Window
import java.awt.Container
import org.rsi.kcommander.input
import javax.swing.BorderFactory

/**
 * User: der3k
 * Date: 16.7.11
 * Time: 11:19
 */
class AwtInputField implements InputField {
  FieldWindow fieldWindow
  HintsWindow hintsWindow;

  Input input() {
    return null  //To change body of implemented methods use File | Settings | File Templates.
  }

  void set(Input input) {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  void set(Hints hints) {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  void addKeyListener(KeyListener listener) {

  }

  void focus() {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  void hide() {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  class FieldWindow extends JDialog {
    JTextField text;

  }

  class HintsWindow extends JWindow {
    JTextArea text;

  }

  static void setLocationAndSize(Window window,int x, int y, int dx, int dy) {
    window.setLocation(x, y)
    window.setSize(dx,dy)
  }

  static void setFontAndColor(JTextComponent field, int fontSize) {
    field.setBackground(Color.BLACK)
    field.setForeground(Color.YELLOW)
    field.setFont(new Font('Candara', Font.PLAIN, fontSize))
  }

  static void setEmptyBorder(JTextComponent field, int size) {
    field.setBorder(BorderFactory.createEmptyBorder(size, size, size, size))
  }

  static void makeWindowOpaque(Window window) {
    if (window instanceof JWindow) {
      JWindow jWindow = ((JWindow) window)
      jWindow.getContentPane().setBackground(Color.BLACK)
    }
    if (window instanceof JDialog) {
      JDialog jDialog = ((JDialog) window)
      jDialog.setUndecorated(true)
      jDialog.getContentPane().setBackground(Color.BLACK)
    }
    AWTUtilities.setWindowOpacity(window, 0.85f)
  }

}
