/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.opentcs.guing.common.persistence.unified;

import static java.util.Objects.requireNonNull;

import jakarta.inject.Inject;
import java.io.File;
import java.io.IOException;
import javax.swing.filechooser.FileFilter;
import org.opentcs.access.to.model.PlantModelCreationTO;
import org.opentcs.guing.common.persistence.ModelFilePersistor;
import org.opentcs.util.persistence.ModelParser;

/**
 * Serializes the data kept in a {@link PlantModelCreationTO} to a xml file.
 */
public class UnifiedModelPersistor
    implements
      ModelFilePersistor {

  /**
   * The model parser.
   */
  private final ModelParser modelParser;

  /**
   * Create a new instance.
   */
  @Inject
  public UnifiedModelPersistor(ModelParser modelParser) {
    this.modelParser = requireNonNull(modelParser, "modelParser");
  }

  @Override
  public boolean serialize(PlantModelCreationTO model, File file)
      throws IOException {
    requireNonNull(model, "model");
    requireNonNull(file, "file");

    writeFile(model, file);

    return true;
  }

  @Override
  public FileFilter getDialogFileFilter() {
    return UnifiedModelConstants.DIALOG_FILE_FILTER;
  }

  private void writeFile(PlantModelCreationTO plantModel, File file)
      throws IOException {
    File outFile = file.getName().endsWith(UnifiedModelConstants.FILE_ENDING_XML)
        ? file
        : new File(
            file.getParentFile(),
            file.getName() + "." + UnifiedModelConstants.FILE_ENDING_XML
        );

    modelParser.writeModel(plantModel, outFile);
  }
}
