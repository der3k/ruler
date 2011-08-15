package org.sikora.ruler.task;

import org.sikora.ruler.context.Context;
import org.sikora.ruler.model.input.InputDriver;

/**
 * Creates drafts from input driver events.
 */
public interface DraftFactory {
  /**
   * Returns task draft for input driver event.
   *
   *
   * @param event input driver event
   * @param context event context
   * @return task draft
   */
  Draft draftFrom(final InputDriver.Event event, final Context context);
}
