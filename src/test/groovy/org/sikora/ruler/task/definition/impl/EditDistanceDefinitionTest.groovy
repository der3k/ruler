package org.sikora.ruler.task.definition.impl

import org.sikora.ruler.context.InputEventInContext
import org.sikora.ruler.model.input.Hints
import org.sikora.ruler.model.input.Input
import org.sikora.ruler.model.input.InputDriver
import org.sikora.ruler.task.Task
import spock.lang.Specification

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 14.7.12
 * Time: 17:09
 * To change this template use File | Settings | File Templates.
 */
class EditDistanceDefinitionTest extends Specification {
    def "empty string matches not at all"() {
        EditDistanceDefinition definition = new EditDistanceDefinition() {
            @Override
            String name() { return "task" }

            @Override
            Task newTask(InputEventInContext event) { return null }
        };
        def event = new InputDriver.InputEvent(InputDriver.Event.CHANGED, Input.of(input), Hints.Item.NONE)
        def eventInContext = new InputEventInContext(event, null)
        def match = definition.match(eventInContext)
        expect:
        match.isPartial()

        where:
        input << ['k', 'sk', 'ask', 't', 'ta', 'tas' ]
    }
}
