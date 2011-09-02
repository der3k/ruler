package org.sikora.ruler.task.impl;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.WString;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.win32.StdCallLibrary;
import org.sikora.ruler.context.InputEventInContext;
import org.sikora.ruler.model.input.InputDriver;
import org.sikora.ruler.task.Result;
import org.sikora.ruler.task.Task;

import java.io.File;

import static org.sikora.ruler.model.input.InputDriver.Command.*;

/**
 * User: der3k
 * Date: 2.9.11
 * Time: 23:45
 */
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
            event.resultWindow().display(name());

//            WinBase.STARTUPINFO startup = new WinBase.STARTUPINFO();
//            startup.dwFlags = WinBase.STARTF_USESHOWWINDOW;
//            startup.wShowWindow = new WinDef.WORD(5L);
//            WinBase.PROCESS_INFORMATION process = new WinBase.PROCESS_INFORMATION();
//            //def cmd = 'c:\\windows\\system32\\cmd.exe /c start C:\\home\\etc\\links\\groovyconsole.lnk'
//            String cmd = "c:\\windows\\explorer.exe";
//            Kernel32Process lib = Kernel32Process.INSTANCE;
//            boolean result = lib.CreateProcessW(null,
//                new WString(cmd),
//                null,
//                null,
//                false,
//                WinBase.DETACHED_PROCESS,
//                null,
//                new WString("c:\\windows"),
//                startup,
//                process);
//
//            Kernel32.INSTANCE.GetLastError();
          }
        };
      }
    };
  }

  interface Kernel32Process extends StdCallLibrary {
    Kernel32Process INSTANCE = (Kernel32Process) Native.loadLibrary("kernel32", Kernel32Process.class);

    boolean CreateProcessW(WString lpApplicationName,
                           WString lpCommandLine,
                           Pointer lpProcessAttributes,
                           Pointer lpThreadAttributes,
                           boolean bInheritHandles,
                           long dwCreationFlags,
                           Pointer lpEnvironment,
                           WString lpCurrentDirectory,
                           WinBase.STARTUPINFO lpStartupInfo,
                           WinBase.PROCESS_INFORMATION lpProcessInformation
    );
  }

}
