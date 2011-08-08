package org.sikora.ruler.ui.awt;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * AWT window for displaying task results.
 */
public class AwtResultWindow extends JDialog {
  private final JTextArea textArea = new JTextArea();

  /**
   * Creates new result window, initially hidden.
   */
  public AwtResultWindow() {
    configureWindow();
    configureTextArea();
    add(textArea);
  }

  private void configureWindow() {
    setSize(600, 300);
    setLocationRelativeTo(null);
    setAlwaysOnTop(true);
    AwtUtil.makeWindowOpaque(this);
  }

  private void configureTextArea() {
    AwtUtil.setFontAndColor(textArea, 30);
    AwtUtil.setEmptyBorder(textArea, 10);
    textArea.setEditable(false);
    textArea.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        setVisible(false);
      }
    });
  }

  /**
   * Displays the window with previously set content.
   */
  public void display() {
    setVisible(true);
  }

  /**
   * Displays the window with given content.
   *
   * @param text result
   */
  public void display(String text) {
    textArea.setText(text);
    setVisible(true);
  }

}
