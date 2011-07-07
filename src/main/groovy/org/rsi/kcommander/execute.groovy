package org.rsi.kcommander

import com.sun.jna.Native
import com.sun.jna.Pointer
import com.sun.jna.WString
import com.sun.jna.platform.win32.Kernel32
import com.sun.jna.platform.win32.WinBase
import com.sun.jna.platform.win32.WinBase.PROCESS_INFORMATION
import com.sun.jna.platform.win32.WinBase.STARTUPINFO
import com.sun.jna.platform.win32.WinDef.WORD
import com.sun.jna.win32.StdCallLibrary

interface Kernel32Process extends StdCallLibrary {
  Kernel32Process INSTANCE = (Kernel32Process) Native.loadLibrary('kernel32', Kernel32Process.class)

  boolean CreateProcessW(WString lpApplicationName,
                         WString lpCommandLine,
                         Pointer lpProcessAttributes,
                         Pointer lpThreadAttributes,
                         boolean bInheritHandles,
                         long dwCreationFlags,
                         Pointer lpEnvironment,
                         WString lpCurrentDirectory,
                         STARTUPINFO lpStartupInfo,
                         PROCESS_INFORMATION lpProcessInformation
  )
}

def startup = new STARTUPINFO()
startup.dwFlags = WinBase.STARTF_USESHOWWINDOW
startup.wShowWindow = new WORD(5L)
def process = new PROCESS_INFORMATION()
//def cmd = 'c:\\windows\\system32\\cmd.exe /c start C:\\home\\etc\\links\\groovyconsole.lnk'
def cmd = 'c:\\windows\\explorer.exe'
Kernel32Process lib = Kernel32Process.INSTANCE
def result = lib.CreateProcessW(null,
    new WString(cmd),
    null,
    null,
    false,
    WinBase.DETACHED_PROCESS,
    null,
    new WString('c:\\windows'),
    startup,
    process)

println result
println Kernel32.INSTANCE.GetLastError()
println process
println 'done'

