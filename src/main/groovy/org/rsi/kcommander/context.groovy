package org.rsi.kcommander

import com.sun.jna.Native
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef.HWND
import com.sun.jna.platform.win32.WinUser.WINDOWINFO
import com.sun.jna.win32.StdCallLibrary

interface User32Window extends StdCallLibrary {
  User32Window INSTANCE = (User32Window) Native.loadLibrary('user32', User32Window.class)

  HWND GetForegroundWindow()

  HWND GetDesktopWindow()

  boolean SetWindowPos(HWND hWnd, HWND hWndInsertAfter, int X, int Y, int cx, int cy, int uFlags)
}

def lib = User32Window.INSTANCE

def active = lib.GetForegroundWindow()
def info = new WINDOWINFO()
User32.INSTANCE.GetWindowInfo(active, info)
println info
//lib.SetWindowPos(active, null, 100, 100, 400, 400, 0x40)

def desktop = lib.GetDesktopWindow()
def desktopInfo = new WINDOWINFO()
User32.INSTANCE.GetWindowInfo(desktop, desktopInfo)
println desktopInfo

println 'done.'