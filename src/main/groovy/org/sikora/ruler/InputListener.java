package org.sikora.ruler;

import org.sikora.ruler.InputDevice.*;

/**
 * User: der3k
 * Date: 11.7.11
 * Time: 18:31
 */
public interface InputListener {
  void onChange(UpdateEvent updateEvent);

  void onComplete(CompleteEvent completeEvent);

  void onCancel(CancelEvent cancelEvent);

  void onSubmit(SubmitEvent submitEvent);
}
