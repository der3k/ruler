package org.sikora.ruler.ui

import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import org.sikora.ruler.Draft
import org.sikora.ruler.DraftListener
import org.sikora.ruler.InputProviderOld
import org.slf4j.LoggerFactory
import static java.awt.event.KeyEvent.*

/**
 * User: der3k
 * Date: 9.7.11
 * Time: 13:05
 */
class DraftKeyListener extends KeyAdapter {
  static final logger = LoggerFactory.getLogger(DraftKeyListener)
  static final List<Character> ACTION_KEY_CHARS = [
      '1',
      '2',
      '3',
      '4',
      '5',
      '6',
      '7',
      '8',
      '9'
  ].collect { it as char }
  final DraftListener draftListener
  final InputProviderOld input

  Draft draft = Draft.EMPTY

  DraftKeyListener(final InputProviderOld input, final DraftListener draftListener) {
    this.input = input
    this.draftListener = draftListener
  }

  @Override
  void keyPressed(KeyEvent event) {
    int key = event.keyCode
    logger.trace('keyPressed: {}, \'{}\'', key, getKeyText(key))
    switch (key) {
      case VK_ESCAPE:
        logger.debug('Canceling {} with {} key', draft, getKeyText(key))
        event.consume()
        draftListener.onCancel(draft)
        break
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
        logger.debug('Completing {} with \'{}\' key', draft, getKeyText(key))
        event.consume()
        def newDraft = draftListener.onComplete(draft, key)
        if (!draft.equals(newDraft)) {
          input.setTextAndPosition(newDraft.input, newDraft.position.position)
          propagateDraftChange(newDraft)
        }
        break
      case VK_ENTER:
        logger.debug('Executing {} with {} key', draft, getKeyText(key))
        event.consume()
        draftListener.onExecute(draft)
        break
    }
  }

  @Override
  void keyReleased(KeyEvent event) {
    def newDraft = Draft.parse(input.text(), input.position())
    if (!draft.equals(newDraft))
      propagateDraftChange(newDraft)
  }

  @Override
  void keyTyped(KeyEvent event) {
    if (ACTION_KEY_CHARS.contains(event.keyChar)) {
      event.consume()
    }
  }

  def propagateDraftChange(Draft newDraft) {
    logger.trace('Draft changed from: {} to: {}', draft, newDraft)
    draft = newDraft
    logger.debug('Calling DraftListener#onChange({})', draft)
    draftListener.onChange(draft)
  }

  void reset() {
    draft = Draft.EMPTY
  }

}
