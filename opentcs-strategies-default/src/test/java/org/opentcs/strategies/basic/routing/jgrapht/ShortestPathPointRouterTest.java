// SPDX-FileCopyrightText: The openTCS Authors
// SPDX-License-Identifier: MIT
package org.opentcs.strategies.basic.routing.jgrapht;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import org.jgrapht.Graph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DirectedWeightedMultigraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opentcs.components.kernel.routing.Edge;
import org.opentcs.data.model.Path;
import org.opentcs.data.model.Point;
import org.opentcs.data.order.Route.Step;
import org.opentcs.strategies.basic.routing.PointRouter;

/**
 */
class ShortestPathPointRouterTest {

  private Point pointA;
  private Point pointB;
  private Point pointC;

  private Path pathAC;

  private Edge edgeAC;

  private ShortestPathPointRouter pointRouter;

  @BeforeEach
  void setUp() {
    pointA = new Point("A");
    pointB = new Point("B");
    pointC = new Point("C");

    Vertex vertexA = new Vertex(pointA.getReference());
    Vertex vertexB = new Vertex(pointB.getReference());
    Vertex vertexC = new Vertex(pointC.getReference());

    pathAC = new Path("A-->C", pointA.getReference(), pointC.getReference());

    edgeAC = new Edge(pathAC, false);

    Graph<Vertex, Edge> graph = new DirectedWeightedMultigraph<>(Edge.class);

    graph.addVertex(vertexA);
    graph.addVertex(vertexB);
    graph.addVertex(vertexC);

    graph.addEdge(vertexA, vertexC, edgeAC);
    graph.setEdgeWeight(edgeAC, 1234);

    pointRouter = new ShortestPathPointRouter(
        new DijkstraShortestPath<>(graph),
        new HashSet<>(Arrays.asList(pointA, pointB, pointC)),
        graph.vertexSet()
    );
  }

  @Test
  void returnZeroCostsIfDestinationIsSource() {
    assertEquals(0, pointRouter.getCosts(pointA.getReference(), pointA.getReference()));
  }

  @Test
  void returnEmptyRouteIfDestinationIsSource() {
    List<Step> steps = pointRouter.getRouteSteps(pointA, pointA);
    assertNotNull(steps);
    assertThat(steps, is(empty()));
  }

  @Test
  void returnInfiniteCostsIfNoRouteExists() {
    assertEquals(
        PointRouter.INFINITE_COSTS,
        pointRouter.getCosts(pointA.getReference(), pointB.getReference())
    );
  }

  @Test
  void returnNullIfNoRouteExists() {
    assertNull(pointRouter.getRouteSteps(pointA, pointB));
  }

  @Test
  void returnGraphPathCostsForExistingRoute() {
    assertEquals(
        1234,
        pointRouter.getCosts(pointA.getReference(), pointC.getReference())
    );
  }

  @Test
  void returnGraphPathStepsForExistingRoute() {
    List<Step> steps = pointRouter.getRouteSteps(pointA, pointC);
    assertNotNull(steps);
    assertThat(steps, is(not(empty())));
  }

}
