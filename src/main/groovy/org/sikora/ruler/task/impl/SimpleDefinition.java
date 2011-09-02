package org.sikora.ruler.task.impl;

import org.sikora.ruler.context.InputEventInContext;
import org.sikora.ruler.model.input.Hints;
import org.sikora.ruler.model.input.Input;
import org.sikora.ruler.model.input.InputDriver.InputCommand;
import org.sikora.ruler.task.Definition;

import java.math.BigDecimal;

import static org.sikora.ruler.model.input.InputDriver.Command.*;

public abstract class SimpleDefinition implements Definition {

  public Match match(final Input input) {
    String text = input.activeText().toLowerCase();
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

  public void onInputUpdate(final InputEventInContext event) {
    event.inputDriver().issue(InputCommand.of(HINT, Hints.NONE));
  }

  public void onCompleteInput(final InputEventInContext event) {
    if (event.hint() != Hints.Item.NONE)
      event.inputDriver().issue(InputCommand.of(UPDATE, Input.of(event.hint().toString() + ' ')));
  }

  public boolean isCompleteFor(final InputEventInContext event) {
    return match(event.input()).isExact();
  }

}
