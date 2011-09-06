package org.sikora.ruler.task.definition;

import org.sikora.ruler.context.InputEventInContext;

/**
 * Task definition repository.
 */
public interface DefinitionRepository {
  Definition find(InputEventInContext eventInContext);
}
