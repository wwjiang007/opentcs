// SPDX-FileCopyrightText: The openTCS Authors
// SPDX-License-Identifier: MIT
package org.opentcs.strategies.basic.routing.jgrapht;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opentcs.data.model.Point;

/**
 * Tests for {@link PointVertexMapper}.
 */
class PointVertexMapperTest {

  private Point pointA;
  private Point pointB;
  private Point pointC;
  private Point pointD;

  private PointVertexMapper mapper;

  @BeforeEach
  void setUp() {
    pointA = new Point("A");
    pointB = new Point("B");
    pointC = new Point("C");
    pointD = new Point("D");

    mapper = new PointVertexMapper();
  }

  @Test
  void translateEmptyPointCollectionToEmptySet() {
    Set<Vertex> result = mapper.translatePoints(new HashSet<>());

    assertThat(result).isEmpty();
  }

  @Test
  void translatePointsToVertex() {
    Set<Vertex> result
        = mapper.translatePoints(new HashSet<>(Arrays.asList(pointA, pointB, pointC, pointD)));

    assertThat(result).hasSize(4);
    assertThat(result)
        .map(vertex -> vertex.getPoint().getName())
        .contains("A", "B", "C", "D");
  }
}
