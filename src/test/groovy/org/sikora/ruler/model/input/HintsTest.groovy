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
    def hints = new Hints([new Item('item1'), item])
    hints.select(1)
    expect:
    hints.selected() == item
  }

  def 'selecting out of range selects nothing'() {
    def item = new Item('item1')
    def hints = new Hints([item])
    hints.select(0)
    hints.select(1) // out of range!
    expect:
    hints.selected() == Item.NONE
  }

  def 'cannot create empty hints'() {
    expect:
    try {
      new Hints(items)
      throw new Exception('test should fail but did not')
    } catch (RuntimeException expected) {
      // expected
    }
    where:
    items << [null, []]
  }

  def 'new hints have nothing selected'() {
    expect:
    Hints.NONE.selected() == Hints.Item.NONE
    new Hints([new Item('item')]).selected() == Hints.Item.NONE
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
    new Hints([new Item('item')])                     | 1
    new Hints([new Item('item1'), new Item('item2')]) | 2
  }

  def 'returns items'() {
    def item = new Item('item')
    expect:
    new Hints([item]).items()[0] == item
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