package org.sikora.ruler.task.definition.impl;

import org.sikora.ruler.context.InputEventInContext;
import org.sikora.ruler.task.Task;
import org.sikora.ruler.task.definition.Definition;
import org.sikora.ruler.task.definition.DefinitionFactory;
import org.sikora.ruler.task.definition.DefinitionRepository;
import org.sikora.ruler.task.definition.ReloadableDefinitionFactory;
import org.sikora.ruler.ui.awt.AwtResultWindow;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Base definition repository, returning partially matched definitions
 * when no exact match found.
 */
public class BaseDefinitionRepository implements DefinitionRepository {
    private final DefinitionFactory[] factories;
    private final Set<Definition> definitions = new HashSet<Definition>();

    public BaseDefinitionRepository(final DefinitionFactory... factories) {
        this.factories = factories;
        reload();
    }

    public void reload() {
        definitions.clear();
        for (DefinitionFactory factory : factories) {
            if (factory instanceof ReloadableDefinitionFactory)
                ((ReloadableDefinitionFactory) factory).reload();
            for (Definition definition : factory.definitions()) {
                definitions.add(definition);
                System.out.println("added '" + definition.name() + "'");
            }
        }
        definitions.add(reloadDefinition());
    }

    private Definition reloadDefinition() {
        return new DefinitionHelper("reload") {
            @Override
            public Task newTask(final InputEventInContext event) {
                return new Task() {
                    public void execute(final AwtResultWindow resultWindow) {
                        reload();
                    }
                };
            }
        };

    }

    /**
     * Returns first definition that exactly matches input or custom
     * definition containing all partial definition matches.
     *
     * @param eventInContext input driver input
     * @return exactly matching definition
     */
    public Definition find(InputEventInContext eventInContext) {
        final List<Definition.Match> exactMatches = new ArrayList<Definition.Match>();
        final List<Definition.Match> partialMatches = new ArrayList<Definition.Match>();
        for (Definition definition : definitions) {
            final Definition.Match match = definition.match(eventInContext);
            if (match.isExact())
                exactMatches.add(match);
            else if (match.isPartial())
                partialMatches.add(match);
        }
        Definition definition;
        if (exactMatches.size() == 1)
            definition = exactMatches.get(0).definition();
        else if (exactMatches.size() > 1)
            definition = new DefinitionsDefinition(exactMatches);
        else
            definition = new DefinitionsDefinition(partialMatches);
        return definition;
    }

}
