// SPDX-FileCopyrightText: The openTCS Authors
// SPDX-License-Identifier: MIT
package org.opentcs.guing.common.components.dockable;

import static java.util.Objects.requireNonNull;

import bibliothek.gui.dock.common.event.CFocusListener;
import bibliothek.gui.dock.common.intern.CDockable;
import jakarta.inject.Inject;
import java.awt.event.FocusEvent;
import org.jhotdraw.draw.DrawingEditor;
import org.opentcs.guing.common.application.ViewManager;
import org.opentcs.guing.common.components.drawing.DrawingViewScrollPane;
import org.opentcs.guing.common.components.drawing.OpenTCSDrawingView;

/**
 * Handles focussing of dockable drawing views.
 */
public class DrawingViewFocusHandler
    implements
      CFocusListener {

  /**
   * Manages the application's views.
   */
  private final ViewManager viewManager;
  /**
   * The drawing editor.
   */
  private final DrawingEditor drawingEditor;

  /**
   * Creates a new instance.
   *
   * @param viewManager Manages the application's views.
   * @param drawingEditor The drawing editor.
   */
  @Inject
  public DrawingViewFocusHandler(
      ViewManager viewManager,
      DrawingEditor drawingEditor
  ) {
    this.viewManager = requireNonNull(viewManager, "viewManager");
    this.drawingEditor = requireNonNull(drawingEditor, "drawingEditor");
  }

  @Override
  public void focusGained(CDockable dockable) {
    DrawingViewScrollPane scrollPane = viewManager.getDrawingViewMap().get(dockable);
    if (scrollPane == null) {
      return;
    }
    OpenTCSDrawingView drawView = scrollPane.getDrawingView();
    drawingEditor.setActiveView(drawView);
    // XXX Looks suspicious: Why are the same values set again here?
    drawView.setConstrainerVisible(drawView.isConstrainerVisible());
    drawView.setLabelsVisible(drawView.isLabelsVisible());
    scrollPane.setRulersVisible(scrollPane.isRulersVisible());
    drawView.getComponent().dispatchEvent(new FocusEvent(scrollPane, FocusEvent.FOCUS_GAINED));
  }

  @Override
  public void focusLost(CDockable dockable) {
  }
}
