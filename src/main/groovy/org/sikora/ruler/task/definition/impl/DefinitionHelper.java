package org.sikora.ruler.task.definition.impl;

import org.sikora.ruler.context.InputEventInContext;
import org.sikora.ruler.task.Task;
import org.sikora.ruler.ui.awt.AwtResultWindow;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 7.7.12
 * Time: 18:27
 * To change this template use File | Settings | File Templates.
 */
public class DefinitionHelper extends EditDistanceDefinition {
    private final String name;

    protected DefinitionHelper(final String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }

    public Task newTask(final InputEventInContext event) {
        return new Task() {
            public void execute(final AwtResultWindow resultWindow) {
                resultWindow.display(name);
            }
        };
    }
}
