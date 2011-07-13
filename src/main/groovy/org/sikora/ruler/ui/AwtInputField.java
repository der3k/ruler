package org.sikora.ruler.ui;

import org.sikora.ruler.model.input.Hints;
import org.sikora.ruler.model.input.Input;

/**
 * User: sikorric
 * Date: 13.7.11
 * Time: 12:16
 */

public interface AwtInputField {
  public Input input();

  public void setInput(Input input);

  public void setHints(Hints hints);
}
