// SPDX-FileCopyrightText: The openTCS Authors
// SPDX-License-Identifier: MIT
package org.opentcs.components.kernel;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.opentcs.components.Lifecycle;
import org.opentcs.data.model.Path;
import org.opentcs.data.model.Point;
import org.opentcs.data.model.TCSResourceReference;
import org.opentcs.data.model.Vehicle;
import org.opentcs.data.order.DriveOrder;
import org.opentcs.data.order.Route;
import org.opentcs.data.order.TransportOrder;
import org.opentcs.util.annotations.ScheduledApiChange;

/**
 * This interface declares the methods a router module for the openTCS
 * kernel must implement.
 * <p>
 * A router finds routes from a start point to an end point, rating them
 * according to implementation specific criteria/costs parameters.
 * </p>
 */
public interface Router
    extends
      Lifecycle {

  /**
   * The key of a vehicle property defining the group of vehicles that may share the same routing.
   * <p>
   * The value is expected to be an integer.
   * </p>
   */
  String PROPKEY_ROUTING_GROUP = "tcs:routingGroup";
  /**
   * The key (prefix) of a path property defining the routing cost when its travelled in forward
   * direction.
   * <p>
   * The value is expected to be a (long) integer.
   * </p>
   */
  String PROPKEY_ROUTING_COST_FORWARD = "tcs:routingCostForward";
  /**
   * The key (prefix) of a path property defining the routing cost when its travelled in reverse
   * direction.
   * <p>
   * The value is expected to be a (long) integer.
   * </p>
   */
  String PROPKEY_ROUTING_COST_REVERSE = "tcs:routingCostReverse";

  /**
   * Notifies the router to update its routing topology with respect to the given paths.
   * <p>
   * This method is supposed to be called only from the kernel executor thread.
   * </p>
   *
   * @param paths The paths to update in the routing topology. An empty set of paths results in the
   * router updating the entire routing topology.
   */
  void updateRoutingTopology(
      @Nonnull
      Set<Path> paths
  );

  /**
   * Checks the routability of a given transport order.
   * <p>
   * The check for routability is affected by path properties and configured edge evaluators. This
   * means that whether a transport order is considered routable <em>can</em> change between
   * consecutive calls to this method.
   * </p>
   * <p>
   * This method is supposed to be called only from the kernel executor thread.
   * </p>
   *
   * @param order The transport order to check for routability.
   * @return A set of vehicles for which a route for the given transport order
   * would be computable.
   */
  @Nonnull
  Set<Vehicle> checkRoutability(
      @Nonnull
      TransportOrder order
  );

  /**
   * Checks the general routability of a given transport order.
   * <p>
   * The check for general routability is <em>not</em> affected by any path properties or any
   * configured edge evaluators. This means that whether a transport order is considered generally
   * routable <em>will not</em> change between consecutive calls to this method.
   * </p>
   * <p>
   * This method is supposed to be called only from the kernel executor thread.
   * </p>
   *
   * @param order The transport order to check for routability.
   * @return {@code true}, if the transport order is generally routable, otherwise {@code false}.
   */
  @ScheduledApiChange(when = "7.0", details = "Default implementation will be removed.")
  default boolean checkGeneralRoutability(
      @Nonnull
      TransportOrder order
  ) {
    return false;
  }

  /**
   * Returns a complete route for a given vehicle that starts on a specified
   * point and allows the vehicle to process a given transport order.
   * The route is encapsulated into drive orders which correspond to those drive
   * orders that the transport order is composed of. The transport order itself
   * is not modified.
   * <p>
   * This method is supposed to be called only from the kernel executor thread.
   * </p>
   *
   * @param vehicle The vehicle for which the calculated route must be passable.
   * @param sourcePoint The position at which the vehicle would start processing
   * the transport order (i.e. the vehicle's current position).
   * @param transportOrder The transport order to be processed by the vehicle.
   * @return A list of drive orders containing the complete calculated route for
   * the given transport order, passable the given vehicle and starting on the
   * given point, or the empty optional, if no such route exists.
   */
  @Nonnull
  Optional<List<DriveOrder>> getRoute(
      @Nonnull
      Vehicle vehicle,
      @Nonnull
      Point sourcePoint,
      @Nonnull
      TransportOrder transportOrder
  );

  /**
   * Returns a route from one point to another, passable for a given vehicle.
   * <p>
   * This method is supposed to be called only from the kernel executor thread.
   * </p>
   *
   * @param vehicle The vehicle for which the route must be passable.
   * @param sourcePoint The starting point of the route to calculate.
   * @param destinationPoint The end point of the route to calculate.
   * @param resourcesToAvoid Resources to avoid when calculating the route.
   * @return The calculated route, or the empty optional, if a route between the
   * given points does not exist.
   */
  @Nonnull
  Optional<Route> getRoute(
      @Nonnull
      Vehicle vehicle,
      @Nonnull
      Point sourcePoint,
      @Nonnull
      Point destinationPoint,
      @Nonnull
      Set<TCSResourceReference<?>> resourcesToAvoid
  );

  /**
   * Returns the costs for travelling a route from one point to another with a
   * given vehicle.
   * <p>
   * This method is supposed to be called only from the kernel executor thread.
   * </p>
   *
   * @param vehicle The vehicle for which the route must be passable.
   * @param sourcePoint The starting point of the route.
   * @param destinationPoint The end point of the route.
   * @param resourcesToAvoid Resources to avoid when calculating the route.
   * @return The costs of the route, or <code>Long.MAX_VALUE</code>, if no such
   * route exists.
   * @deprecated Use {@link #getRoute(Vehicle, Point, Point, Set) } instead.
   */
  @Deprecated
  @ScheduledApiChange(when = "7.0", details = "Will be removed.")
  long getCosts(
      @Nonnull
      Vehicle vehicle,
      @Nonnull
      Point sourcePoint,
      @Nonnull
      Point destinationPoint,
      @Nonnull
      Set<TCSResourceReference<?>> resourcesToAvoid
  );

  /**
   * Notifies the router of a route being selected for a vehicle.
   * <p>
   * This method is supposed to be called only from the kernel executor thread.
   * </p>
   *
   * @param vehicle The vehicle for which a route is being selected.
   * @param driveOrders The drive orders encapsulating the route being selected,
   * or <code>null</code>, if no route is being selected for the vehicle (i.e.
   * an existing entry for the given vehicle would be removed).
   * @deprecated Will be removed without replacement. A vehicle's selected route (i.e. the list of
   * drive orders) is already contained in the transport order assigned to it.
   */
  @Deprecated
  @ScheduledApiChange(when = "7.0", details = "Will be removed.")
  void selectRoute(
      @Nonnull
      Vehicle vehicle,
      @Nullable
      List<DriveOrder> driveOrders
  );

  /**
   * Returns an unmodifiable view on the selected routes the router knows about.
   * The returned map contains an entry for each vehicle for which a selected
   * route is known.
   * <p>
   * This method is supposed to be called only from the kernel executor thread.
   * </p>
   *
   * @return An unmodifiable view on the selected routes the router knows about.
   * @deprecated Will be removed without replacement. A vehicle's selected route (i.e. the list of
   * drive orders) is already contained in the transport order assigned to it.
   */
  @Deprecated
  @ScheduledApiChange(when = "7.0", details = "Will be removed.")
  @Nonnull
  Map<Vehicle, List<DriveOrder>> getSelectedRoutes();

  /**
   * Returns all points which are currently targeted by any vehicle.
   * <p>
   * This method is supposed to be called only from the kernel executor thread.
   * </p>
   *
   * @return A set of all points currently targeted by any vehicle.
   * @deprecated Will be removed without replacement. The points targeted by vehicles can be
   * retrieved via the transport orders assigned to them.
   */
  @Deprecated
  @ScheduledApiChange(when = "7.0", details = "Will be removed.")
  @Nonnull
  Set<Point> getTargetedPoints();
}
