package org.sikora.ruler.task;

import org.sikora.ruler.model.input.Input;

/**
 * Task definition repository.
 */
public interface DefinitionRepository {
  Definition find(Input input);
}
