package org.sikora.ruler;

import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.JIntellitype;
import org.sikora.ruler.model.input.InputDriver;
import org.sikora.ruler.model.input.InputDriver.Event;
import org.sikora.ruler.task.*;
import org.sikora.ruler.task.impl.BaseDefinitionRepository;
import org.sikora.ruler.task.impl.DefinitionDraftFactory;
import org.sikora.ruler.ui.awt.AwtInputDriver;
import org.sikora.ruler.ui.awt.AwtResultWindow;

import static org.sikora.ruler.model.input.InputDriver.Command.*;

/**
 * Input driver handler. It provides context based hints and executes recognized tasks. It is activated by
 * global hot key.
 */
public class Ruler implements InputDriver.Handler, HotkeyListener {
  final InputDriver inputDriver;
  final DraftFactory draftFactory;
  final AwtResultWindow resultWindow;

  /**
   * Configures global key hooks and wires Ruler to it. It uses AwtInputDriver for receiving users input.
   *
   * @param args ignored
   */
  public static void main(final String[] args) {
    InputDriver driver = new AwtInputDriver();
    DefinitionRepository definitionRepository = new BaseDefinitionRepository();
    Ruler ruler = new Ruler(driver, definitionRepository, new AwtResultWindow());
    driver.addHandler(ruler);

    JIntellitype.setLibraryLocation("../lib/JIntellitype64.dll");
    JIntellitype hook = JIntellitype.getInstance();
    hook.addHotKeyListener(ruler);
    hook.registerHotKey(1, JIntellitype.MOD_CONTROL, (int) ' ');
    hook.registerHotKey(2, JIntellitype.MOD_CONTROL + JIntellitype.MOD_SHIFT, (int) ' ');
  }

  private Ruler(final InputDriver inputDriver, final DefinitionRepository definitionRepository, final AwtResultWindow resultWindow) {
    this.inputDriver = inputDriver;
    this.draftFactory = new DefinitionDraftFactory(definitionRepository);
    this.resultWindow = resultWindow;
  }

  /**
   * Based on input driver event provides context based hints back to the driver.
   * It also executes recognized tasks.
   *
   * @param event input driver event
   */
  public void dispatch(final Event event) {
    final Draft draft = draftFactory.draftFrom(event);
    switch (event.command()) {
      case SUBMIT_INPUT:
        if (draft.isTaskComplete()) {
          final Task task = draft.toTask();
          final Result result = task.performAction();
          result.showOn(resultWindow);
        }
        break;
      case CANCEL:
        event.driver().issue(HIDE_INPUT);
        event.driver().issue(RESET_INPUT);
        break;
    }
  }

  /**
   * Global hot key hook. It focuses input window or result window based on a hot key.
   *
   * @param hook hook id
   */
  public void onHotKey(final int hook) {
    switch (hook) {
      case 1:
        inputDriver.issue(FOCUS_INPUT);
        break;
      case 2:
        resultWindow.display();
        break;
      default:
        break;
    }
  }

}

