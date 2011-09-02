package org.sikora.ruler;

import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.JIntellitype;
import org.sikora.ruler.context.Context;
import org.sikora.ruler.context.ContextProvider;
import org.sikora.ruler.context.InputEventInContext;
import org.sikora.ruler.context.WindowsContextProvider;
import org.sikora.ruler.model.input.InputDriver;
import org.sikora.ruler.model.input.InputDriver.InputCommand;
import org.sikora.ruler.model.input.InputDriver.InputEvent;
import org.sikora.ruler.task.*;
import org.sikora.ruler.task.impl.BaseDefinitionRepository;
import org.sikora.ruler.task.impl.DefinitionDraftFactory;
import org.sikora.ruler.ui.awt.AwtInputDriver;
import org.sikora.ruler.ui.awt.AwtInputWindow;
import org.sikora.ruler.ui.awt.AwtResultWindow;

import static org.sikora.ruler.model.input.InputDriver.Command.*;

/**
 * Input driver handler. It provides context based hints and executes recognized tasks. It is activated by
 * global hot key.
 */
public class Ruler implements InputDriver.Listener, HotkeyListener {
  private final DraftFactory draftFactory;
  private final ContextProvider contextProvider;
  private Context currentContext;

  /**
   * Configures global key hooks and wires Ruler to it. It uses AwtInputDriver for receiving users input.
   *
   * @param args ignored
   */
  public static void main(final String[] args) {
    final AwtInputDriver inputDriver = new AwtInputDriver(new AwtInputWindow());
    final WindowsContextProvider contextProvider = new WindowsContextProvider(inputDriver, new AwtResultWindow());
    final DefinitionRepository definitionRepository = new BaseDefinitionRepository();
    Ruler ruler = new Ruler(contextProvider, definitionRepository);
    inputDriver.addListener(ruler);

    JIntellitype.setLibraryLocation("../lib/JIntellitype64.dll");
    JIntellitype hook = JIntellitype.getInstance();
    hook.addHotKeyListener(ruler);
    hook.registerHotKey(1, JIntellitype.MOD_SHIFT, (int) ' ');
    hook.registerHotKey(2, JIntellitype.MOD_CONTROL + JIntellitype.MOD_SHIFT, (int) ' ');
  }

  private Ruler(final ContextProvider contextProvider, DefinitionRepository definitionRepository) {
    this.contextProvider = contextProvider;
    this.currentContext = contextProvider.currentContext();
    this.draftFactory = new DefinitionDraftFactory(definitionRepository);
  }

  /**
   * Based on input driver inputEvent provides context based hints back to the driver.
   * It also executes recognized tasks.
   *
   * @param inputEvent input driver inputEvent
   */
  public void dispatch(final InputEvent inputEvent) {
    final InputEventInContext eventInContext = new InputEventInContext(inputEvent, currentContext);
    final Draft draft = draftFactory.draftFrom(eventInContext);
    switch (inputEvent.event()) {
      case SUBMIT_ISSUED:
        if (draft.isTaskComplete()) {
          final Task task = draft.toTask();
          final Result result = task.performAction();
          result.display();
        }
        break;
      case CANCEL_ISSUED:
        currentContext.inputDriver().issue(InputCommand.of(HIDE));
        currentContext.inputDriver().issue(InputCommand.of(RESET));
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
        currentContext.inputDriver().issue(InputCommand.of(FOCUS));
        break;
      case 2:
        currentContext.resultWindow().display();
        break;
      default:
        break;
    }
  }

}

