package org.sikora.ruler.ui.awt;

import com.sun.awt.AWTUtilities;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;

/**
 * AWT utility functions.
 */
public class AwtUtil {

  /**
   * Sets location on size to a window.
   *
   * @param window window that would be updated
   * @param x      x position
   * @param y      y position
   * @param dx     width
   * @param dy     height
   */
  static void setLocationAndSize(final Window window, final int x, final int y, final int dx, final int dy) {
    window.setLocation(x, y);
    window.setSize(dx, dy);
  }

  /**
   * Sets background and foreground color together with font size to a text component.
   *
   * @param field    text component that would be updated
   * @param fontSize font size
   */
  public static void setFontAndColor(final JTextComponent field, final int fontSize) {
    field.setBackground(Color.BLACK);
    field.setForeground(Color.YELLOW);
    field.setFont(new Font("Candara", Font.PLAIN, fontSize));
  }

  /**
   * Sets empty border of given size to a text component.
   *
   * @param field text component that would be updated
   * @param size  border size in pixels
   */
  public static void setEmptyBorder(final JTextComponent field, final int size) {
    field.setBorder(BorderFactory.createEmptyBorder(size, size, size, size));
  }

  /**
   * Makes a window opaque.
   *
   * @param window window that would be updated
   */
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
