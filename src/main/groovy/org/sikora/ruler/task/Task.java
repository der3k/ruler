package org.sikora.ruler.task;

import org.sikora.ruler.ui.awt.AwtResultWindow;

/**
 * Defines task behavior.
 */
public interface Task {
  /**
   * Performs task's event and returns result.
   *
   * @return result of event
   */
  void execute(AwtResultWindow resultWindow);
}
