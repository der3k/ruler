package org.sikora.ruler

/**
 * User: der3k
 * Date: 9.7.11
 * Time: 14:14
  */
public interface InputProvider {
  String text()
  int position()
  void setTextAndPosition(String text, int position)
}