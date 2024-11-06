// SPDX-FileCopyrightText: The openTCS Authors
// SPDX-License-Identifier: MIT
package org.opentcs.access.rmi.services;

import java.rmi.Remote;
import java.rmi.RemoteException;
import org.opentcs.access.rmi.ClientID;
import org.opentcs.components.kernel.Query;
import org.opentcs.components.kernel.services.QueryService;

/**
 * Declares the methods provided by the {@link QueryService} via RMI.
 *
 * <p>
 * The majority of the methods declared here have signatures analogous to their counterparts in
 * {@link QueryService}, with an additional {@link ClientID} parameter which serves the purpose
 * of identifying the calling client and determining its permissions.
 * </p>
 * <p>
 * To avoid redundancy, the semantics of methods that only pass through their arguments are not
 * explicitly documented here again. See the corresponding API documentation in
 * {@link QueryService} for these, instead.
 * </p>
 */
public interface RemoteQueryService
    extends
      Remote {

  // CHECKSTYLE:OFF
  <T> T query(ClientID clientId, Query<T> query)
      throws RemoteException;
  // CHECKSTYLE:ON
}