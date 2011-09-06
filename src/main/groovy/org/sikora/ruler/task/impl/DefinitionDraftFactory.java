package org.sikora.ruler.task.impl;

import org.sikora.ruler.context.InputEventInContext;
import org.sikora.ruler.model.input.InputDriver;
import org.sikora.ruler.task.*;
import org.sikora.ruler.task.definition.Definition;
import org.sikora.ruler.task.definition.DefinitionRepository;

/**
 * User: der3k
 * Date: 12.8.11
 * Time: 21:58
 */
public class DefinitionDraftFactory implements DraftFactory {
  private final DefinitionRepository definitionRepository;

  public DefinitionDraftFactory(final DefinitionRepository definitionRepository) {
    this.definitionRepository = definitionRepository;
  }

  /**
   * Returns task draft utilizing DefinitionRepository. If Definition repository
   * does not find definition for the event's input it returns DEFINITIONS_DRAFT
   * that provides definition hints for available tasks.
   *
   * @param eventInContext input driver event in context
   * @return task draft created by task definition, or DEFINITIONS_DRAFT
   */
  public Draft draftFor(final InputEventInContext eventInContext) {
    final Definition definition = definitionRepository.find(eventInContext);
    return new Draft() {
      boolean definitive = false;
      public void consumeEvent(final InputDriver inputDriver) {
        switch (eventInContext.event()) {
          case CHANGED:
            definition.onInputUpdate(eventInContext, inputDriver);
            break;
          case COMPLETE_ISSUED:
            definition.onCompleteInput(eventInContext, inputDriver);
            break;
          case SUBMIT_ISSUED:
            definitive = definition.isCompleteFor(eventInContext);
        }
      }

      public boolean isDefinitive() {
        return definitive;
      }

      public Task toTask() {
        return definition.newTask(eventInContext);
      }
    };
  }
}
