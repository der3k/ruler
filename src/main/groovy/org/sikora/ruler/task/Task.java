package org.sikora.ruler.task;

/**
 * Defines task behavior.
 */
public interface Task {
  /**
   * Performs task's event and returns result.
   *
   * @return result of event
   */
  Result performAction();
}
