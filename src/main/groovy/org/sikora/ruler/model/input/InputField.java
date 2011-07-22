package org.sikora.ruler.model.input;

import org.sikora.ruler.model.input.Hints;
import org.sikora.ruler.model.input.Input;

/**
 * User: sikorric
 * Date: 13.7.11
 * Time: 12:16
 */

public interface InputField {
  public Input input();

  public void set(Input input);

  public void set(Hints hints);

  public void focus();

  public void hide();
}
