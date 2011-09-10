package org.sikora.ruler.model.input;

import java.util.ArrayList;
import java.util.List;

/**
 * Data object containing hint items. It allows to select hint by index.
 */
public class Hints {
  /**
   * Value representing no hints.
   */
  public static final Hints NONE = new Hints();

  private Item selected = Item.NONE;
  private final List<Item> items = new ArrayList<Item>();

  private Hints() {
  }

  //TODO implement static factory that would return Hints.NONE when list is empty

  /**
   * Creates hints from list of items. There will be Item.NONE selected.
   *
   * @param items list of hint items
   * @throws IllegalArgumentException when items are null or contain no item
   */
  public Hints(List<Item> items) {
    super();
    if (items == null)
      throw new IllegalArgumentException("items cannot be null");
    if (items.size() == 0)
      throw new IllegalArgumentException("no hint items specified");
    this.items.addAll(items);
  }

  /**
   * Selects current item. If the index is out of bound it silently selects Item.NONE.
   *
   * @param index item index
   */
  public void select(int index) {
    if (index < 0 || index >= items.size()) {
      selected = Item.NONE;
      return;
    }
    selected = items.get(index);
  }


  /**
   * Returns currently selected item.
   *
   * @return selected item
   */
  public Item selected() {
    return selected;
  }

  /**
   * Returns hint items. Defensive array copy is created on each call.
   *
   * @return
   */
  public Item[] items() {
    return items.toArray(new Item[items.size()]);
  }

  /**
   * Returns hints count.
   *
   * @return hints count
   */
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

  /**
   * Value object containing item data.
   */
  public static class Item {
    /**
     * Null item instance.
     */
    public static final Item NONE = new Item("?") {
      @Override
      public boolean equals(final Object o) {
        return this == o;
      }
    };

    private final String text;

    /**
     * Creates new Item from text.
     *
     * @param text item text
     * @throws IllegalArgumentException when text is null or empty
     */
    public Item(final String text) {
      if (text == null)
        throw new IllegalArgumentException("Hint Item text cannot be null");
      if (text.trim().isEmpty())
        throw new IllegalArgumentException("Hint Item text cannot be empty");
      this.text = text;
    }

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
