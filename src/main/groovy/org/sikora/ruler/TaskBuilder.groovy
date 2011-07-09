package org.sikora.ruler

/**
 * User: der3k
 * Date: 9.7.11
 * Time: 11:29
 */
public interface TaskBuilder {
  boolean wouldBuild()

  Task build()

  void reset()

  void refresh(Draft blueprint)

  Hint[] hints()

}