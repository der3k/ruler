package org.sikora.ruler.model.input;


import org.sikora.ruler.model.input.Hints.Item
import spock.lang.Specification

/**
 * User: sikorric
 * Date: 1.8.11
 * Time: 15:55
 */

public class HintsTest extends Specification {

  def 'can select item by zero based index'() {
    def item = new Item('item2')
    def hints = Hints.of([new Item('item1'), item])
    hints.select(1)
  expect:
    hints.selected() == item
  }

  def 'selecting out of range selects nothing'() {
    def item = new Item('item1')
    def hints = Hints.of([item])
    hints.select(0)
    hints.select(1) // out of range!
  expect:
    hints.selected() == Item.NONE
  }

  def 'cannot create hints from null'() {
  when:
    Hints.of(null)
  then:
    thrown IllegalArgumentException
  }

  def 'hints from no items return Hints.NONE constant'() {
    expect: Hints.of([]) is Hints.NONE
  }


  def 'new hints have nothing selected'() {
  expect:
    Hints.NONE.selected() == Hints.Item.NONE
    Hints.of([new Item('item')]).selected() == Hints.Item.NONE
  }

  def 'no hints have no items'() {
  expect:
    Hints.NONE.items().length == 0
  }

  def 'returns number of items'() {
  expect:
    hints.size() == size
  where:
    hints                                             | size
    Hints.NONE                                        | 0
    Hints.of([new Item('item')])                     | 1
    Hints.of([new Item('item1'), new Item('item2')]) | 2
  }

  def 'returns items'() {
    def item = new Item('item')
  expect:
    Hints.of([item]).items()[0] == item
  }

  def 'cannot create item with empty or null text'() {
  expect:
    try {
      new Hints.Item(text)
      throw new Exception('test should fail but did not')
    } catch (RuntimeException expected) {
      // expected
    }
  where:
    text << [null, '', ' ', "\t", "\n"]
  }
}