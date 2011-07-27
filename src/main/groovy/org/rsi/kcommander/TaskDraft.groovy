package org.rsi.kcommander

/**
 * User: sikorric
 * Date: 11.7.11
 * Time: 14:07
 */

class TaskDraft {
  List<Entry> entries = []
  Entry current

  TaskDraft(String input, int position) {
    switch (position) {
      case 0:
        input = "#$input"
        break
      case input.length():
        input = "$input#"
        break
      default:
        input = input[0..(position - 1)] + "#" + input[position..-1]
    }
    def currentIndex = 0
    input.split(' +').eachWithIndex { entry, i ->
      def index = entry.indexOf('#')
      def positionInEntry = index >= 0 ? new Position(index) : Position.NONE
      if (Position.NONE != positionInEntry) {
        entry = entry.replace('#', '')
        currentIndex = i
      }
      entries << new Entry(value: entry, position: positionInEntry)
    }
    current = entries[currentIndex]
  }

  TaskDraft complete(String hint) {
    entries.each { entry ->
      def suffix = ""
    }
  }

  String text() {
    entries*.value.join(' ')
  }

  Position position() {
    
  }

  @Override
  String toString() {
    "{${entries.join(', ')}}"
  }

  static class Entry {
    String value
    Position position

    String suffix() {
      position != Position.NONE ? value[position.value..-1] : ''
    }
    @Override
    String toString() {
      Position.NONE == position ? "'$value'" : "'$value[$position]'"
    }


  }

  static class Position {
    final static Position NONE = new Position(-1) {
      @Override
      String toString() {
        "NONE"
      }

    }
    int value

    Position(int value) {
      this.value = value
    }

    @Override
    String toString() {
      value
    }


  }

}

