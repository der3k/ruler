package org.sikora.ruler.ui

import com.sun.awt.AWTUtilities
import java.awt.Color
import java.awt.Font
import java.awt.Window
import javax.swing.BorderFactory
import javax.swing.JDialog
import javax.swing.JWindow
import javax.swing.text.JTextComponent

/**
 * User: der3k
 * Date: 16.7.11
 * Time: 11:19
 */
class AwtUtil {
  static void setLocationAndSize(Window window, int x, int y, int dx, int dy) {
    window.setLocation(x, y)
    window.setSize(dx, dy)
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
