package org.sikora.ruler.task.impl;

import org.sikora.ruler.context.InputEventInContext;
import org.sikora.ruler.model.input.InputDriver;
import org.sikora.ruler.task.Result;
import org.sikora.ruler.task.Task;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.sikora.ruler.model.input.InputDriver.Command.*;

public class ExecuteFileDefinition extends SimpleDefinition {
  private final File file;

  public ExecuteFileDefinition(final File file) {
    this.file = file;
  }

  public String name() {
    return file.getName();
  }

  public Task createTask(final InputEventInContext event) {
    return new Task() {
      public Result performAction() {
        return new Result() {
          public void display() {
            event.inputDriver().issue(InputDriver.InputCommand.of(HIDE));
            event.inputDriver().issue(InputDriver.InputCommand.of(RESET));
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
    };
  }
}
