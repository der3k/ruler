package org.sikora.ruler.task.impl;


import org.sikora.ruler.model.input.Input
import spock.lang.Specification
import org.sikora.ruler.task.impl.BaseDefinitionRepository

/**
 * User: der3k
 * Date: 14.8.11
 * Time: 15:30
 */
public class BaseDefinitionRepositoryTest extends Specification {
  def 'has predefined definitions'() {
    def repo = new BaseDefinitionRepository()
    def definition = repo.find(Input.of('now'))
  expect:
    definition.name() == 'now'
  }
}
