package org.sikora.ruler.task;

import com.melloware.jintellitype.JIntellitype;
import org.sikora.ruler.model.input.Hints;
import org.sikora.ruler.model.input.Input;
import org.sikora.ruler.model.input.InputDriver;
import org.sikora.ruler.ui.awt.AwtResultWindow;

import java.util.ArrayList;
import java.util.Date;

import static org.sikora.ruler.model.input.InputDriver.Command.*;

/**
 * User: der3k
 * Date: 12.8.11
 * Time: 21:58
 */
public class DefinitionDraftFactory implements DraftFactory {

  /**
   * Returns task draft utilizing DefinitionRepository. If Definition repository
   * does not find definition for the event's input it returns DEFINITIONS_DRAFT
   * that provides definition hints for available tasks.
   *
   * @param event input driver event
   * @return task draft created by task definition, or DEFINITIONS_DRAFT
   */
  public Draft draftFrom(final InputDriver.Event event) {
    final String text = event.input().text();
    switch (event.command()) {
      case UPDATE_INPUT:
        Hints hints = Hints.NONE;
        ArrayList<Hints.Item> items = new ArrayList<Hints.Item>();
        if (!text.trim().isEmpty() && "no".startsWith(text.trim()))
          items.add(new Hints.Item("now"));
        if (!text.trim().isEmpty() && "quit".startsWith(text.trim()))
          items.add(new Hints.Item("quit"));
        if (items.size() > 0)
          hints = new Hints(items);
        event.driver().set(hints);
        break;
      case COMPLETE_INPUT:
        if (event.hint() != Hints.Item.NONE) {
          event.driver().set(Input.of(event.hint().toString() + ' '));
        }
        break;
    }
    return new Draft() {

      public boolean isTaskComplete() {
        return "now".equals(text.trim()) || "quit".equals(text.trim());
      }

      public Task toTask() {
        if (!isTaskComplete())
          throw new IllegalStateException("task is not defined");
        if ("now".equals(text.trim())) {
          return new Task() {
            public Result performAction() {
              event.driver().issue(HIDE_INPUT);
              event.driver().issue(RESET_INPUT);
              return new Result() {
                public void showOn(final AwtResultWindow resultWindow) {
                  resultWindow.display(new Date().toString());
                }
              };
            }
          };
        }
        if ("quit".equals(text.trim())) {
          JIntellitype.getInstance().cleanUp();
          System.exit(0);
        }
        return null; // should not get here
      }
    };
  }
}
