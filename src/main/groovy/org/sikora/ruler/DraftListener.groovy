package org.sikora.ruler

import org.sikora.ruler.Draft

/**
 * User: der3k
 * Date: 9.7.11
 * Time: 13:40
 */
interface DraftListener {
  void onChange(Draft draft)

  Draft onComplete(Draft draft, int key)

  void onExecute(Draft draft)

  void onCancel(Draft draft)

}
