package org.sikora.ruler.task;

import org.sikora.ruler.model.input.InputDriver;

/**
 * Defines task draft behaviour.
 */
public interface Draft {
  void consumeEvent(InputDriver inputDriver);

  /**
   * Returns true when draft can define a task.
   *
   * @return true when task can be defined, false otherwise
   */
  boolean isDefinitive();

  /**
   * Returns task definition.
   *
   * @return task
   * @throws IllegalStateException when task cannot be defined
   */
  Task toTask();

  /**
   * Value object representing draft entry
   */
  public class Entry {
    private final String text;
    private final int mark;

    public Entry(final String text, final int mark) {
      this.text = text;
      this.mark = mark;
    }

    public String text() {
      return text;
    }

    public int mark() {
      return mark;
    }
  }
}
