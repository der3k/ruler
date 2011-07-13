package org.sikora.ruler.model.input;

/**
 * User: der3k
 * Date: 11.7.11
 * Time: 18:28
 */
public interface Broker {
  public void setInput(Input input);

  public void setHints(Hints hints);

  public void addListener(BrokerListener listener);

  public void removeListener(BrokerListener listener);

  public class Event {
    private final Context context;

    public Event(final Context context) {
      this.context = context;
    }

    public Context context() {
      return context;
    }
  }

  public class UpdateEvent extends Event {
    private final Input.Update update;

    public UpdateEvent(final Context context, final Input.Update update) {
      super(context);
      this.update = update;
    }

    public Input.Update update() {
      return update;
    }
  }

  public class CompleteEvent extends Event {
    private final Hints.Item hint;

    public CompleteEvent(final Context context, final Hints.Item hint) {
      super(context);
      this.hint = hint;
    }

    public Hints.Item hint() {
      return hint;
    }
  }

  public class SubmitEvent extends Event {

    public SubmitEvent(final Context context) {
      super(context);
    }
  }

  public class CancelEvent extends Event {

    public CancelEvent(final Context context) {
      super(context);
    }
  }
}
