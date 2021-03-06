package org.sikora.ruler.model.input

import spock.lang.Specification

/**
 * User: der3k
 * Date: 11.7.11
 * Time: 17:51
 */
public class InputTest extends Specification {

  def 'has text'() {
  expect:
    Input.of('a').text() == 'a'
  }

  def 'marker marks position in the text'() {
    def input = Input.of('abc', 1)
  expect:
    input.text() == 'abc'
    input.marker() == 1
  }

  def 'marker cannot be less than zero'() {
  when:
    Input.of("", -1)
  then:
    thrown IllegalArgumentException
  }

  def 'marker cannot be outside the text'() {
  when:
    Input.of("", 1)
  then:
    thrown IllegalArgumentException
  }

  def 'toSting formats input'() {
  expect:
    input.toString() == expected
  where:
    input              | expected
    Input.EMPTY        | '|'
    Input.of('')       | '|'
    Input.of('a')      | 'a|'
    Input.of('a', 0)   | '|a'
    Input.of('ab', 1)  | 'a|b'
    Input.of('abc', 2) | 'ab|c'
  }

  def 'equals() is defined based on text and position'() {
  expect:
    Input.of('').equals(Input.EMPTY)
    !Input.of('a', 0).equals(Input.of('a', 1))
  }

  def 'can produce input update'() {
    def update = Input.of('a').updateTo(Input.of('ab'))
  expect:
    update.oldValue() == Input.of('a')
    update.newValue() == Input.of('ab')
    update.toString() == "'a|' => 'ab|'"
  }

  def 'new input value of update cannot be null'() {
  when:
    Input.of('a').updateTo(null)
  then:
    thrown IllegalArgumentException
  }

  def 'update detects void update'() {
    def update = Input.of('a').updateTo(Input.of('a'))
  expect:
    update.isVoid()
  }

  def 'update detects change'() {
    def update = Input.of('a').updateTo(Input.of('ab'))
  expect:
    !update.isVoid()
  }

  def 'returns active text'() {
  expect:
    input.activeText() == expected
  where:
    input       | expected
    Input.EMPTY | ''
    Input.of(' ') | ''
    Input.of(' ', 0) | ''
    Input.of('a', 0) | ''
    Input.of('a') | 'a'
    Input.of('a ') | 'a'
    Input.of(' a') | 'a'
    Input.of(' a ') | 'a'
    Input.of(' a b ') | 'a b'
  }


}
