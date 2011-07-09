package org.sikora.ruler

/**
 * User: sikorric
 * Date: 8.7.11
 * Time: 14:57
 */

class Draft {
  static final EMPTY = new Draft('', Position.ZERO)

  final String input
  final Position position

  Entry[] entries() {}

  Entry currentEntry() {}

  static Draft parse(String input, int position) {
    new Draft(input, new Position(position))
  }

  Draft(final String input, final Position position) {
    this.input = input
    this.position = position
  }

  Draft complete(Hint hint) {}

  @Override
  String toString() {
    "Draft[$input, $position]"
  }


  boolean equals(final o) {
    if (this.is(o)) return true;
    if (!(o instanceof Draft)) return false;

    final Draft draft = (Draft) o;

    if (input != draft.input) return false;
    if (position != draft.position) return false;

    return true;
  }

  int hashCode() {
    int result;
    result = (input != null ? input.hashCode() : 0);
    result = 31 * result + (position != null ? position.hashCode() : 0);
    return result;
  }
}
