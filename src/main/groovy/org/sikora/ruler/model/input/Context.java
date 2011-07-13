package org.sikora.ruler.model.input;

/**
 * User: sikorric
 * Date: 12.7.11
 * Time: 13:06
 */

public class Context {
  private final Broker broker;
  private final Input input;
  private final Hints hints;

  public Context(final Broker broker, final Input input, final Hints hints) {
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

  public Hints hints() {
    return hints;
  }
}
