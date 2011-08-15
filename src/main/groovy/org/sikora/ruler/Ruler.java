package org.sikora.ruler;

import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.JIntellitype;
import org.sikora.ruler.context.Context;
import org.sikora.ruler.context.ContextProvider;
import org.sikora.ruler.context.InputEventInContext;
import org.sikora.ruler.context.WindowsContextProvider;
import org.sikora.ruler.model.input.InputDriver;
import org.sikora.ruler.model.input.InputDriver.Event;
import org.sikora.ruler.task.*;
import org.sikora.ruler.task.impl.BaseDefinitionRepository;
import org.sikora.ruler.task.impl.DefinitionDraftFactory;
import org.sikora.ruler.ui.awt.AwtInputDriver;
import org.sikora.ruler.ui.awt.AwtResultWindow;

import static org.sikora.ruler.model.input.InputDriver.Action.*;

/**
 * Input driver handler. It provides context based hints and executes recognized tasks. It is activated by
 * global hot key.
 */
public class Ruler implements InputDriver.Handler, HotkeyListener {
  private final DraftFactory draftFactory;
  private final ContextProvider contextProvider;
  private Context currentContext;

  /**
   * Configures global key hooks and wires Ruler to it. It uses AwtInputDriver for receiving users input.
   *
   * @param args ignored
   */
  public static void main(final String[] args) {
    final AwtInputDriver inputDriver = new AwtInputDriver();
    final WindowsContextProvider contextProvider = new WindowsContextProvider(inputDriver, new AwtResultWindow());
    final DefinitionRepository definitionRepository = new BaseDefinitionRepository();
    Ruler ruler = new Ruler(contextProvider, definitionRepository);
    inputDriver.addHandler(ruler);

    JIntellitype.setLibraryLocation("../lib/JIntellitype64.dll");
    JIntellitype hook = JIntellitype.getInstance();
    hook.addHotKeyListener(ruler);
    hook.registerHotKey(1, JIntellitype.MOD_CONTROL, (int) ' ');
    hook.registerHotKey(2, JIntellitype.MOD_CONTROL + JIntellitype.MOD_SHIFT, (int) ' ');
  }

  private Ruler(final ContextProvider contextProvider, DefinitionRepository definitionRepository) {
    this.contextProvider = contextProvider;
    this.currentContext = contextProvider.currentContext();
    this.draftFactory = new DefinitionDraftFactory(definitionRepository);
  }

  /**
   * Based on input driver event provides context based hints back to the driver.
   * It also executes recognized tasks.
   *
   * @param event input driver event
   */
  public void dispatch(final Event event) {
    final InputEventInContext eventInContext = new InputEventInContext(event, currentContext);
    final Draft draft = draftFactory.draftFrom(eventInContext);
    switch (event.action()) {
      case SUBMIT_INPUT:
        if (draft.isTaskComplete()) {
          final Task task = draft.toTask();
          final Result result = task.performAction();
          result.display();
        } else {
          currentContext.inputDriver().issue(COMPLETE_INPUT);
          currentContext.inputDriver().issue(SUBMIT_INPUT);
        }
        break;
      case CANCEL:
        currentContext.inputDriver().issue(HIDE_INPUT);
        currentContext.inputDriver().issue(RESET_INPUT);
        break;
    }
  }

  /**
   * Global hot key hook. It focuses input window or result window based on a hot key.
   *
   * @param hook hook id
   */
  public void onHotKey(final int hook) {
    currentContext = contextProvider.currentContext();
    switch (hook) {
      case 1:
        currentContext.inputDriver().issue(FOCUS_INPUT);
        break;
      case 2:
        currentContext.resultWindow().display();
        break;
      default:
        break;
    }
  }

}

