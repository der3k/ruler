package org.sikora.ruler.ui.awt;

import org.sikora.ruler.model.input.Hints;

import javax.swing.*;

/**
 * User: der3k
 * Date: 23.7.11
 * Time: 20:41
 */
public class AwtHintsWindow {
  private final JWindow window = new JWindow();
  private final JTextArea textArea = new JTextArea();

  public AwtHintsWindow() {
    AwtUtil.setLocationAndSize(window, 0, 50, 600, 355);
    AwtUtil.makeWindowOpaque(window);
    configureTextArea();
    window.add(textArea);
  }

  private void configureTextArea() {
    AwtUtil.setFontAndColor(textArea, 30);
    AwtUtil.setEmptyBorder(textArea, 10);
  }

  public void hide() {
    window.setVisible(false);
  }

  public void set(Hints hints) {
    if (hints.size() == 0) {
      window.setVisible(false);
    } else {
      populateHints(hints);
      window.setVisible(true);
    }
  }

  private void populateHints(Hints hints) {
    int size = Math.min(9, hints.size());
    Hints.Item[] items = hints.items();
    final StringBuilder content = new StringBuilder();
    for (int i = 1; i < size; i++)
      appendItemLabel(content, i, items[i - 1]).append("\n");
    appendItemLabel(content, size, items[size - 1]);
    textArea.setText(content.toString());
    window.setSize(600, size * 40);
  }

  private StringBuilder appendItemLabel(final StringBuilder builder, final int i, final Hints.Item item) {
    builder.append(i).append(" - ").append(item);
    return builder;
  }

}
