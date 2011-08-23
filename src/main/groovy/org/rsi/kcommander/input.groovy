package org.rsi.kcommander

import com.melloware.jintellitype.JIntellitype
import org.sikora.ruler.ui.awt.AwtInputDriver
import org.sikora.ruler.Ruler
import org.sikora.ruler.ui.awt.AwtInputWindow
import org.sikora.ruler.ui.awt.AwtResultWindow

def input = new AwtInputWindow()
def driver = new AwtInputDriver(input)
input.addKeyListener(driver)

def ruler = new Ruler(input, new AwtResultWindow())
driver.addListener(ruler)

JIntellitype.setLibraryLocation('../lib/JIntellitype64.dll')
def hook = JIntellitype.getInstance()
hook.addHotKeyListener(ruler)
hook.registerHotKey(1, JIntellitype.MOD_CONTROL, (int) ' ')
hook.registerHotKey(2, JIntellitype.MOD_CONTROL + JIntellitype.MOD_SHIFT, (int) ' ')
