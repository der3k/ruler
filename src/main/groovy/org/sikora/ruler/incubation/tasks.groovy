package org.sikora.ruler.incubation

import org.sikora.ruler.model.input.Hints
import org.sikora.ruler.model.input.Input
import org.sikora.ruler.model.input.InputDriver
import org.sikora.ruler.model.input.InputDriver.Event
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

class Definition {

}

interface DefinitionRepository extends HintsProvider {
  Definition definitionFor(Input input)
}

interface DraftFactory {
  Draft draftFor(Event event)
}

def handler = new InputDriver.Handler() {
  DraftFactory draftFactory
  AwtResultWindow resultWindow

  void dispatch(Event event) {
    def draft = draftFactory.draftFor(event)
    switch (event.command()) {
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