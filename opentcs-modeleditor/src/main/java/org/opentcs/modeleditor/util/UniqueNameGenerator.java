// SPDX-FileCopyrightText: The openTCS Authors
// SPDX-License-Identifier: MIT
package org.opentcs.modeleditor.util;

import jakarta.inject.Inject;
import org.opentcs.guing.base.model.ModelComponent;
import org.opentcs.guing.base.model.elements.BlockModel;
import org.opentcs.guing.base.model.elements.LayoutModel;
import org.opentcs.guing.base.model.elements.LinkModel;
import org.opentcs.guing.base.model.elements.LocationModel;
import org.opentcs.guing.base.model.elements.LocationTypeModel;
import org.opentcs.guing.base.model.elements.PathModel;
import org.opentcs.guing.base.model.elements.PointModel;
import org.opentcs.guing.base.model.elements.VehicleModel;
import org.opentcs.util.UniqueStringGenerator;

/**
 */
public class UniqueNameGenerator
    extends
      UniqueStringGenerator<Class<? extends ModelComponent>> {

  @Inject
  @SuppressWarnings("this-escape")
  public UniqueNameGenerator(ElementNamingSchemeConfiguration config) {
    registerNamePattern(PointModel.class, config.pointPrefix(), config.pointNumberPattern());
    registerNamePattern(PathModel.class, config.pathPrefix(), config.pathNumberPattern());
    registerNamePattern(
        LocationTypeModel.class, config.locationTypePrefix(), config.locationTypeNumberPattern()
    );
    registerNamePattern(
        LocationModel.class, config.locationPrefix(), config.locationNumberPattern()
    );
    registerNamePattern(LinkModel.class, config.linkPrefix(), config.linkNumberPattern());
    registerNamePattern(BlockModel.class, config.blockPrefix(), config.blockNumberPattern());
    registerNamePattern(LayoutModel.class, config.layoutPrefix(), config.layoutNumberPattern());
    registerNamePattern(VehicleModel.class, config.vehiclePrefix(), config.vehicleNumberPattern());
  }
}
