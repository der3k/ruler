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
import org.sikora.ruler.task.Draft;
import org.sikora.ruler.task.DraftFactory;
import org.sikora.ruler.task.Task;
import org.sikora.ruler.task.definition.impl.BaseDefinitionRepository;
import org.sikora.ruler.task.definition.impl.DefaultDefinitionFactory;
import org.sikora.ruler.task.definition.impl.FilesystemDefinitionFactory;
import org.sikora.ruler.task.impl.DefinitionDraftFactory;
import org.sikora.ruler.ui.awt.AwtInputDriver;
import org.sikora.ruler.ui.awt.AwtInputWindow;
import org.sikora.ruler.ui.awt.AwtResultWindow;

import static org.sikora.ruler.model.input.InputDriver.Command.*;
import static org.sikora.ruler.model.input.InputDriver.Event.CANCEL_ISSUED;

/**
 * Input driver handler. It provides context based hints and executes recognized tasks. It is activated by
 * global hot key.
 */
public class Ruler implements InputDriver.Listener, HotkeyListener {
    private final DraftFactory draftFactory;
    private final ContextProvider contextProvider;
    private final InputDriver inputDriver;
    private final AwtResultWindow resultWindow;
    private Context context;

    /**
     * Configures global key hooks and wires Ruler to it. It uses AwtInputDriver for receiving users input.
     *
     * @param args ignored
     */
    public static void main(final String[] args) {
        final AwtInputDriver inputDriver = new AwtInputDriver(new AwtInputWindow());
        final BaseDefinitionRepository definitions = new BaseDefinitionRepository(DefaultDefinitionFactory.instanceOf(), FilesystemDefinitionFactory.of(args));
        Ruler ruler = new Ruler(inputDriver, new AwtResultWindow(), new WindowsContextProvider(), new DefinitionDraftFactory(definitions));
        inputDriver.addListener(ruler);
        JIntellitype.setLibraryLocation("../lib/JIntellitype64.dll");
        JIntellitype hook = JIntellitype.getInstance();
        hook.addHotKeyListener(ruler);
        hook.registerHotKey(1, JIntellitype.MOD_ALT, (int) ' ');
        hook.registerHotKey(2, JIntellitype.MOD_CONTROL + JIntellitype.MOD_SHIFT, (int) ' ');
    }

    private Ruler(final InputDriver inputDriver, final AwtResultWindow resultWindow, final ContextProvider contextProvider, final DraftFactory draftFactory) {
        this.inputDriver = inputDriver;
        this.resultWindow = resultWindow;
        this.contextProvider = contextProvider;
        this.draftFactory = draftFactory;
        this.context = contextProvider.currentContext();
    }

    /**
     * Based on input driver inputEvent provides context based hints back to the driver.
     * It also executes recognized tasks.
     *
     * @param inputEvent input driver inputEvent
     */
    public void dispatch(final InputEvent inputEvent) {
        if (inputEvent.event() == CANCEL_ISSUED) {
            resetAndHideInput();
            return;
        }
        final Draft draft = draftFactory.draftFor(new InputEventInContext(inputEvent, context));
        draft.consumeEvent(inputDriver);
        if (draft.isDefinitive()) {
            resetAndHideInput();
            final Task task = draft.toTask();
            task.execute(resultWindow);
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
                context = contextProvider.currentContext();
                inputDriver.issue(InputCommand.of(FOCUS));
                break;
            case 2:
                resultWindow.display();
                break;
            default:
                break;
        }
    }

    private void resetAndHideInput() {
        inputDriver.issue(InputCommand.of(HIDE));
        inputDriver.issue(InputCommand.of(RESET));
    }

}

