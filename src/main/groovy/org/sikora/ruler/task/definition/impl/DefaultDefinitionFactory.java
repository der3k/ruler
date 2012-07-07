package org.sikora.ruler.task.definition.impl;

import com.melloware.jintellitype.JIntellitype;
import org.sikora.ruler.context.InputEventInContext;
import org.sikora.ruler.task.Task;
import org.sikora.ruler.task.definition.Definition;
import org.sikora.ruler.task.definition.DefinitionFactory;
import org.sikora.ruler.ui.awt.AwtResultWindow;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 7.7.12
 * Time: 18:14
 * To change this template use File | Settings | File Templates.
 */
public class DefaultDefinitionFactory implements DefinitionFactory {
    private final Set<Definition> definitions = new HashSet<Definition>();
    private static DefaultDefinitionFactory instance;

    public static DefinitionFactory instanceOf() {
        if (instance == null)
            instance = new DefaultDefinitionFactory();
        return instance;
    }

    private DefaultDefinitionFactory() {
        definitions.add(new DefinitionHelper("now") {
            @Override
            public Task newTask(final InputEventInContext event) {
                return new Task() {
                    public void execute(final AwtResultWindow resultWindow) {
                        resultWindow.display(new Date().toString());
                    }
                };
            }
        });
        definitions.add(new DefinitionHelper("quit") {
            @Override
            public Task newTask(final InputEventInContext event) {
                JIntellitype.getInstance().cleanUp();
                System.exit(0);
                return null;
            }
        });
        definitions.add(new DefinitionHelper("quote") {
        });
        definitions.add(new DefinitionHelper("notify") {
        });
        definitions.add(new DefinitionHelper("minimize") {
            @Override
            public Task newTask(final InputEventInContext event) {
                return new Task() {
                    public void execute(final AwtResultWindow resultWindow) {
                        event.foregroundWindow().minimize();
                    }
                };
            }
        });
    }

    public Iterable<Definition> definitions() {
        return Collections.unmodifiableCollection(definitions);
    }

}
