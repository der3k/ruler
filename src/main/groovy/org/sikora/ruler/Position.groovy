package org.sikora.ruler

import groovy.transform.Immutable

/**
 * User: der3k
 * Date: 9.7.11
 * Time: 14:37
  */

class Position {
  static final ZERO = new Position(0)
  final int position

  Position(final int position) {
    this.position = position
  }

  @Override
  String toString() {
    "$position"
  }


  boolean equals(final o) {
    if (this.is(o)) return true;
    if (!(o instanceof Position)) return false;

    final Position position1 = (Position) o;

    if (position != position1.position) return false;

    return true;
  }

  int hashCode() {
    return position;
  }
}
