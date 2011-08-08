package org.sikora.ruler;

import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.JIntellitype;
import org.sikora.ruler.model.input.Hints;
import org.sikora.ruler.model.input.Input;
import org.sikora.ruler.model.input.InputDriver;
import org.sikora.ruler.model.input.InputDriver.Event;
import org.sikora.ruler.ui.awt.AwtInputDriver;
import org.sikora.ruler.ui.awt.AwtResultWindow;

import java.util.ArrayList;
import java.util.Date;

import static org.sikora.ruler.model.input.InputDriver.Command.*;

/**
 * Input driver handler. It provides context based hints and executes recognized tasks. It is activated by
 * global hot key.
 */
public class Ruler implements InputDriver.Handler, HotkeyListener {
  final InputDriver inputDriver;
  final AwtResultWindow resultWindow;

  /**
   * Configures global key hooks and wires Ruler to it. It uses AwtInputDriver for receiving users input.
   *
   * @param args ignored
   */
  public static void main(final String[] args) {
    InputDriver driver = new AwtInputDriver();
    Ruler ruler = new Ruler(driver, new AwtResultWindow());
    driver.addHandler(ruler);

    JIntellitype.setLibraryLocation("../lib/JIntellitype64.dll");
    JIntellitype hook = JIntellitype.getInstance();
    hook.addHotKeyListener(ruler);
    hook.registerHotKey(1, JIntellitype.MOD_CONTROL, (int) ' ');
    hook.registerHotKey(2, JIntellitype.MOD_CONTROL + JIntellitype.MOD_SHIFT, (int) ' ');
  }

  private Ruler(final InputDriver inputDriver, final AwtResultWindow resultWindow) {
    this.inputDriver = inputDriver;
    this.resultWindow = resultWindow;
  }

  /**
   * Based on input driver event provides context based hints back to the driver.
   * It also executes recognized tasks.
   *
   * @param event input driver event
   */
  public void dispatch(final Event event) {
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
      case SUBMIT_INPUT:
        if ("now".equals(text.trim())) {
          event.driver().issue(HIDE_INPUT);
          event.driver().issue(RESET_INPUT);
          String now = new Date().toString();
          resultWindow.display(now);
        }
        if ("quit".equals(text.trim())) {
          JIntellitype.getInstance().cleanUp();
          System.exit(0);
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

