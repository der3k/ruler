package org.sikora.ruler.task;

import org.sikora.ruler.model.input.InputDriver;

/**
 * Creates drafts from input driver events.
 */
public interface DraftFactory {
  /**
   * Returns task draft for input driver event.
   *
   * @param event input driver event
   * @return task draft
   */
  Draft draftFrom(InputDriver.Event event);
}
