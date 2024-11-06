// SPDX-FileCopyrightText: The openTCS Authors
// SPDX-License-Identifier: MIT
package org.opentcs.strategies.basic.dispatching.phase.assignment.priorization;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.theInstance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opentcs.data.model.Vehicle;
import org.opentcs.strategies.basic.dispatching.priorization.vehicle.VehicleComparatorByEnergyLevel;

/**
 */
class VehicleComparatorByEnergyLevelTest {

  private VehicleComparatorByEnergyLevel comparator;

  @BeforeEach
  void setUp() {
    comparator = new VehicleComparatorByEnergyLevel();
  }

  @Test
  void sortHighEnergyLevelsUp() {
    Vehicle vehicle1 = new Vehicle("Vehicle1").withEnergyLevel(99);
    Vehicle vehicle2 = vehicle1.withEnergyLevel(50);
    Vehicle vehicle3 = vehicle1.withEnergyLevel(98);

    List<Vehicle> list = new ArrayList<>();
    list.add(vehicle1);
    list.add(vehicle2);
    list.add(vehicle3);

    Collections.sort(list, comparator);

    assertThat(list.get(0), is(theInstance(vehicle1)));
    assertThat(list.get(1), is(theInstance(vehicle3)));
    assertThat(list.get(2), is(theInstance(vehicle2)));
  }

}