package org.sikora.ruler.task;

import org.sikora.ruler.model.input.Input;

/**
 * Task definition.
 */
public interface Definition {


  /**
   * Value object for task name.
   */
  public class TaskName {
    private final String value;

    /**
     * Creates new task name from text. It can consist only from letters and digits,
     * no punctuation or 'space' characters are allowed.
     *
     * @param name task name
     * @throws IllegalArgumentException when encountered illegal characters in the name
     */
    public TaskName(final String name) {
      this.value = name;
    }

    public boolean matches(Input input) {
      return input.text().startsWith(value + " ");
    }
  }
}
