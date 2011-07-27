package org.rsi.kcommander

import com.melloware.jintellitype.JIntellitype
import org.sikora.ruler.ui.AwtInputDriver
import org.sikora.ruler.ui.AwtInputWindow
import org.sikora.ruler.Ruler
import org.sikora.ruler.ui.AwtResultWindow

def input = new AwtInputWindow()
def driver = new AwtInputDriver(input)
input.addKeyListener(driver)

def ruler = new Ruler(input, new AwtResultWindow())

def driverListener = new InputDriverListener(ruler)
driver.addListener(driverListener)
def hotkeyListener = new RulerHotkeyListener(ruler)

JIntellitype.setLibraryLocation('../lib/JIntellitype64.dll')
def hook = JIntellitype.getInstance()
hook.addHotKeyListener(hotkeyListener)
hook.registerHotKey(1, JIntellitype.MOD_CONTROL, (int) ' ')
hook.registerHotKey(2, JIntellitype.MOD_CONTROL + JIntellitype.MOD_SHIFT, (int) ' ')
