package org.sikora.ruler.task;

import org.sikora.ruler.ui.awt.AwtResultWindow;

/**
 * Defines task action result behavior.
 */
public interface Result {
  /**
   * Uses window to display itself.
   *
   * @param resultWindow window for result to show on
   */
  void showOn(AwtResultWindow resultWindow);
}
