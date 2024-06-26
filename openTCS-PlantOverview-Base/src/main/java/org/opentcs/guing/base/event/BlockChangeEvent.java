/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.opentcs.guing.base.event;

import java.util.EventObject;
import org.opentcs.guing.base.model.elements.BlockModel;

/**
 * An event that informs listener about changes in a block area.
 */
public class BlockChangeEvent
    extends
      EventObject {

  /**
   * Creates a new instance of BlockElementChangeEvent.
   *
   * @param block The <code>BlockModel</code> that has changed.
   */
  public BlockChangeEvent(BlockModel block) {
    super(block);
  }
}
