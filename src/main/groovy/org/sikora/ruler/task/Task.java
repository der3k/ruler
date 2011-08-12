package org.sikora.ruler.task;

/**
 * Defines task behavior.
 */
public interface Task {
  /**
   * Performs task's action and returns result.
   *
   * @return result of action
   */
  Result performAction();
}
