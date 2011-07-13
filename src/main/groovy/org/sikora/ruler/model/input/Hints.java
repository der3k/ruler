package org.sikora.ruler.model.input;

import java.util.ArrayList;
import java.util.List;

/**
 * User: sikorric
 * Date: 12.7.11
 * Time: 16:48
 */

public class Hints {
  public static final Hints NONE = new Hints();

  private Item selected = Item.NONE;
  private final List<Item> items = new ArrayList<Item>();

  public Hints() {

  }

  public Hints(List<Item> items) {
    super();
    this.items.addAll(items);
  }

  public void select(int index) {
    if (index < 0 || index >= items.size())
      return;
    selected = items.get(index);
  }

  public Item selected() {
    return selected;
  }

  public int size() {
    return items.size();
  }

  public static class Item {
    private final String text;

    public Item(final String text) {
      if (text == null)
        throw new IllegalArgumentException("Hint Item text cannot be null");
      if (text.trim().isEmpty())
        throw new IllegalArgumentException("Hint Item text cannot be empty");
      this.text = text;
    }

    public static final Item NONE = new Item("?");

    @Override
    public String toString() {
      return text;
    }
  }
}
