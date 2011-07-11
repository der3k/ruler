package org.sikora.ruler;

/**
 * User: der3k
 * Date: 11.7.11
 * Time: 18:28
 */
public interface InputDevice {
  public void input(Input input);

  public void addListener(InputListener listener);

  public void removeListener(InputListener listener);

  

}
