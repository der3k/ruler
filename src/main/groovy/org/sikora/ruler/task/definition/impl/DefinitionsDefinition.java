package org.sikora.ruler.task.definition.impl;

import org.sikora.ruler.context.InputEventInContext;
import org.sikora.ruler.model.input.Hints;
import org.sikora.ruler.model.input.Input;
import org.sikora.ruler.model.input.InputDriver;
import org.sikora.ruler.model.input.InputDriver.InputCommand;
import org.sikora.ruler.task.Task;
import org.sikora.ruler.task.definition.Definition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.sikora.ruler.model.input.InputDriver.Command.*;

/**
 * User: der3k
 * Date: 10.9.11
 * Time: 12:29
 */
public class DefinitionsDefinition implements Definition {
    final List<Match> matches;

    public DefinitionsDefinition(final List<Match> matches) {
        this.matches = matches;
        Collections.sort(matches);
    }

    public boolean handleEvent(final InputEventInContext eventInContext, final InputDriver inputDriver) {
        switch (eventInContext.event()) {
            case CHANGED:
                if (matches.isEmpty())
                    inputDriver.issue(InputCommand.of(HINT, Hints.NONE));
                else {
                    final ArrayList<Hints.Item> items = new ArrayList<Hints.Item>();
                    for (Match match : matches)
                        items.add(new Hints.Item(match.definition().name()));
                    inputDriver.issue(InputCommand.of(HINT, Hints.of(items)));
                }
                break;
            case COMPLETE_ISSUED:
                if (eventInContext.hint() != Hints.Item.NONE) {
                    inputDriver.issue(InputDriver.InputCommand.of(UPDATE, Input.of(eventInContext.hint().toString())));
                    inputDriver.issue(InputCommand.of(SUBMIT));
                    return false;
                }
                if (matches.size() > 0 && matches.get(0).isExact())
                    return true; // is definitive
                break;
            case SUBMIT_ISSUED:
                if (eventInContext.hint() != Hints.Item.NONE) {
                    inputDriver.issue(InputDriver.InputCommand.of(UPDATE, Input.of(eventInContext.hint().toString())));
                    inputDriver.issue(InputCommand.of(SUBMIT));
                    return false;
                }
                if (matches.size() > 0 && matches.get(0).isExact())
                    return true; // is definitive
                break;
        }
        return false;
    }

    public String name() {
        throw new UnsupportedOperationException();
    }

    public Match match(final InputEventInContext eventInContext) {
        throw new UnsupportedOperationException();
    }

    public Task newTask(final InputEventInContext event) {
        if (matches.size() == 0)
            throw new IllegalStateException("there are no matches available");
        return matches.get(0).definition().newTask(event);
    }
}
