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

  private Hints() {
  }

  public Hints(List<Item> items) {
    super();
    if (items == null)
      throw new IllegalArgumentException("items cannot be null");
    if (items.size() == 0)
      throw new IllegalArgumentException("no hint items specified");
    this.items.addAll(items);
  }

  public void select(int index) {
    if (index < 0 || index >= items.size()) {
      selected = Item.NONE;
      return;
    }
    selected = items.get(index);
  }

  public Item selected() {
    return selected;
  }

  public Item[] items() {
    return items.toArray(new Item[items.size()]);
  }

  public int size() {
    return items.size();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Hints hints = (Hints) o;

    if (items != null ? !items.equals(hints.items) : hints.items != null) return false;
    if (selected != null ? !selected.equals(hints.selected) : hints.selected != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = selected != null ? selected.hashCode() : 0;
    result = 31 * result + (items != null ? items.hashCode() : 0);
    return result;
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

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      Item item = (Item) o;

      if (text != null ? !text.equals(item.text) : item.text != null) return false;

      return true;
    }

    @Override
    public int hashCode() {
      return text != null ? text.hashCode() : 0;
    }
  }


}
