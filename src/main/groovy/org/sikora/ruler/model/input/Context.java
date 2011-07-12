package org.sikora.ruler.model.input;

import org.sikora.ruler.Hint;

/**
 * User: sikorric
 * Date: 12.7.11
 * Time: 13:06
 */

public class Context {
  private final Broker broker;
  private final Input input;
  private final Hint[] hints;

  public Context(final Broker broker, final Input input, final Hint[] hints) {
    this.broker = broker;
    this.input = input;
    this.hints = hints;
  }

  public Broker broker() {
    return broker;
  }

  public Input input() {
    return input;
  }

  public Hint[] hints() {
    return hints;
  }
}
