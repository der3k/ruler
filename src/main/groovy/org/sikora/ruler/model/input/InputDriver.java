package org.sikora.ruler.model.input;

import org.sikora.ruler.Hint;

/**
 * User: sikorric
 * Date: 12.7.11
 * Time: 12:49
 */

public interface InputDriver {
  public Input input();

  public void setInput(Input input);

  public Hint[] hints();

  public void setHints(Hint[] hints);

}
