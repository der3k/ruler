package org.sikora.ruler.ui;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * User: der3k
 * Date: 23.7.11
 * Time: 21:17
 */
public class AwtResultWindow extends JDialog {
  private final JTextArea textArea = new JTextArea();

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

  public void display() {
    setVisible(true);
  }

  public void display(String text) {
    textArea.setText(text);
    setVisible(true);
  }

}
