package org.sikora.ruler.task.impl;

import com.melloware.jintellitype.JIntellitype;
import org.sikora.ruler.context.InputEventInContext;
import org.sikora.ruler.model.input.Hints;
import org.sikora.ruler.model.input.Input;
import org.sikora.ruler.task.Definition;
import org.sikora.ruler.task.DefinitionRepository;
import org.sikora.ruler.task.Result;
import org.sikora.ruler.task.Task;

import java.util.*;

import static org.sikora.ruler.model.input.InputDriver.Action.*;

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
   * @param input input driver input
   * @return exactly matching definition
   */
  public Definition find(final Input input) {
    final List<Definition.Match> partialMatches = new ArrayList<Definition.Match>();
    for (Definition definition : definitions) {
      final Definition.Match match = definition.match(input);
      if (match.isExact())
        return definition;
      if (match.isPartial())
        partialMatches.add(match);
    }
    Collections.sort(partialMatches);

    return new Definition() {
      public Match match(final Input input) {
        return Match.of(Match.EXACT, this);
      }

      public String name() {
        return "allTasks";
      }

      public void onInputUpdate(final InputEventInContext event) {
        if (partialMatches.size() == 0) {
          event.inputDriver().issue(UPDATE_HINTS, Hints.NONE);
          return;
        }
        final ArrayList<Hints.Item> items = new ArrayList<Hints.Item>();
        for (Match match : partialMatches)
          items.add(new Hints.Item(match.definition().name()));
        event.inputDriver().issue(UPDATE_HINTS, new Hints(items));
      }

      public void onCompleteInput(final InputEventInContext event) {
        if (event.hint() != Hints.Item.NONE) {
          event.inputDriver().issue(UPDATE_INPUT, Input.of(event.hint().toString() + ' '));
        }
      }

      public boolean isCompleteFor(final Input input) {
        return false;
      }

      public Task createTask(final InputEventInContext event) {
        throw new IllegalArgumentException("not supported operation");
      }
    };
  }

  /**
   * Creates repository with predefined definitions.
   */
  public BaseDefinitionRepository() {
    definitions.add(new DefinitionHelper("now") {
      @Override
      public Task createTask(final InputEventInContext event) {
        return new Task() {
          public Result performAction() {
            event.inputDriver().issue(HIDE_INPUT);
            event.inputDriver().issue(RESET_INPUT);
            return new Result() {
              public void display() {
                event.resultWindow().display(new Date().toString());
              }
            };
          }
        };
      }
    });
    definitions.add(new DefinitionHelper("quit") {
      @Override
      public Task createTask(final InputEventInContext event) {
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
      public Task createTask(final InputEventInContext event) {
        return new Task() {
          public Result performAction() {
            event.inputDriver().issue(HIDE_INPUT);
            event.inputDriver().issue(RESET_INPUT);
            event.foregroundWindow().minimize();
            return new Result() {
              public void display() {
                // nothing to display
              }
            };
          }
        };
      }
    });
  }

  public class DefinitionHelper implements Definition {
    private final String name;

    protected DefinitionHelper(final String name) {
      this.name = name;
    }

    public Match match(final Input input) {
      String text = input.text().trim();
      if (text.isEmpty())
        return Match.of(Match.NONE, this);
      if (name.equals(text))
        return Match.of(Match.EXACT, this);
      if (name.startsWith(text))
        return Match.of(50, this);
      return Match.of(Match.NONE, this);
    }

    public String name() {
      return name;
    }

    public void onInputUpdate(final InputEventInContext event) {
      event.inputDriver().issue(UPDATE_HINTS, Hints.NONE);
    }

    public void onCompleteInput(final InputEventInContext event) {
      if (event.hint() != Hints.Item.NONE) {
        event.inputDriver().issue(UPDATE_INPUT, Input.of(event.hint().toString() + ' '));
      }
    }

    public boolean isCompleteFor(final Input input) {
      return match(input).isExact();
    }

    public Task createTask(final InputEventInContext event) {
      return new Task() {
        public Result performAction() {
          return new Result() {
            public void display() {
              event.inputDriver().issue(HIDE_INPUT);
              event.inputDriver().issue(RESET_INPUT);
              event.resultWindow().display(name);
            }
          };
        }
      };
    }
  }
}
