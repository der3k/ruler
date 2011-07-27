package org.rsi.kcommander

import org.sikora.ruler.model.input.Hints
import org.rsi.kcommander.Draft
import org.rsi.kcommander.Task

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

  Hints hints()

}