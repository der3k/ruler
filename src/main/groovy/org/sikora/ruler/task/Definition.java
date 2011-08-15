package org.sikora.ruler.task;

import org.sikora.ruler.context.InputEventInContext;
import org.sikora.ruler.model.input.Input;

/**
 * Task definition.
 */
public interface Definition {

  /**
   * Return this definition input matching..
   *
   * @param input input driver input
   * @return match
   */
  Match match(Input input);

  /**
   * Returns task name.
   *
   * @return task name
   */
  String name();

  /**
   * Responds to input update event.
   *
   * @param event input event in context
   */
  void onInputUpdate(final InputEventInContext event);

  /**
   * Responds to complete input event.
   *
   * @param event complete input event in context
   */
  void onCompleteInput(final InputEventInContext event);

  /**
   * Returns true if the definition is complete for given input.
   *
   * @param event input event in context
   * @return true if the input defines complete task, false otherwise
   */
  boolean isCompleteFor(final InputEventInContext event);

  /**
   * Creates new task from event.
   *
   * @param event input driver event in context
   * @return new task as defined by event's input
   * @throws IllegalArgumentException when event input does not define task completely
   */
  Task createTask(final InputEventInContext event);

  public final class Match implements Comparable<Match> {
    public static final int NONE = 0;
    public static final int EXACT = 100;

    private final Definition definition;
    private final int rate;

    public static Match of(final int rate, final Definition definition) {
      return new Match(rate, definition);
    }

    private Match(final int rate, Definition definition) {
      if (NONE > rate || rate > EXACT)
        throw new IllegalArgumentException("match rate must be in the interval [0..100], was: " + rate);
      if (definition == null)
        throw new IllegalArgumentException("definition cannot be null");
      this.definition = definition;
      this.rate = rate;
    }

    public Definition definition() {
      return definition;
    }

    public boolean isNone() {
      return rate == NONE;
    }

    public boolean isExact() {
      return rate == EXACT;
    }

    public boolean isPartial() {
      return NONE < rate && rate < EXACT;
    }

    public int compareTo(final Match other) {
      return other.rate - this.rate;
    }

    @Override
    public boolean equals(final Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      final Match match = (Match) o;

      if (rate != match.rate) return false;
      if (definition != null ? !definition.equals(match.definition) : match.definition != null) return false;

      return true;
    }

    @Override
    public int hashCode() {
      int result = definition != null ? definition.hashCode() : 0;
      result = 31 * result + rate;
      return result;
    }
  }
}
