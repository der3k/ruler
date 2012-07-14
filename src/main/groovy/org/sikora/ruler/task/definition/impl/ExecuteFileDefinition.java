package org.sikora.ruler.task.definition.impl;

import org.sikora.ruler.context.InputEventInContext;
import org.sikora.ruler.task.Task;
import org.sikora.ruler.ui.awt.AwtResultWindow;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ExecuteFileDefinition extends EditDistanceDefinition {
  private final File file;
  private final String name;

  public ExecuteFileDefinition(final File file) {
    this.file = file;
    this.name = baseName(file);
  }

  public String name() {
    return name;
  }

  public Task newTask(final InputEventInContext event) {
    return new Task() {
      public void execute(final AwtResultWindow resultWindow) {
        try {
          final ArrayList<String> cmd = new ArrayList<String>();
          cmd.add("rundll32.exe");
          cmd.add("shell32.dll,ShellExec_RunDLL");
          cmd.add(file.getCanonicalPath());
          final ProcessBuilder processBuilder = new ProcessBuilder(cmd);
          processBuilder.start();
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    };
  }

  private String baseName(final File file) { 
    String name = file.getName();
    final int dotIndex = name.lastIndexOf('.');
    return dotIndex > 0 ? name.substring(0, dotIndex) : name;
  }
}
