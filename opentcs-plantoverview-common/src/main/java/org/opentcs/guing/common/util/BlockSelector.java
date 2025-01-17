// SPDX-FileCopyrightText: The openTCS Authors
// SPDX-License-Identifier: MIT
package org.opentcs.guing.common.util;

import static java.util.Objects.requireNonNull;

import jakarta.inject.Inject;
import java.awt.geom.Rectangle2D;
import java.util.List;
import org.jhotdraw.draw.Figure;
import org.opentcs.guing.base.model.elements.BlockModel;
import org.opentcs.guing.common.components.drawing.OpenTCSDrawingEditor;
import org.opentcs.guing.common.components.drawing.OpenTCSDrawingView;
import org.opentcs.guing.common.persistence.ModelManager;

/**
 * A helper for selecting blocks/block elements.
 */
public class BlockSelector {

  /**
   * The application's model manager.
   */
  private final ModelManager modelManager;
  /**
   * The application's drawing editor.
   */
  private final OpenTCSDrawingEditor drawingEditor;

  /**
   * Creates a new instance.
   *
   * @param modelManager The application's model manager.
   * @param drawingEditor The application's drawing editor.
   */
  @Inject
  public BlockSelector(ModelManager modelManager, OpenTCSDrawingEditor drawingEditor) {
    this.modelManager = requireNonNull(modelManager, "modelManager");
    this.drawingEditor = requireNonNull(drawingEditor, "drawingEditor");
  }

  /**
   * Called when a block was selected, for instance in the tree view.
   * Should select all figures in the drawing view belonging to the block.
   *
   * @param block The selected block.
   */
  public void blockSelected(BlockModel block) {
    requireNonNull(block, "block");

    Rectangle2D r = null;

    List<Figure> blockElementFigures
        = ModelComponentUtil.getChildFigures(block, modelManager.getModel());

    for (Figure figure : blockElementFigures) {
      Rectangle2D displayBox = figure.getDrawingArea();

      if (r == null) {
        r = displayBox;
      }
      else {
        r.add(displayBox);
      }
    }

    if (r != null) {
      OpenTCSDrawingView drawingView = drawingEditor.getActiveView();

      drawingView.clearSelection();

      for (Figure figure : blockElementFigures) {
        drawingView.addToSelection(figure);
      }

      drawingView.updateBlock(block);
    }
  }
}
