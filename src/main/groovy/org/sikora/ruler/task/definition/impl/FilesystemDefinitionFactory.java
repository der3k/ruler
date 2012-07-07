package org.sikora.ruler.task.definition.impl;

import org.sikora.ruler.task.definition.Definition;
import org.sikora.ruler.task.definition.ReloadableDefinitionFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 7.7.12
 * Time: 17:44
 * To change this template use File | Settings | File Templates.
 */
public class FilesystemDefinitionFactory implements ReloadableDefinitionFactory {
    private final List<Definition> definitions = new ArrayList<Definition>();
    private final File[] folders;

    public static FilesystemDefinitionFactory of(final String... folders) {
        final File[] fileFolders = new File[folders.length];
        int idx = 0;
        for (String folder : folders) {
            final File file = new File(folder);
            if (!file.isDirectory())
                throw new IllegalArgumentException("task folder '" + folder + "' is not directory");
            fileFolders[idx++] = file;
        }
        return new FilesystemDefinitionFactory(fileFolders);
    }


    private FilesystemDefinitionFactory(final File... folders) {
        this.folders = folders;
        reload();
    }

    public void reload() {
        definitions.clear();
        for (File folder : folders) {
            final File[] files = folder.listFiles();
            if (files == null)
                throw new IllegalStateException("folder '" + folder + "' does not exist");
            for (File file : files)
                definitions.add(new ExecuteFileDefinition(file));
        }
    }

    public Iterable<Definition> definitions() {
        return Collections.unmodifiableCollection(definitions);
    }
}
