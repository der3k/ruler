package org.sikora.ruler.model.input;

/**
 * Defines behaviour of input device field.
 */
public interface InputField {
  /**
   * Returns current input value.
   *
   * @return input value
   */
  public Input input();

  /**
   * Sets input value.
   *
   * @param input new input value
   */
  public void set(Input input);

  /**
   * Sets hints.
   *
   * @param hints new hints
   */
  public void set(Hints hints);

  /**
   * Starts receiving input.
   */
  public void focus();

  /**
   * Stops receiving input.
   */
  public void hide();
}
