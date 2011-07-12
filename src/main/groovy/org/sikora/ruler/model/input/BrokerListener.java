package org.sikora.ruler.model.input;

import org.sikora.ruler.model.input.Broker.*;

/**
 * User: der3k
 * Date: 11.7.11
 * Time: 18:31
 */
public interface BrokerListener {
  void onChange(UpdateEvent updateEvent);

  void onComplete(CompleteEvent completeEvent);

  void onCancel(CancelEvent cancelEvent);

  void onSubmit(SubmitEvent submitEvent);
}
