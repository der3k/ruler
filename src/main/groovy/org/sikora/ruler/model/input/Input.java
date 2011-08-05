package org.sikora.ruler.model.input;

/**
 * Value object encapsulating user's input.
 */
public final class Input {
  private static final String MARKER_PRESENTATION = "|";

  /**
   * Null input.
   */
  public static Input EMPTY = new Input("", 0);

  private final String text;
  private final int marker;

  /**
   * Creates new Input from text and marker index.
   *
   * @param text   input text
   * @param marker zero based marker position
   * @return Input from text with marker set
   * @throws IllegalArgumentException when text is null or when marker is out of boundary
   */
  public static Input of(final String text, final int marker) {
    return new Input(text, marker);
  }

  /**
   * Creates new Input from text with marker set at the end.
   *
   * @param text input text
   * @return Input from text with marker set after last character
   * @throws IllegalArgumentException when text is null
   */
  public static Input of(final String text) {
    return new Input(text, text.length());
  }

  private Input(final String text, final int marker) {
    if (text == null)
      throw new IllegalArgumentException("text cannot be null");
    if (marker < 0)
      throw new IllegalArgumentException("marker cannot be less than zero, was: " + marker);
    if (marker > text.length())
      throw new IllegalArgumentException(String.format("marker too big, max value for '%s' is: %d, was: %d", text, text.length(), marker));
    this.text = text;
    this.marker = marker;
  }

  /**
   * Creates new Input.Update based on current Input.
   *
   * @param value new Input value
   * @return Input.Update referring to this Input and new value
   */
  public Update updateTo(final Input value) {
    return new Update(value);
  }


  /**
   * Returns input text value.
   *
   * @return input text value
   */
  public String text() {
    return text;
  }

  /**
   * Returns marker value. The value is always inside the bounds of text.
   * Value ranges from 0 to text().length(). Value equal to text length means
   * that marker is set after the last text character.
   *
   * @return marker value
   */
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

  /**
   * Value object encapsulating Input change.
   */
  public class Update {
    private final Input value;

    private Update(final Input value) {
      if (value == null)
        throw new IllegalArgumentException("new input value cannot be null");
      this.value = value;
    }

    /**
     * Returns former input value.
     *
     * @return old input value
     */
    public Input oldValue() {
      return Input.this;
    }

    /**
     * Returns new input value.
     *
     * @return new input value
     */
    public Input newValue() {
      return value;
    }

    /**
     * Detects if the new value differs from the old one.
     *
     * @return true when the new value equals the old one, false otherwise
     */
    public boolean isVoid() {
      return Input.this.equals(value);
    }

    /**
     * Detects if the new value differs from the old one.
     *
     * @return true when the new value is not equals the old one, false otherwise
     */
    public boolean isNotVoid() {
      return !isVoid();
    }

    @Override
    public String toString() {
      return String.format("'%s' => '%s'", oldValue(), newValue());
    }
  }

}
