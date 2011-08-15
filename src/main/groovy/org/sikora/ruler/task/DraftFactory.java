package org.sikora.ruler.task;

import org.sikora.ruler.context.InputEventInContext;

/**
 * Creates drafts from input driver events.
 */
public interface DraftFactory {
  /**
   * Returns task draft for input driver event and context.
   *
   * @param inputEventInContext input driver event
   * @return task draft
   */
  Draft draftFrom(final InputEventInContext inputEventInContext);
}
