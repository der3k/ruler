package org.sikora.ruler.ui.awt;

import org.sikora.ruler.model.input.InputDriver;
import org.sikora.ruler.model.input.InputDriver.InputCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static java.awt.event.KeyEvent.*;
import static org.sikora.ruler.model.input.InputDriver.Command.*;

/**
 * Key listener that propagates recognized keys to input driver.
 */
public class AwtInputKeyListener implements KeyListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(AwtInputKeyListener.class);
    private static final char COMPLETE_KEY_MIN = '1';
    private static final char COMPLETE_KEY_MAX = '9';

    private final InputDriver driver;

    public AwtInputKeyListener(final InputDriver driver) {
        this.driver = driver;
    }

    /**
     * Consumes key event when complete key was typed (keys 1-9).
     *
     * @param event key event
     */
    public void keyTyped(final KeyEvent event) {
        if (isCompleteKey(event.getKeyChar()))
            event.consume();
    }

    /**
     * Dispatches driver events for functional keys (ESC, ENTER, TAB, 1-9).
     *
     * @param keyEvent key event
     */
    public void keyPressed(final KeyEvent keyEvent) {
        final int key = keyEvent.getKeyCode();
        LOGGER.trace("Key pressed: '{}'", eventKeys(keyEvent));
        if (isNotRecognizedEvent(keyEvent))
            return;
        switch (key) {
            case VK_ESCAPE:
                issueKeyCommand(keyEvent, InputCommand.of(CANCEL));
                break;
            case VK_TAB:
            case VK_1:
            case VK_2:
            case VK_3:
            case VK_4:
            case VK_5:
            case VK_6:
            case VK_7:
            case VK_8:
            case VK_9:
                issueKeyCommand(keyEvent, InputCommand.of(COMPLETE, hintIndexFromKey(key)));
                break;
            case VK_ENTER:
                issueKeyCommand(keyEvent, InputCommand.of(SUBMIT, hintIndexFromKey(VK_1)));
                break;
        }
    }

    /**
     * Recognizes and propagates input updates.
     *
     * @param event key event
     */
    public void keyReleased(KeyEvent event) {
        driver.issue(InputCommand.of(UPDATE_FROM_FIELD));
    }

    private void issueKeyCommand(final KeyEvent keyEvent, final InputCommand command) {
        keyEvent.consume();
        driver.issue(command);
    }

    private boolean isCompleteKey(final char key) {
        return COMPLETE_KEY_MIN <= key && key <= COMPLETE_KEY_MAX;
    }

    private int hintIndexFromKey(final int key) {
        if (VK_TAB == key)
            return 0;
        if (VK_1 <= key && key <= VK_9)
            return key - VK_1;
        throw new IllegalArgumentException("unsupported complete key: " + getKeyText(key));
    }

    private boolean isNotRecognizedEvent(KeyEvent event) {
        return event.isShiftDown()
                || event.isAltDown()
                || event.isAltGraphDown()
                || event.isControlDown()
                || event.isMetaDown()
                || event.isConsumed();
    }

    private String eventKeys(final KeyEvent event) {
        final StringBuilder builder = new StringBuilder();
        if (event.getModifiersEx() != 0)
            builder.append(getModifiersExText(event.getModifiersEx())).append("+ ");
        builder.append(getKeyText(event.getKeyCode()));
        return builder.toString();
    }

}
