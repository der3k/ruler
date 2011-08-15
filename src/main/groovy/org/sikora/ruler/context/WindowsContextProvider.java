package org.sikora.ruler.context;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.win32.StdCallLibrary;

import static com.sun.jna.platform.win32.WinDef.*;
import static com.sun.jna.platform.win32.WinUser.*;

/**
 * Microsoft Windows context provider utilizing JNA library.
 */
public class WindowsContextProvider implements ContextProvider {

  public Context currentContext() {
    return new Context() {
      final WINDOWINFO info = new WINDOWINFO();
      final HWND active = User32Window.INSTANCE.GetForegroundWindow();
      final boolean status = User32.INSTANCE.GetWindowInfo(active, info);

      public Window foregroundWindow() {
        return new Window() {

          public void minimize() {
            User32Window.INSTANCE.ShowWindow(active, User32Window.SW_MINIMIZE);
          }
        };
      }
    };
  }

  /**
   * JNA interface for calling native Windows methods.
   */
  private interface User32Window extends StdCallLibrary {
    // TODO migrate newer than 3.3.0 jna version which has more window functions by default
    public int SW_MINIMIZE = 6;

    User32Window INSTANCE = (User32Window) Native.loadLibrary("user32", User32Window.class);

    HWND GetForegroundWindow();

    boolean ShowWindow(HWND hWnd, int nCmdShow);

    HWND GetDesktopWindow();

    boolean SetWindowPos(HWND hWnd, HWND hWndInsertAfter, int x, int y, int cx, int cy, int uFlags);
  }
}
