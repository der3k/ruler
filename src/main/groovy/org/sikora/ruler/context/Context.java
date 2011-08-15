package org.sikora.ruler.context;

import org.sikora.ruler.model.input.InputDriver;
import org.sikora.ruler.ui.awt.AwtResultWindow;

/**
 * User: der3k
 * Date: 15.8.11
 * Time: 19:26
 */
public interface Context {
  InputDriver inputDriver();

  AwtResultWindow resultWindow();

  Window foregroundWindow();
}
