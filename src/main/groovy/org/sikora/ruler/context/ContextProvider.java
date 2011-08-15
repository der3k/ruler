package org.sikora.ruler.context;

/**
 * Provides context for input driver event.
 */
public interface ContextProvider {
  /**
   * Returns current context.
   *
   * @return current context
   */
  Context currentContext();
}
