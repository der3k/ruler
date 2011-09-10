package org.sikora.ruler.task.definition.impl;

import com.melloware.jintellitype.JIntellitype;
import org.sikora.ruler.context.InputEventInContext;
import org.sikora.ruler.incubation.fileTasks;
import org.sikora.ruler.model.input.Hints;
import org.sikora.ruler.model.input.Input;
import org.sikora.ruler.model.input.InputDriver;
import org.sikora.ruler.model.input.InputDriver.InputCommand;
import org.sikora.ruler.task.Task;
import org.sikora.ruler.task.definition.Definition;
import org.sikora.ruler.task.definition.DefinitionRepository;
import org.sikora.ruler.ui.awt.AwtResultWindow;

import java.util.*;

import static org.sikora.ruler.model.input.InputDriver.Command.*;

/**
 * Base definition repository, returning partially matched definitions
 * when no exact match found.
 */
public class BaseDefinitionRepository implements DefinitionRepository {
  private final Set<Definition> definitions = new HashSet<Definition>();

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
     definition =  exactMatches.get(0).definition();
    else if (exactMatches.size() > 1)
      definition = new DefinitionsDefinition(exactMatches);
    else
      definition = new DefinitionsDefinition(partialMatches);
    return definition;
   }

  /**
   * Creates repository with predefined definitions.
   */
  public BaseDefinitionRepository() {
    definitions.add(new DefinitionHelper("now") {
      @Override
      public Task newTask(final InputEventInContext event) {
        return new Task() {
          public void execute(final AwtResultWindow resultWindow) {
            resultWindow.display(new Date().toString());
          }
        };
      }
    });
    definitions.add(new DefinitionHelper("quit") {
      @Override
      public Task newTask(final InputEventInContext event) {
        JIntellitype.getInstance().cleanUp();
        System.exit(0);
        return null;
      }
    });
    definitions.add(new DefinitionHelper("quote") {
    });
    definitions.add(new DefinitionHelper("notify") {
    });
    definitions.add(new DefinitionHelper("minimize") {
      @Override
      public Task newTask(final InputEventInContext event) {
        return new Task() {
          public void execute(final AwtResultWindow resultWindow) {
            event.foregroundWindow().minimize();
          }
        };
      }
    });
    definitions.addAll((Collection<? extends Definition>) new fileTasks().run());
  }

  public class DefinitionHelper extends SimpleDefinition {
    private final String name;

    protected DefinitionHelper(final String name) {
      this.name = name;
    }

    public String name() {
      return name;
    }

    public Task newTask(final InputEventInContext event) {
      return new Task() {
        public void execute(final AwtResultWindow resultWindow) {
          resultWindow.display(name);
        }
      };
    }
  }
}
