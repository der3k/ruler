package org.sikora.ruler.ui.awt;

import com.sun.awt.AWTUtilities;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;

/**
 * User: der3k
 * Date: 16.7.11
 * Time: 11:19
 */
class AwtUtil {
  static void setLocationAndSize(final Window window, final int x, final int y, final int dx, final int dy) {
    window.setLocation(x, y);
    window.setSize(dx, dy);
  }

  public static void setFontAndColor(final JTextComponent field, final int fontSize) {
    field.setBackground(Color.BLACK);
    field.setForeground(Color.YELLOW);
    field.setFont(new Font("Candara", Font.PLAIN, fontSize));
  }

  public static void setEmptyBorder(final JTextComponent field, final int size) {
    field.setBorder(BorderFactory.createEmptyBorder(size, size, size, size));
  }

  public static void makeWindowOpaque(final Window window) {
    if (window instanceof JWindow) {
      JWindow jWindow = ((JWindow) window);
      jWindow.getContentPane().setBackground(Color.BLACK);
    }
    if (window instanceof JDialog) {
      JDialog jDialog = ((JDialog) window);
      jDialog.setUndecorated(true);
      jDialog.getContentPane().setBackground(Color.BLACK);
    }
    AWTUtilities.setWindowOpacity(window, 0.85f);
  }

}
