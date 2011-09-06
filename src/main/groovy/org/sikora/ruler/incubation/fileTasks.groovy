package org.sikora.ruler.incubation

import org.sikora.ruler.task.definition.impl.ExecuteFileDefinition

def folders = []
folders << new File('H:/share/etc/links')
folders << new File('H:/etc/links')

def definitions = []

folders.each { folder ->
  folder.eachFile { file ->
    def taskDef = new ExecuteFileDefinition(file)
    definitions << taskDef
  }
}
definitions
