package org.sikora.ruler.ui.awt;

import org.sikora.ruler.model.input.Hints;
import org.sikora.ruler.model.input.Input;
import org.sikora.ruler.model.input.InputField;

import javax.swing.*;
import java.awt.event.KeyListener;

/**
 * User: der3k
 * Date: 23.7.11
 * Time: 20:27
 */
public class AwtInputWindow implements InputField {
  private final JDialog window = new JDialog();
  private final JTextField textField = new JTextField();
  private final AwtHintsWindow hintsWindow = new AwtHintsWindow();

  public AwtInputWindow() {
    configureWindow();
    configureTextField();
    window.add(textField);
  }

  public void addKeyListener(final KeyListener keyListener) {
    textField.addKeyListener(keyListener);
  }

  public Input input() {
    return Input.of(textField.getText(), textField.getCaretPosition());
  }

  public void set(final Input input) {
    textField.setText(input.text());
    textField.setCaretPosition(input.marker());
  }

  public void set(final Hints hints) {
    hintsWindow.set(hints);
  }

  public void focus() {
    window.setVisible(true);
  }

  public void hide() {
    hintsWindow.hide();
    window.setVisible(false);
  }

  private void configureWindow() {
    AwtUtil.setLocationAndSize(window, 0, 0, 800, 50);
    AwtUtil.makeWindowOpaque(window);
  }

  private void configureTextField() {
    textField.setFocusTraversalKeysEnabled(false);
    AwtUtil.setFontAndColor(textField, 35);
    AwtUtil.setEmptyBorder(textField, 4);
  }
}
