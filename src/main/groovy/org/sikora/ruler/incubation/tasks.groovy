package org.sikora.ruler.incubation

import org.sikora.ruler.model.input.Hints

import org.sikora.ruler.model.input.InputDriver
import org.sikora.ruler.model.input.InputDriver.InputEvent
import static org.sikora.ruler.model.input.InputDriver.Command.*
import org.sikora.ruler.ui.awt.AwtResultWindow

class Draft {
  boolean isComplete() { }
  Task toTask() { }
}

interface Task {
  Result performAction()
}

interface Result {
  void showOn(AwtResultWindow resultWindow)
}


interface HintsProvider {
  Hints hints()
}

interface DraftFactory {
  Draft draftFor(InputEvent event)
}

def handler = new InputDriver.Listener() {
  DraftFactory draftFactory
  AwtResultWindow resultWindow

  void dispatch(InputEvent event) {
    def draft = draftFactory.draftFor(event)
    switch (event.event()) {
      case SUBMIT_INPUT:
        if (draft.isComplete()) {
          def task = draft.toTask()
          def result = task.performAction()
          result.showOn(resultWindow)
        }
        break
      case CANCEL:
        break
    }


  }

}