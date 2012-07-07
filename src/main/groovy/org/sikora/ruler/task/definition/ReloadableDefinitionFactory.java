package org.sikora.ruler.task.definition;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 7.7.12
 * Time: 17:40
 * To change this template use File | Settings | File Templates.
 */
public interface ReloadableDefinitionFactory extends DefinitionFactory {
    void reload();
}
