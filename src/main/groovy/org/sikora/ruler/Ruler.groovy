package org.sikora.ruler

import org.sikora.ruler.model.input.InputField
import org.sikora.ruler.ui.AwtResultWindow

/**
 * User: sikorric
 * Date: 8.7.11
 * Time: 15:06
 */
 
class Ruler {
  final InputField inputField
  final AwtResultWindow resultWindow

  Ruler(final InputField inputField, final AwtResultWindow resultWindow) {
    this.inputField = inputField
    this.resultWindow = resultWindow
  }

  void focus() {
    inputField.focus()
  }

  void show(Result result) {
    resultWindow.display(result.text)
  }

  void show() {
    resultWindow.display()
  }

  void hideResult() {
    resultWindow.hide()
  }
}

