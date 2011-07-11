package org.sikora.ruler;

/**
 * User: der3k
 * Date: 11.7.11
 * Time: 17:42
 */
public final class Input {
  private static final String MARKER_PRESENTATION = "|";

  public static Input EMPTY = new Input("", 0);

  private final String text;
  private final int marker;

  public static Input of(final String text, final int marker) {
    return new Input(text, marker);
  }

  public static Input of(final String text) {
    return new Input(text, text.length());
  }

  private Input(final String text, final int marker) {
    if (marker < 0)
      throw new IllegalArgumentException("marker cannot be less than zero, was: " + marker);
    if (marker > text.length())
      throw new IllegalArgumentException(String.format("marker too big, max value for '%s' is: %d, was: %d", text, text.length(), marker));
    this.text = text;
    this.marker = marker;
  }

  public Update updateTo(final Input value, final InputDevice.Action action) {
    return new Update(value, action);
  }

  public String text() {
    return text;
  }

  public int marker() {
    return marker;
  }

  @Override
  public String toString() {
    return marker() > 0
        ? text.substring(0, marker) + MARKER_PRESENTATION + text.substring(marker)
        : MARKER_PRESENTATION + text;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (!(o instanceof Input)) return false;

    final Input input = (Input) o;

    if (marker != input.marker) return false;
    if (text != null ? !text.equals(input.text) : input.text != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = text != null ? text.hashCode() : 0;
    result = 31 * result + marker;
    return result;
  }

  class Update {
    private final Input value;
    private final InputDevice.Action action;

    private Update(final Input value, final InputDevice.Action action) {
      if (value == null)
        throw new IllegalArgumentException("new input value cannot be null");
      if (action == null)
        throw new IllegalArgumentException("action cannot be null");
      this.value = value;
      this.action = action;
    }

    public Input oldValue() {
      return Input.this;
    }

    public Input newValue() {
      return value;
    }

    public InputDevice.Action action() {
      return action;
    }

    @Override
    public String toString() {
      return oldValue().toString() + " => " + newValue().toString();
    }
  }

}
