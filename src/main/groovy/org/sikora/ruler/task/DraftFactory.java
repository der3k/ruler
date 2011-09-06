package org.sikora.ruler.task;

import org.sikora.ruler.context.InputEventInContext;

/**
 * Creates drafts from input driver events.
 */
public interface DraftFactory {
  /**
   * Returns task draft for input driver event and context.
   *
   * @return task draft
   */
  Draft draftFor(InputEventInContext eventInContext);
}
