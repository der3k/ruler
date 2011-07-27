package org.rsi.kcommander

import org.sikora.ruler.Ruler
import com.melloware.jintellitype.HotkeyListener

/**
 * User: der3k
 * Date: 27.7.11
 * Time: 7:47
 */
class RulerHotkeyListener implements HotkeyListener {
  final Ruler ruler

  RulerHotkeyListener(final Ruler ruler) {
    this.ruler = ruler
  }

  void onHotKey(int hookId) {
    switch (hookId) {
      case 1:
        ruler.focus()
        break;
      case 2:
        ruler.show()
        break;
      default:
        break;
    }
  }
}
