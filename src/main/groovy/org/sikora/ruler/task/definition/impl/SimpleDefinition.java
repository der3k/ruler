package org.sikora.ruler.task.definition.impl;

import org.sikora.ruler.context.InputEventInContext;
import org.sikora.ruler.model.input.Hints;
import org.sikora.ruler.model.input.Input;
import org.sikora.ruler.model.input.InputDriver;
import org.sikora.ruler.model.input.InputDriver.InputCommand;
import org.sikora.ruler.task.definition.Definition;

import java.math.BigDecimal;

import static org.sikora.ruler.model.input.InputDriver.Command.*;

public abstract class SimpleDefinition implements Definition {

  public Match match(final InputEventInContext inputEventInContext) {
    String text = inputEventInContext.input().activeText().toLowerCase();
    String name = name().trim().toLowerCase();
    if (text.isEmpty())
      return Match.of(Match.NONE, this);
    if (name.equals(text))
      return Match.of(Match.EXACT, this);
    if (name.startsWith(text)) {
      int match = BigDecimal.valueOf(text.length() * 100)
          .divideToIntegralValue(BigDecimal.valueOf(name.length())).intValue();
      return Match.of(match, this);
    }
    return Match.of(Match.NONE, this);
  }

  public void onInputUpdate(final InputEventInContext event, final InputDriver inputDriver) {
    inputDriver.issue(InputCommand.of(HINT, Hints.NONE));
  }

  public void onCompleteInput(final InputEventInContext event, final InputDriver inputDriver) {
    if (event.hint() != Hints.Item.NONE)
      inputDriver.issue(InputCommand.of(UPDATE, Input.of(event.hint().toString() + ' ')));
  }

  public boolean isCompleteFor(final InputEventInContext event) {
    return match(event).isExact();
  }

}
