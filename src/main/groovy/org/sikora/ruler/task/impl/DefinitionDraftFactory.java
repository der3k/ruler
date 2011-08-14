package org.sikora.ruler.task.impl;

import org.sikora.ruler.model.input.InputDriver;
import org.sikora.ruler.task.*;

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
   * @param event input driver event
   * @return task draft created by task definition, or DEFINITIONS_DRAFT
   */
  public Draft draftFrom(final InputDriver.Event event) {
    final Definition definition = definitionRepository.find(event.input());
    final String text = event.input().text();
    switch (event.command()) {
      case UPDATE_INPUT:
        definition.onInputUpdate(event);
        break;
      case COMPLETE_INPUT:
        definition.onCompleteInput(event);
        break;
    }
    return new Draft() {

      public boolean isTaskComplete() {
        return definition.isCompleteFor(event.input());
      }

      public Task toTask() {
        return definition.createTask(event);
      }
    };
  }
}
